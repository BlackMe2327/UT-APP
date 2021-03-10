package run.ut.app.mail;

import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;
import run.ut.app.exception.EmailException;
import run.ut.app.model.properties.EmailProperties;
import run.ut.app.service.OptionsService;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Abstract other.mail service.
 *
 * @author johnniang
 * @author wenjie
 */
@Slf4j
public abstract class AbstractMailService implements MailService {

    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors() << 1;
    private static final long DEFAULT_ALIVE_TIME = 600L;
    private static final int DEFAULT_QUEUE_SIZE = 1 << 14;
    protected final OptionsService optionService;
    private JavaMailSender cachedMailSender;
    private MailProperties cachedMailProperties;
    private String cachedFromName;
    @Nullable
    private ExecutorService executorService;

    protected AbstractMailService(OptionsService optionService) {
        this.optionService = optionService;
    }

    @NonNull
    public ExecutorService getExecutorService() {
        if (this.executorService == null) {
            this.executorService = new ThreadPoolExecutor(
                DEFAULT_POOL_SIZE,
                DEFAULT_POOL_SIZE << 1,
                DEFAULT_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(DEFAULT_QUEUE_SIZE),
                new DefaultThreadFactory("EmailThreadPool", true),
                new ThreadPoolExecutor.CallerRunsPolicy()
            );
        }
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Test connection with email server.
     */
    @Override
    public void testConnection() {
        JavaMailSender javaMailSender = getMailSender();
        if (javaMailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
            try {
                mailSender.testConnection();
            } catch (MessagingException e) {
                throw new EmailException("无法连接到邮箱服务器，请检查邮箱配置.[" + e.getMessage() + "]", e);
            }
        }
    }

    /**
     * Send other.mail template.
     *
     * @param callback mime message callback.
     */
    protected void sendMailTemplate(@Nullable Callback callback) {
        if (callback == null) {
            log.info("Callback is null, skip to send email");
            return;
        }

        // check if other.mail is enable
        Boolean emailEnabled = optionService.getByPropertyOrDefault(EmailProperties.ENABLED, Boolean.class);

        if (!emailEnabled) {
            // If disabled
            log.info("Email has been disabled by yourself, you can re-enable it through email settings on admin page.");
            return;
        }

        // get other.mail sender
        JavaMailSender mailSender = getMailSender();
        printMailConfig();

        // create mime message helper
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage());

        try {
            // set from-name
            messageHelper.setFrom(getFromAddress(mailSender));
            // handle message set separately
            callback.handle(messageHelper);

            // get mime message
            MimeMessage mimeMessage = messageHelper.getMimeMessage();
            // send email
            mailSender.send(mimeMessage);

            log.info("Sent an email to [{}] successfully, subject: [{}], sent date: [{}]",
                Arrays.toString(mimeMessage.getAllRecipients()),
                mimeMessage.getSubject(),
                mimeMessage.getSentDate());
        } catch (Exception e) {
            throw new EmailException("邮件发送失败，请检查 SMTP 服务配置是否正确", e);
        }
    }

    /**
     * Send other.mail template if executor service is enable.
     *
     * @param callback   callback message handler
     * @param tryToAsync if the send procedure should try to asynchronous
     */
    protected void sendMailTemplate(boolean tryToAsync, @Nullable Callback callback) {
        ExecutorService executorService = getExecutorService();
        if (tryToAsync && executorService != null) {
            // send other.mail asynchronously
            executorService.execute(() -> sendMailTemplate(callback));
        } else {
            // send other.mail synchronously
            sendMailTemplate(callback);
        }
    }

    /**
     * Get java other.mail sender.
     *
     * @return java other.mail sender
     */
    @NonNull
    private synchronized JavaMailSender getMailSender() {
        if (this.cachedMailSender == null) {
            // create other.mail sender factory
            MailSenderFactory mailSenderFactory = new MailSenderFactory();
            // get other.mail sender
            this.cachedMailSender = mailSenderFactory.getMailSender(getMailProperties());
        }

        return this.cachedMailSender;
    }

    /**
     * Get from-address.
     *
     * @param javaMailSender java other.mail sender.
     * @return from-name internet address
     * @throws UnsupportedEncodingException throws when you give a wrong character encoding
     */
    private synchronized InternetAddress getFromAddress(@NonNull JavaMailSender javaMailSender) throws UnsupportedEncodingException {
        Assert.notNull(javaMailSender, "Java other.mail sender must not be null");

        if (StringUtils.isBlank(this.cachedFromName)) {
            // set personal name
            this.cachedFromName = optionService.getByPropertyOfNonNull(EmailProperties.FROM_NAME).toString();
        }

        if (javaMailSender instanceof JavaMailSenderImpl) {
            // get user name(email)
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
            String username = mailSender.getUsername();

            // build internet address
            return new InternetAddress(username, this.cachedFromName, mailSender.getDefaultEncoding());
        }

        throw new UnsupportedOperationException("Unsupported java other.mail sender: " + javaMailSender.getClass().getName());
    }

    /**
     * Get other.mail properties.
     *
     * @return other.mail properties
     */
    @NonNull
    private synchronized MailProperties getMailProperties() {
        if (cachedMailProperties == null) {
            // create other.mail properties
            MailProperties mailProperties = new MailProperties(log.isDebugEnabled());

            // set properties
            mailProperties.setHost(optionService.getByPropertyOrDefault(EmailProperties.HOST, String.class));
            mailProperties.setPort(optionService.getByPropertyOrDefault(EmailProperties.SSL_PORT, Integer.class));
            mailProperties.setUsername(optionService.getByPropertyOrDefault(EmailProperties.USERNAME, String.class));
            mailProperties.setPassword(optionService.getByPropertyOrDefault(EmailProperties.PASSWORD, String.class));
            mailProperties.setProtocol(optionService.getByPropertyOrDefault(EmailProperties.PROTOCOL, String.class));
            this.cachedMailProperties = mailProperties;
        }

        return this.cachedMailProperties;
    }

    /**
     * Print other.mail configuration.
     */
    private void printMailConfig() {
        if (!log.isDebugEnabled()) {
            return;
        }

        // get other.mail properties
        MailProperties mailProperties = getMailProperties();
        log.debug(mailProperties.toString());
    }

    /**
     * Clear cached instance.
     */
    protected void clearCache() {
        this.cachedMailSender = null;
        this.cachedFromName = null;
        this.cachedMailProperties = null;
        log.debug("Cleared all other.mail caches");
    }

    /**
     * Message callback.
     */
    protected interface Callback {
        /**
         * Handle message set.
         *
         * @param messageHelper mime message helper
         * @throws Exception if something goes wrong
         */
        void handle(@NonNull MimeMessageHelper messageHelper) throws Exception;
    }
}