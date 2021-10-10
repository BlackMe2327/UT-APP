package run.ut.utils.csv;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author chenwenjie.star
 * @date 2021/10/9 5:12 下午
 */
public class DataProviderUtil {
    private DataProviderUtil() {
    }

    public static Iterator<Object[]> getEntitiesFromCsv(Class<?> clazz, LinkedHashMap<String, Class<?>> entityClazzMap, String filename) {
        System.gc();
        Iterator<Object[]> objectIterator = CSVUtil.getDataFromCSV(clazz, filename);
        return EntityEncapsulator.toEntitiesFromObjectArray(objectIterator, entityClazzMap);
    }
}