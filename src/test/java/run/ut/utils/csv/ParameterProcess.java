package run.ut.utils.csv;

import cn.hutool.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import run.ut.app.utils.JsonUtils;

import java.util.*;

/**
 * @author chenwenjie.star
 * @date 2021/10/9 5:19 下午
 */
class ParameterProcess {
    private static final Logger logger = LoggerFactory.getLogger(ParameterProcess.class);

    private ParameterProcess() {
    }

    public static Object getOneParameter(List<HeaderProperty> headerPropertyList, Object[] originalRowData,
                                         Map.Entry<String, Class<?>> entityClazzEntry) {
        for (HeaderProperty headerProperty : headerPropertyList) {
            if (StringUtils.equalsIgnoreCase(headerProperty.getHeader(), entityClazzEntry.getKey())) {
                return originalRowData[headerPropertyList.indexOf(headerProperty)];
            }
        }
        return null;
    }

    public static Object getJsonParameter(List<HeaderProperty> headerPropertyList, Object[] originalRowData,
                                          Map.Entry<String, Class<?>> entityClazzEntry) {
        for (HeaderProperty headerProperty : headerPropertyList) {
            if (StringUtils.equalsIgnoreCase(headerProperty.getHeader(), entityClazzEntry.getKey())) {
                return new JSONObject(originalRowData[headerPropertyList.indexOf(headerProperty)]);
            }
        }
        return null;
    }

    public static Object getOneEntity(List<HeaderProperty> headerPropertyList, Object[] originalRowData,
                                      Map.Entry<String, Class<?>> entityClazzEntry) {

        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < headerPropertyList.size(); i++) {
            HeaderProperty headerProperty = headerPropertyList.get(i);
            if (headerProperty.isEntity()
                    && !originalRowData[i].equals("")
                    && headerProperty.getKey().equals(entityClazzEntry.getKey())) {
                map = getNewParameterMap(headerProperty.getHeader(), originalRowData[i].toString(), map);
            }
        }

        if (map == null || map.isEmpty()) {
            try {
                return entityClazzEntry.getValue().newInstance();
            } catch (Exception e) {
                logger.error(entityClazzEntry.getKey() + " 未传入参数时，创建Instance异常", e);
                return null;
            }
        }

        map = (Map<String, Object>) map.get(entityClazzEntry.getKey());

        JSONObject jsonObject = new JSONObject();
        for (String key : map.keySet()) {
            if (map.get(key) instanceof Map) {
                jsonObject = mapTypeProcess(map, key);
            } else {
                jsonObject.put(key, map.get(key));
            }
        }

        return JsonUtils.jsonToObject(jsonObject.toString(), entityClazzEntry.getValue());
    }

    private static Map<String, Object> getNewParameterMap(String header, String rowData, Map<String, Object> map) {
        int index = header.indexOf(".");
        if (index > 0) {
            String keyLast = header.substring(index + 1);
            String firstKey = header.substring(0, index);
            if (map.containsKey(firstKey)) {
                Object childMap = map.get(firstKey);
                if (childMap instanceof Map) {
                    getNewParameterMap(keyLast, rowData, (Map<String, Object>) childMap);
                } else {
                    logger.error(header + " is error parameter !!!");
                    return null;
                }
            } else {
                map.put(firstKey, getNewParameterMap(keyLast, rowData, new LinkedHashMap<>()));
            }
            return map;
        } else {
            map.put(header, rowData);
            return map;
        }
    }

    private static JSONObject mapTypeProcess(Map<String, Object> map, String key) {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> secondMap = (Map<String, Object>) map.get(key);
        Set<String> secondKeys = secondMap.keySet();

        JSONObject secondObject = new JSONObject();
        secondKeys.forEach(sk -> secondObject.put(sk, secondMap.get(sk)));
        jsonObject.put(key, secondObject);

        return jsonObject;
    }

}
