package im.youdu.sdk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class ZipUtil {

    private ZipUtil(){}

    public static byte[] gzipUncompressionOrgFile(byte[] data) throws IOException {
        InputStream in = new ByteArrayInputStream(data);
        data = gzipUncompressionOrgFile(in);
        return data;
    }

    public static byte[] gzipUncompressionOrgFile(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[1024];
        int n;
        while ((n = ungzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

}
