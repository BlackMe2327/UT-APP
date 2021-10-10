package run.ut.utils.csv;

import run.ut.app.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenwenjie.star
 * @date 2021/10/9 5:42 下午
 */
public class TestObject {

    private String testMethod = "";
    private String testTitle = "";
    private String paramJson = "";
    private String respCode = "";

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    public String getParamJson() {
        return paramJson;
    }

    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
    }

    public Map<String, String> parseParamJsonToMap() {
        return (Map<String, String>) JsonUtils.jsonToObject(paramJson, HashMap.class);
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespCode() {
        return respCode;
    }

    public Integer parseRespCodeToInt() {
        return Integer.parseInt(respCode);
    }
}