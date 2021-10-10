package run.ut.utils.csv;

/**
 * @author chenwenjie.star
 * @date 2021/10/9 5:27 下午
 */
public class HeaderProperty {
    private final String header;
    private String key;
    private final int hierachy;
    private boolean isEntity;
    private final String[] headerList;

    public HeaderProperty(String sourceHeader) {
        this.header = sourceHeader;
        headerList = header.split("\\.");
        hierachy = headerList.length;
        if (hierachy > 1) {
            isEntity = true;
            key = headerList[0];
        }
    }

    public String getHeader() {
        return header;
    }

    public String getKey() {
        return key;
    }

    public int getHierachy() {
        return hierachy;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public String[] getHeaderList() {
        return headerList;
    }
}