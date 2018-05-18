package im.youdu.sdk.client;

import im.youdu.sdk.entity.SessionCreateBody;
import im.youdu.sdk.entity.SessionInfo;
import im.youdu.sdk.entity.SessionUpdateBody;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import junit.framework.TestCase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SessionMsgClientTest extends TestCase {
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "sysOrgAssistant"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "n76ut0qxPWozXbMxGMt8s9pgxUZKUxu/GJ5R5dz+u4g="; // 请填写企业应用的EncodingaesKey

    private SessionMsgClient sessionClient;

    public SessionMsgClientTest(){
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, "", APP_ID, "", APP_AESKEY);
        sessionClient = new SessionMsgClient(app);
    }

    String testSessonId = "{7248ADE6-C9A9-4119-A9F5-F58CFE62F836}";
    //测试创建会话
    public void testCreateSession() throws ParamParserException, HttpRequestException, AESCryptoException {
        SessionCreateBody body = new SessionCreateBody();
        body.setTitle("接口测试创建会话");
        body.setCreator("max.chen");
        List<String> mems = new ArrayList<String>();
        mems.add("ad1");
        mems.add("ad2");
        mems.add("ad3");
        body.setMember(mems);
        SessionInfo sess = sessionClient.createSession(body);
        testSessonId = sess.getSessionId();
        printSession("create session ok:",sess);
    }

    //测试获取会话
    public void testGetSession() throws ParamParserException, HttpRequestException, AESCryptoException, UnsupportedEncodingException {
        SessionInfo sess = sessionClient.getSession(testSessonId);
        printSession("get session ok:",sess);
    }

    //测试更新会话
    public void testUpdateSession() throws ParamParserException, HttpRequestException, AESCryptoException {
        SessionUpdateBody body = new SessionUpdateBody();
        body.setSessionId(testSessonId);
        body.setOpUser("cs10");
        body.setTitle("更新后的会话名称");
        List<String> addUser = new ArrayList<String>();
        addUser.add("cs1");
        addUser.add("cs5");
        addUser.add("cs6");
        addUser.add("cs7");
        body.setAddMember(addUser);
        List<String> delUser = new ArrayList<String>();
        delUser.add("cs4");
        body.setDelMember(delUser);
        SessionInfo sess = sessionClient.updateSession(body);
        printSession("update session ok:",sess);
    }

    private void printSession(String msg, SessionInfo sess){
        System.out.println("---------------------------------");
        System.out.println(msg);
        System.out.println("会话ID:"+sess.getSessionId());
        System.out.println("会话名称:"+sess.getTitle());
        System.out.println("会话类型:"+sess.getType());
        System.out.println("会话创建者:"+sess.getOwner());
        System.out.println("会话版本:"+sess.getVersion());
        System.out.println("会话成员:");
        List<String> mems = sess.getMember();
        if(null == mems || mems.size()==0){
            System.out.println("没有成员");
        }else{
            for(String str : mems){
                System.out.println(str);
            }
        }
        System.out.println("---------------------------------");
    }
//----------------------------------------------------------------------
    String msgFrom = "cs1";
    String msgTo = "max.chen";
    String text = "有度即时通";
    String imgPath = "D:\\pics\\2018\\2018-01-05.jpg";

    //测试发送单人会话文字消息
    public void testSendSingleTxtMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        sessionClient.sendSingleTextMsg(msgFrom,msgTo,text);
    }

    //测试发送单人会话图片消息
    public void testSendSingleImgMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        sessionClient.sendSingleImgMsgV1(msgFrom,msgTo,imgPath);
    }

    //测试发送单人会话文件消息
    public void testSendSingleFileMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        sessionClient.sendSingleFileMsgV1(msgFrom,msgTo,imgPath);
    }

    //测试发送多人会话文字消息
    public void testSendSessionTxtMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        sessionClient.sendSessionTextMsg(msgFrom,testSessonId,text);
    }

    //测试发送多人会话图片消息
    public void testSendSessionImgMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        sessionClient.sendSessionImgMsgV1(msgFrom,testSessonId,imgPath);
    }

    //测试发送多人会话文件消息
    public void testSendSessionFileMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        sessionClient.sendSessionFileMsgV1(msgFrom,testSessonId,imgPath);
    }

//----------------------------------------------------------------------
    String str = "aqwe请问rty儿uui童ioioo椅pUlkIj哦h破h了g客g家f话f给fQdWdEdRsTaYzUxIc噢v普b朗n克m激AS活D过F范G德H萨J自K行L车PVOBI你U们Y门T板R是E从W小Q杂Z水X电C费V规B划N局M科伦坡";
    public void testBatchSendSingleTextMsg() throws ParamParserException, HttpRequestException, AESCryptoException, InterruptedException {
        String from = "cs1";
        String to = "max.chen";
        String content = "";
        for (int k = 1; k <= 10000; k++){
            content = randomMsg();
            content = k+":松:"+content;
            System.out.println(content);
            sessionClient.sendSingleTextMsg(from, to, content);
            Thread.sleep(10);
        }
        System.out.println("send single text msg ok.");
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

