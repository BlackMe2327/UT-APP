package run.ut.base;

import lombok.extern.slf4j.Slf4j;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.ObjectUtils;
import org.testng.annotations.BeforeTest;
import run.ut.app.UtApplication;
import run.ut.app.model.dto.UserDTO;
import run.ut.app.model.param.EmailLoginParam;
import run.ut.app.model.support.BaseResponse;
import run.ut.app.service.RedisService;
import run.ut.app.utils.JsonUtils;
import run.ut.utils.csv.TestObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static run.ut.app.config.redis.RedisKey.USER_EMAIL_LOGIN;

/**
 * Base api test.
 *
 * @author johnniang
 */
@SpringBootTest(
        classes = UtApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("not-websocket")
@Slf4j
@AutoConfigureMockMvc
public class BaseApiTest extends AbstractTestNGSpringContextTests {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    private RedisService redisService;

    private final String LOGIN_PATH = "/user/loginByEmail";

    @BeforeTest
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public final String ADMIN_ACCOUNT = "1498780478@qq.com";
    public final String TOURIST_ACCOUNT = "chenwenjie@bytedance.com";
    public final String STUDENT_ACCOUNT = "1599603313@qq.com";


    private final String CODE = "123456";

    /**
     * 是否打印响应信息
     */
    private boolean printRes = true;

    public void setPrintRes(boolean printRes) {
        this.printRes = printRes;
    }

    /**
     * 缓存管理员登陆邮箱+验证码
     */
    public void cacheAdminLogin() {
        String key = String.format("ADMIN_EMAIL_LOGIN::%s", ADMIN_ACCOUNT);
        String key2 = String.format(USER_EMAIL_LOGIN, ADMIN_ACCOUNT);
        redisService.set(key, CODE);
        redisService.set(key2, CODE);
    }

    /**
     * 缓存游客登陆邮箱+验证码
     */
    public void cacheTouristLogin() {
        String key = String.format(USER_EMAIL_LOGIN, TOURIST_ACCOUNT);
        redisService.set(key, CODE);
    }

    /**
     * 缓存学生登陆邮箱+验证码
     */
    public void cacheStudentLogin() {
        String key = String.format(USER_EMAIL_LOGIN, STUDENT_ACCOUNT);
        redisService.set(key, CODE);
    }

    /**
     * http请求
     *
     * @param headers       请求头
     * @param method        请求方法
     * @param unJsonObject  该对象会被转化为json再发送
     * @param uri           请求地址
     * @param uriVars       restful风格api才需要
     */
    public BaseResponse httpRequest(
            String uri, HttpHeaders headers,
            Object unJsonObject, HttpMethod method,
            Object...uriVars
    ) throws Exception {

        MockHttpServletRequestBuilder request = request(method, uri, uriVars);
        if (!ObjectUtils.isEmpty(unJsonObject)) {
            if (unJsonObject.getClass() == String.class) {
                request.content((String) unJsonObject);
            } else {
                request.content(JsonUtils.objectToJson(unJsonObject));
            }
        }
        if (!ObjectUtils.isEmpty(headers) && !headers.isEmpty()) {
            request.headers(headers);
        }

        ResultActions perform = mvc.perform(
                request.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
        );
        if (printRes) {
            perform.andDo(print());
        }
        MvcResult mvcResult = perform.andReturn();

        String response = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return JsonUtils.jsonToObject(response, BaseResponse.class);
    }


    /**
     * 登陆
     *
     * @param email       账号邮箱
     * @param code        验证码
     * @return            用户信息（包括Token）
     * @throws Exception  mock异常
     */
    public UserDTO login(String email, String code) throws Exception {
        EmailLoginParam emailLoginParam = new EmailLoginParam();
        emailLoginParam.setEmail(email);
        emailLoginParam.setCode(code);

        BaseResponse baseResponse = httpRequest(LOGIN_PATH, null, emailLoginParam, HttpMethod.POST);
        return JsonUtils.mapToObject((Map<?, ?>) baseResponse.getData(), UserDTO.class);
    }

    /**
     * 登陆管理员账号
     *
     * @see #login
     */
    public UserDTO loginByAdmin() throws Exception {
        return login(ADMIN_ACCOUNT, CODE);
    }

    /**
     * 登陆游客账号
     *
     * @see #login
     */
    public UserDTO loginByTourist() throws Exception {
        return login(TOURIST_ACCOUNT, CODE);
    }

    /**
     * 登陆学生账号
     *
     * @see #login
     */
    public UserDTO loginByStudent() throws Exception {
        return login(STUDENT_ACCOUNT, CODE);
    }


    /**
     * 获取管理员Token
     *
     * @see #login
     */
    public String getAdminToken() throws Exception {
        return loginByAdmin().getToken().getAccessToken();
    }

    /**
     * 获取游客Token
     *
     * @see #login
     */
    public String getTouristToken() throws Exception {
        return loginByTourist().getToken().getAccessToken();
    }
    /**
     * 获取学生Token
     *
     * @see #login
     */
    public String getStudentToken() throws Exception {
        return loginByStudent().getToken().getAccessToken();
    }

    /**
     * 构造带有token的请求头对象
     *
     * @param token    token
     * @return         带有token的请求头对象
     */
    public HttpHeaders generatorHeaderByToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("UT-Token", token);
        return headers;
    }

    protected void printTestTitle(TestObject testObject) {
        log.info("当前测试用例为：【{}】", testObject.getTestTitle());
    }

}
