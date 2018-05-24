package im.youdu.sdk.client;

import im.youdu.sdk.entity.SessionCreateBody;
import im.youdu.sdk.entity.SessionInfo;
import im.youdu.sdk.entity.SessionUpdateBody;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;
import junit.framework.TestCase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SessionClientTest extends TestCase {
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "sysOrgAssistant"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "n76ut0qxPWozXbMxGMt8s9pgxUZKUxu/GJ5R5dz+u4g="; // 请填写企业应用的EncodingaesKey

    private SessionClient sessionClient;

    public SessionClientTest(){
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, "", APP_ID, "", APP_AESKEY);
        sessionClient = new SessionClient(app);
    }

    //创建会话
    public void testCreateSession() throws ParamParserException, HttpRequestException, AESCryptoException {
        SessionCreateBody body = new SessionCreateBody();
        body.setTitle("测试创建会话");
        body.setCreator("test1");
        List<String> mems = new ArrayList<>();
        mems.add("test2");
        mems.add("test3");
        body.setMember(mems);
        SessionInfo sess = sessionClient.createSession(body);
        printSession("create session ok:",sess);
    }

    //获取会话
    public void testGetSession() throws ParamParserException, HttpRequestException, AESCryptoException, UnsupportedEncodingException {
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        SessionInfo sess = sessionClient.getSession(sessionId);
        printSession("get session ok:",sess);
    }

    //修改会话名称
    public void testUpdateSessionTitle() throws ParamParserException, HttpRequestException, AESCryptoException {
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        String title = "修改后的标题";
        String opUser = "test1";
        SessionInfo sess = sessionClient.updateSessionTitle(sessionId,opUser,title);
        printSession("update session title ok:",sess);
    }

    //增加会话成员
    public void testAddSessionMember() throws ParamParserException, HttpRequestException, AESCryptoException {
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        String opUser = "test1";
        List<String> addMems = new ArrayList<>();
        addMems.add("test4");
        addMems.add("test5");

        SessionUpdateBody body = new SessionUpdateBody();
        body.setSessionId(sessionId);
        body.setOpUser(opUser);
        body.setAddMember(addMems);
        SessionInfo sess = sessionClient.updateSession(body);
        printSession("add session member ok:",sess);
    }

    //删除会话成员
    public void testDelSessionMember() throws ParamParserException, HttpRequestException, AESCryptoException {
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        String opUser = "test1";
        List<String> delMems = new ArrayList<>();
        delMems.add("test4");
        delMems.add("test5");

        SessionUpdateBody body = new SessionUpdateBody();
        body.setSessionId(sessionId);
        body.setOpUser(opUser);
        body.setDelMember(delMems);
        SessionInfo sess = sessionClient.updateSession(body);
        printSession("del session member ok:",sess);
    }

    //整个更新会话
    public void testUpdateSession() throws ParamParserException, HttpRequestException, AESCryptoException {
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        String opUser = "test1";
        String title = "第二次修改会话标题";
        List<String> addUser = new ArrayList<>();
        addUser.add("test6");
        addUser.add("test7");
        addUser.add("max.chen");
        List<String> delUser = new ArrayList<>();
        delUser.add("test4");
        delUser.add("test5");


        SessionUpdateBody body = new SessionUpdateBody();
        body.setSessionId(sessionId);
        body.setOpUser(opUser);
        body.setTitle(title);
        body.setAddMember(addUser);
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
        System.out.println("会话成员有:");
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
    String voicePath = "D:\\videos\\1526636096.amr";
    String videoPath = "D:\\videos\\1526636096.mp4";

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

    //测试发送单人会话语音消息
    public void testSendSingleVoiceMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException, UnsupportedEncodingException {
        byte[] data = Helper.readFile(voicePath);
        sessionClient.sendSingleVoiceMsg(msgFrom,msgTo,data);
    }

    //测试发送单人会话视频消息
    public void testSendSingleVideoMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException, UnsupportedEncodingException {
        byte[] data = Helper.readFile(videoPath);
        sessionClient.sendSingleVideoMsg(msgFrom,msgTo,data);
    }

    //--------------------
    //测试发送多人会话文字消息
    public void testSendSessionTxtMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        sessionClient.sendSessionTextMsg(msgFrom,sessionId,text);
    }

    //测试发送多人会话图片消息
    public void testSendSessionImgMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        sessionClient.sendSessionImgMsgV1(msgFrom,sessionId,imgPath);
    }

    //测试发送多人会话文件消息
    public void testSendSessionFileMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        sessionClient.sendSessionFileMsgV1(msgFrom,sessionId,imgPath);
    }

    //测试发送多人会话语音消息
    public void testSendSessionVoiceMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        byte[] data = Helper.readFile(voicePath);
        sessionClient.sendSessionVoiceMsg(msgFrom,sessionId,data);
    }

    //测试发送多人会话视频消息
    public void testSendSessionVideoMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        byte[] data = Helper.readFile(videoPath);
        sessionClient.sendSessionVideoMsg(msgFrom,sessionId,data);
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

