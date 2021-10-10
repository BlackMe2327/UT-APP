package run.ut.utils.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;

/**
 * @author chenwenjie.star
 * @date 2021/10/9 5:09 下午
 */
public class EntryParser implements CSVEntryParser<Object[]> {
    @Override
    public Object[] parseEntry(String... data) {
        return data;
    }
}
