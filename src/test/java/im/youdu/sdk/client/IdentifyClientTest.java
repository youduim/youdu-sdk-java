package im.youdu.sdk.client;

import im.youdu.sdk.entity.UserInfo;
import junit.framework.TestCase;

public class IdentifyClientTest extends TestCase {
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String YD_IDENTIFY_TOKEN = "PUJPfC9hXnxmd3mo9_h122GPEDOyNa5l5EUWUOzNoWGPbdvhPdYfFy1kSoMPzeWecvDFnADFEqUc2FHF4fi26Q==";

    private IdentifyClient identifyClient;

    public IdentifyClientTest() {
        this.identifyClient = new IdentifyClient(YDSERVER_HOST);
    }

    //身份认证
    public void testIdentify() throws Exception {
       UserInfo userInfo = identifyClient.idetify(YD_IDENTIFY_TOKEN);
       System.out.println(userInfo);
    }
}
