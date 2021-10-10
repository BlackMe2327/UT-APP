package run.ut.api.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import run.ut.app.model.support.BaseResponse;
import run.ut.base.BaseApiTest;
import run.ut.utils.AssertUtil;
import run.ut.utils.csv.DataProviderUtil;
import run.ut.utils.csv.TestObject;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 用户tag测试
 *
 * @author chenwenjie.star
 * @date 2021/10/9 6:33 下午
 */
@Slf4j
@ActiveProfiles("not-websocket")
public class UserTagApiTest extends BaseApiTest {

    private final String USER_TAG_API = "/user/saveUserTags";

    @DataProvider(name = "data")
    public static Iterator<Object[]> getItemData(Method m, ITestContext testContext) {

        LinkedHashMap<String, Class<?>> entityClazzMap = new LinkedHashMap<>();
        entityClazzMap.put("TestObject", TestObject.class);
        return DataProviderUtil.getEntitiesFromCsv(
                UserTagApiTest.class,
                entityClazzMap,
                "./UserTagApiTest.csv"
        );
    }


    @Test(testName = "未登录设置tag")
    public void showSelfPageTest(ITestContext context) throws Exception {

        Integer[] tagIds = new Integer[]{1, 2, 3};
        BaseResponse res = httpRequest(USER_TAG_API, null, tagIds, HttpMethod.POST);
        AssertUtil.assertEquals((int) res.getStatus(), 401, "没登录保存tag返回码 != 401");
    }

    @Test(testName = "未认证用户设置tag")
    public void touristSetTag(ITestContext context) throws Exception {

        String token = getTouristToken();
        HttpHeaders headers = generatorHeaderByToken(token);

        Integer[] tagIds = new Integer[]{1,2,3};
        BaseResponse res = httpRequest(USER_TAG_API, headers, tagIds, HttpMethod.POST);

        AssertUtil.assertEquals(res.getStatus().intValue(), 401, "为认证用户设置tag response code != 401");
    }

    @Test(testName = "认证用户设置tag", dataProvider = "data")
    public void studentSetTag(TestObject testObject, ITestContext context) throws Exception {

        printTestTitle(testObject);

        String tagIds = testObject.getParamJson();

        String token = getStudentToken();
        HttpHeaders headers = generatorHeaderByToken(token);

        BaseResponse res = httpRequest(USER_TAG_API, headers, tagIds, HttpMethod.POST);

        AssertUtil.assertEquals(res.getStatus(), testObject.parseRespCodeToInt(), "返回码异常");
    }
}
