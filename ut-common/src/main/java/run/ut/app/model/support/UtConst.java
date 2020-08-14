package run.ut.app.model.support;

import java.io.File;
import java.io.Serializable;

/**
 * <pre>
 *     公共常量
 * </pre>
 *
 * @author wenjie
 */
public class UtConst implements Serializable {

    /**
     * User home directory.
     */
    public final static String USER_HOME = System.getProperties().getProperty("user.home");

    /**
     * Temporary directory.
     */
    public final static String TEMP_DIR = System.getProperties().getProperty("java.io.tmpdir");

    /**
     * Static pages pack prefix.
     */
    public final static String STATIC_PAGE_PACK_PREFIX = "static-pages-";

    /**
     * Default theme name.
     */
    public final static String DEFAULT_THEME_ID = "caicai_anatole";

    /**
     * Path separator.
     */
    public static final String FILE_SEPARATOR = File.separator;

    /**
     * Suffix of freemarker template file
     */
    public static final String SUFFIX_FTL = ".ftl";

    /**
     * Custom freemarker tag method key.
     */
    public static final String METHOD_KEY = "method";
    /**
     * 网易云音乐短代码前缀
     */
    public static final String NETEASE_MUSIC_PREFIX = "[music:";
    /**
     * 网易云音乐 iframe 代码
     */
    public static final String NETEASE_MUSIC_IFRAME = "<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=330 height=86 src=\"//music.163.com/outchain/player?type=2&id=$1&auto=1&height=66\"></iframe>";
    /**
     * 网易云音乐短代码正则表达式
     */
    public static final String NETEASE_MUSIC_REG_PATTERN = "\\[music:(\\d+)\\]";
    /**
     * 哔哩哔哩视频短代码前缀
     */
    public static final String BILIBILI_VIDEO_PREFIX = "[bilibili:";
    /**
     * 哔哩哔哩视频 iframe 代码
     */
    public static final String BILIBILI_VIDEO_IFRAME = "<iframe height=$3 width=$2 src=\"//player.bilibili.com/player.html?aid=$1\" scrolling=\"no\" border=\"0\" frameborder=\"no\" framespacing=\"0\" allowfullscreen=\"true\"> </iframe>";
    /**
     * 哔哩哔哩视频正则表达式
     */
    public static final String BILIBILI_VIDEO_REG_PATTERN = "\\[bilibili:(\\d+)\\,(\\d+)\\,(\\d+)\\]";
    /**
     * YouTube 视频短代码前缀
     */
    public static final String YOUTUBE_VIDEO_PREFIX = "[youtube:";
    /**
     * YouTube 视频 iframe 代码
     */
    public static final String YOUTUBE_VIDEO_IFRAME = "<iframe width=$2 height=$3 src=\"https://www.youtube.com/embed/$1\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
    /**
     * YouTube 视频正则表达式
     */
    public static final String YOUTUBE_VIDEO_REG_PATTERN = "\\[youtube:(\\w+)\\,(\\d+)\\,(\\d+)\\]";

}
