package im.youdu.sdk.client;

import im.youdu.sdk.entity.FileInfo;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import junit.framework.TestCase;

import java.io.IOException;

public class ChatMsgMenuClientTest extends TestCase {
    private static final int BUIN = 14797363; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "yd901B3FD68E2048D8822238A3D5888878"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "lZ/BHIk167HMScKEAWMIwPR4ivqJltn+YgMGvyf8BMc="; // 请填写企业应用的EncodingaesKey

    private ChatMsgMenuClient chatMsgMenuClient;

    public ChatMsgMenuClientTest() throws Exception {
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, "", APP_ID, "", APP_AESKEY);
        this.chatMsgMenuClient = new ChatMsgMenuClient(app);
    }

    public void testDownloadZipFile() throws ParamParserException, HttpRequestException, AESCryptoException, IOException, FileIOException {
        String forwardId = "100028-41ad242d07ce6e8d70301588aad069c9-44793";
        String dir = "D:\\Test\\jsdk\\";

        FileInfo fileInfo = this.chatMsgMenuClient.downloadChatMsgZipFileAndSave(forwardId, dir);
        System.out.println("save file to" + fileInfo.getPath());
    }


}
