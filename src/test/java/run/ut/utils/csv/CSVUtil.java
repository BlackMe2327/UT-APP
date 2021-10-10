package run.ut.utils.csv;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author chenwenjie.star
 * @date 2021/10/9 5:05 下午
 */
public class CSVUtil {

    private static Logger logger = LoggerFactory.getLogger(CSVUtil.class);

    private CSVUtil() {

    }

    public static Iterator<Object[]> getDataFromCSV(Class<?> clazz, String filename) {
        if (!StringUtils.containsIgnoreCase(filename,".csv")) {
            throw new IllegalArgumentException("Filename needs to be ended with '.csv' ");
        }

        InputStream in = null;
        InputStreamReader inReader = null;
        CSVReader<Object[]> csvReader = null;
        List<Object[]> lists = new ArrayList<>();

        try {
            if (clazz != null) {
                in = clazz.getResourceAsStream(filename);
            } else {
                in = new FileInputStream(filename);
            }
            if (in == null) {
                throw new FileNotFoundException("FileNotFound: " + filename);
            }

            inReader = new InputStreamReader(in);
            csvReader = new CSVReaderBuilder<Object[]>(inReader)
                    .strategy(CSVStrategy.UK_DEFAULT)
                    .entryParser(new EntryParser())
                    .build();

            inReader.close();

            if (clazz != null) {
                in = clazz.getResourceAsStream(filename);
            } else {
                in = new FileInputStream(filename);
            }
            if (in == null) {
                throw new FileNotFoundException("FileNotFound: " + filename);
            }

            inReader = new InputStreamReader(in, StandardCharsets.UTF_8);

            csvReader.close();
            csvReader = new CSVReaderBuilder<Object[]>(inReader)
                    .strategy(CSVStrategy.UK_DEFAULT)
                    .entryParser(new EntryParser())
                    .build();

            lists.add(csvReader.readHeader().toArray());
            lists.addAll(csvReader.readAll());

            return lists.iterator();

        } catch (Exception e) {
            logger.error("deal with CSV exception", e);
            throw new RuntimeException("deal with CSV exception");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (inReader != null) {
                    inReader.close();
                }

                if (csvReader != null) {
                    csvReader.close();
                }

            } catch (Exception e) {
                logger.error("CSV file close Exception");
                throw new RuntimeException("CSV file close Exception", e);
            }
        }
    }
}