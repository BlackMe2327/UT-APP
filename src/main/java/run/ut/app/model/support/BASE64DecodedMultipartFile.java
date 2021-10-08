package run.ut.app.model.support;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * base64转MultipartFile
 *
 * @author wenjie
 */
public class BASE64DecodedMultipartFile implements MultipartFile {

    private final byte[] imgContent;
    private final String header;

    public BASE64DecodedMultipartFile(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }

    @Override
    public String getName() {
        return System.currentTimeMillis() + Math.random() + "." + header.split("/")[1];
    }

    @Override
    public String getOriginalFilename() {
        return System.currentTimeMillis() + (int) (Math.random() * 10000) + "." + header.split("/")[1];
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IllegalStateException, IOException {
        new FileOutputStream(dest).write(imgContent);
    }

    /**
     * base64转MultipartFile文件
     */
    public static MultipartFile base64ToMultipart(String base64) {
        String[] baseStrs = base64.split(",");

        byte[] b = new byte[0];
        b = Base64Decoder.decode(baseStrs[1]);

        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }

        return new BASE64DecodedMultipartFile(b, baseStrs[0]);
    }


    public static String imageToBase64(File file) throws FileNotFoundException {

        InputStream in = new FileInputStream(file);
        byte[] data = null;

        // 读取图片字节数组
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "data:image/jpeg;base64," + Base64Encoder.encode(data);
    }
}
