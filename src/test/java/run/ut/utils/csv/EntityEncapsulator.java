package run.ut.utils.csv;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author chenwenjie.star
 * @date 2021/10/9 5:17 下午
 */
public class EntityEncapsulator {
    private EntityEncapsulator() {

    }

    public static Iterator<Object[]> toEntitiesFromObjectArray(Iterator<Object[]> objectIterator,
                                                               LinkedHashMap<String, Class<?>> entityCllazzMap) {

        List<Object[]> list = new ArrayList<>();
        List<HeaderProperty> headerList = getHeaderList(objectIterator);

        while (objectIterator.hasNext()) {
            List<Object> encapsuledRowData = new ArrayList<>();
            Object[] originalRowData = FillMissingItems(objectIterator, headerList);

            if (entityCllazzMap != null) {
                for (Map.Entry<String, Class<?>> stringClazzEntry : entityCllazzMap.entrySet()) {
                    Object object;
                    if (StringUtils.containsIgnoreCase(stringClazzEntry.getValue().getName(), "json")) {
                        object = ParameterProcess.getJsonParameter(headerList, originalRowData, stringClazzEntry);
                    } else if (checkParamType(stringClazzEntry)) {
                        object = ParameterProcess.getOneParameter(headerList, originalRowData, stringClazzEntry);
                    } else {
                        object = ParameterProcess.getOneEntity(headerList, originalRowData, stringClazzEntry);
                    }
                    encapsuledRowData.add(object);
                }
            }

            list.add(encapsuledRowData.toArray(new Object[]{encapsuledRowData.size()}));
        }
        return list.iterator();
    }

    private static List<HeaderProperty> getHeaderList(Iterator<Object[]> objectIterator) {
        Object[] headers = null;
        if (objectIterator.hasNext()) {
            headers = objectIterator.next();
        }

        if (null == headers) {
            throw new RuntimeException("未获取到测试数据，请检查测试数据装载文件以及文件填写是否正确");
        }

        List<HeaderProperty> headerList = new ArrayList<>();
        Arrays.asList(headers).forEach(header -> headerList.add(new HeaderProperty((String) header)));

        return headerList;
    }

    private static Object[] FillMissingItems(Iterator<Object[]> objectIterator, List<HeaderProperty> headerList) {
        Object[] originalRowData = new Object[headerList.size()];
        Object[] originalRowData1 = objectIterator.next();

        for (int od = 0; od < originalRowData1.length; od++) {
            originalRowData[od] = originalRowData1[od];
        }
        for (int j = originalRowData1.length; j < headerList.size(); j++) {
            originalRowData[j] = "";
        }

        return originalRowData;
    }

    /**
     * 判断
     *
     * @param stringClazzEntry
     * @return
     */
    private static boolean checkParamType(Map.Entry<String, Class<?>> stringClazzEntry) {
        return StringUtils.startsWithIgnoreCase(stringClazzEntry.getValue().getName(), "java.lang") ||
                stringClazzEntry.getValue().isPrimitive();
    }

}
