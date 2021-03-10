package run.ut.app;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * UT main class
 *
 * @author wenjie
 */

@SpringBootApplication
@EnableAsync
@EnableScheduling
@DubboComponentScan(basePackages = "run.ut.app.service.impl")
public class UtBbsServiceApplication extends SpringBootServletInitializer {

    private static ConfigurableApplicationContext CONTEXT;

    public static void main(String[] args) {
        System.setProperty("spring.config.additional-location", "file:${user.home}/.ut-bbs-service/");
        CONTEXT = SpringApplication.run(UtBbsServiceApplication.class);
    }

    public static void close() {
        Thread thread = new Thread(() -> {
            CONTEXT.close();
        });

        thread.setDaemon(false);
        thread.start();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // Customize the spring config location
        System.setProperty("spring.config.additional-location", "file:${user.home}/.ut-bbs-service/");
        return super.configure(builder);
    }
}