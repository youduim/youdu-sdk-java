package im.youdu.sdk.client;

import im.youdu.sdk.entity.SysMsgBody;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.*;
import junit.framework.TestCase;

public class SysMsgClientTest extends TestCase {

    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_NAME = "系统消息"; //应用名称
    private static final String APP_ID = "sysMsgAssistant"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "nHff0+CrZRd0jg/o+b2BpLVVI0XzgOkasRaz50k80KE="; // 请填写企业应用的EncodingaesKey

    private SysMsgClient msgClient;

    public SysMsgClientTest() throws Exception {
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, APP_NAME, APP_ID, "", APP_AESKEY);
        msgClient = new SysMsgClient(app);
    }

    //发送系统消息
    public void testSendSysMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        SysMsgBody sysMsg = new SysMsgBody();
        sysMsg.setTitle("有度即时通");
        sysMsg.addTextBody("欢迎使用有度即时通：");
        sysMsg.addLinkBody("https://www.youdu.cn","有度官网",0);
        String receiveUsers = "test1|test2";
        String receiveDepts = "1|2|3";
        msgClient.sendSysMsg(receiveUsers,receiveDepts,sysMsg);
    }
}
