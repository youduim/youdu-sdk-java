package im.youdu.sdk.client;

import im.youdu.sdk.entity.FileInfo;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import junit.framework.TestCase;

import java.io.IOException;

public class CustomMenuClientTest extends TestCase {
    private static final int BUIN = 14797363; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "yd901B3FD68E2048D8822238A3D5888878"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "lZ/BHIk167HMScKEAWMIwPR4ivqJltn+YgMGvyf8BMc="; // 请填写企业应用的EncodingaesKey

    private CustomMenuClient customMenuClient;

    public CustomMenuClientTest() throws Exception {
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, "", APP_ID, "", APP_AESKEY);
        this.customMenuClient = new CustomMenuClient(app);
    }

    public void testDownloadZipFile() throws ParamParserException, HttpRequestException, AESCryptoException, IOException, FileIOException {
        String forwardId = "100028_0f27042568b8bf0b469f0033e5c63a5f-1008372_2020-08-13";
        String dir = "D:\\Test\\jsdk\\";

        FileInfo fileInfo = this.customMenuClient.downloadChatMsgZipFileAndSave(forwardId, dir);
        System.out.println("save file to" + fileInfo.getPath());
    }


}
