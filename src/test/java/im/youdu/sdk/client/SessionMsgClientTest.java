package im.youdu.sdk.client;

import im.youdu.sdk.entity.SessionCreateBody;
import im.youdu.sdk.entity.SessionInfo;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class SessionMsgClientTest extends TestCase {
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "sysOrgAssistant"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "jFf3li+v5krxk5xz4B4TzlGmMup+X7cofjO06e5brk0="; // 请填写企业应用的EncodingaesKey

    private SessionMsgClient msgClient;

    String str = "aqwe请问rty儿uui童ioioo椅pUlkIj哦h破h了g客g家f话f给fQdWdEdRsTaYzUxIc噢v普b朗n克m激AS活D过F范G德H萨J自K行L车PVOBI你U们Y门T板R是E从W小Q杂Z水X电C费V规B划N局M科伦坡";

    public SessionMsgClientTest(){
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, "", APP_ID, "", APP_AESKEY);
        msgClient = new SessionMsgClient(app);
    }

    public void testSendSingleTextMsg() throws ParamParserException, HttpRequestException, AESCryptoException, InterruptedException {
        String from = "cs1";
        String to = "max.chen";
        String content = "";
        for (int k = 1; k <= 10000; k++){
            content = randomMsg();
            content = k+":松:"+content;
            System.out.println(content);
            msgClient.sendSingleTextMsg(from, to, content);
            Thread.sleep(10);
        }
        System.out.println("send single text msg ok.");
    }

    public void testCreateSession() throws ParamParserException, HttpRequestException, AESCryptoException {
        SessionCreateBody body = new SessionCreateBody();
        body.setTitle("接口测试创建会话");
        body.setCreator("max.chen");
        List<String> mems = new ArrayList<String>();
        mems.add("max.chen");
        mems.add("cs1");
        mems.add("cs2");
        body.setMember(mems);
       SessionInfo sess = msgClient.CreateSession(body);
       System.out.println("create session ok:"+sess);
    }

    private String randomMsg(){
        int msgLen = (int)(Math.random()*20);
        if(msgLen==0){
            msgLen = 10;
        }
        StringBuffer buff = new StringBuffer("");
        int len = str.length();
        for(int k=0;k<msgLen;k++){
            int i = (int)(Math.random()*len);
            buff.append(str.charAt(i));
        }
        return buff.toString();
    }
}
