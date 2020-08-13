package im.youdu.sdk.client;

import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SessionClientTest extends TestCase {
    private static final int BUIN = 14797363; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "yd901B3FD68E2048D8822238A3D5888878"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "lZ/BHIk167HMScKEAWMIwPR4ivqJltn+YgMGvyf8BMc="; // 请填写企业应用的EncodingaesKey

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
    //测试发送单人会话文字消息
    public void testSendSingleTxtMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        String sender = "test1";
        String receiver = "test2";
        String text = "有度即时通";
        sessionClient.sendSingleTextMsg(sender,receiver,text);
    }

    //测试发送单人会话图片消息
    public void testSendSingleImgMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String sender = "test1";
        String receiver = "test2";
        String imgPath = "D:\\pics\\2018\\2018-01-05.jpg";
        sessionClient.sendSingleImgMsg(sender,receiver,imgPath);
    }

    //测试发送单人会话文件消息
    public void testSendSingleFileMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String sender = "test1";
        String receiver = "test2";
        String filePath = "D:\\files\\有度即时通.docx";
        sessionClient.sendSingleFileMsg(sender,receiver,filePath);
    }

    //测试发送单人会话语音消息
    public void testSendSingleVoiceMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException, UnsupportedEncodingException {
        String sender = "test1";
        String receiver = "test2";
        String voicePath = "D:\\videos\\1526636096.amr";
        byte[] data = Helper.readFile(voicePath);
        sessionClient.sendSingleVoiceMsg(sender,receiver,data);
    }

    //测试发送单人会话视频消息
    public void testSendSingleVideoMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException, UnsupportedEncodingException {
        String sender = "test1";
        String receiver = "test2";
        String videoPath = "D:\\videos\\1526636096.mp4";
        byte[] data = Helper.readFile(videoPath);
        sessionClient.sendSingleVideoMsg(sender,receiver,data);
    }

    //--------------------
    //测试发送多人会话文字消息
    public void testSendSessionTxtMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        String sender = "test1";
        String text = "有度即时通";
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        sessionClient.sendSessionTextMsg(sender,sessionId,text);
    }

    //测试发送多人会话图片消息
    public void testSendSessionImgMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String sender = "test1";
        String imgPath = "D:\\pics\\2018\\2018-01-05.jpg";
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        sessionClient.sendSessionImgMsg(sender,sessionId,imgPath);
    }

    //测试发送多人会话文件消息
    public void testSendSessionFileMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String sender = "test1";
        String filePath = "D:\\files\\有度即时通.docx";
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        sessionClient.sendSessionFileMsg(sender,sessionId,filePath);
    }

    //测试发送多人会话语音消息
    public void testSendSessionVoiceMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String sender = "test1";
        String voicePath = "D:\\videos\\1526636096.amr";
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        byte[] data = Helper.readFile(voicePath);
        sessionClient.sendSessionVoiceMsg(sender,sessionId,data);
    }

    //测试发送多人会话视频消息
    public void testSendSessionVideoMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String sender = "test1";
        String videoPath = "D:\\videos\\1526636096.mp4";
        String sessionId = "{8AD595F3-73B3-4B56-90AF-F336F9222FC5}";
        byte[] data = Helper.readFile(videoPath);
        sessionClient.sendSessionVideoMsg(sender,sessionId,data);
    }

    // 测试下载会话
    public void testDownloadZipFile() throws ParamParserException, HttpRequestException, AESCryptoException, IOException, FileIOException {
        String forwardId = "100028_0f27042568b8bf0b469f0033e5c63a5f-1008372_2020-08-13";
        String dir = "D:\\Test\\jsdk\\";

        FileInfo fileInfo = this.sessionClient.downloadMsgZipFileAndSave(forwardId, dir);
        System.out.println("save file to" + fileInfo.getPath());
    }
}

