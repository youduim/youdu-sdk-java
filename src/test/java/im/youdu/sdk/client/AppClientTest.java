package im.youdu.sdk.client;

import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import junit.framework.TestCase;

import java.util.ArrayList;

public class AppClientTest extends TestCase {
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_NAME = "A应用"; //应用名称
    private static final String APP_ID = "yd5C0995DBC736451D80763F9270A9A9E8"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "aempBarITDeMaqUg4ee+Wq2txhEG4q1NhWVoBfz1vzo="; // 请填写企业应用的EncodingaesKey

    private String toUser = "cs1|max.chen"; // 测试收收消息的账号
    private String toDept = "1|2|3"; // 测试收收消息的部门ID
    private  String testImg = "D:\\pics\\2018\\2018-01-01.jpg";
    private  String testImg2 = "D:\\pics\\2018\\2018-02-01.jpg";
    private  String testFile = "D:\\docs\\有度企业应用集成 .docx";

    private AppClient msgClient;

    public AppClientTest() throws Exception {
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, APP_NAME, APP_ID, "", APP_AESKEY);
        msgClient = new AppClient(app);
    }

    //发送文本消息
    public void testSendTxtMsg() throws Exception {
        msgClient.sendTextMsg(toUser,toDept,"Hello, YD!!");
    }

    //发送文本消息
    public void testSendTxtMsg2() throws Exception {
        String msg = "";
        for(int i=1;i<=1000;i++) {
            msg = String.format("第%d条测试消息", i);
            System.out.println(msg);
            msgClient.sendTextMsg("dong", "", msg);
        }
    }

    public void testBatchSentExtMsg(){
        String[] accs = {"cs1","cs2","cs3","cs4","cs5","cs6","cs7","cs8","cs9","cs10"};
        for(int i=1;i<=1000;i++) {
            String msg = String.format("第%d条测试消息", i);
            try {
                System.out.println(msg);
                for(String acc : accs){
                    msgClient.sendTextMsg(acc, "", msg);
                }
            } catch (ParamParserException e) {
                e.printStackTrace();
            } catch (AESCryptoException e) {
                e.printStackTrace();
            } catch (HttpRequestException e) {
                e.printStackTrace();
            }
        }
    }

    //发送图片消息
    public void testSendImgMsg() throws Exception {
        msgClient.sendImageMsg(toUser,toDept,"test.jpg",testImg);
    }

    //发送文件消息
    public void testSendFileMsg() throws Exception{
        msgClient.sendFileMsg(toUser,toDept,"abc.jpg",testFile);
    }

    //上传文件
    public void testUploadFile() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String mediaId = msgClient.uploadFile("",testFile);
        System.out.println("upload file: "+mediaId);
    }

    //上传图片
    public void testUploadImage() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String mediaId = msgClient.uploadImage("",testImg2);
        System.out.println("upload image: "+mediaId);
    }

    //下载图片
    public void testDownloadImg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String imgMediaId = "989475c35fc2a8565d9345796997ce16-251071";
        FileInfo img = msgClient.downloadImage(imgMediaId);
        System.out.println(String.format("name: %s; size: %d",img.getName(),img.size()));
    }

    //下载图片并保存
    public void testDownloadAndSaveImg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String imgMediaId = "989475c35fc2a8565d9345796997ce16-251071";
        String saveTo = "D:\\work\\sdk\\java\\test";
        FileInfo img = msgClient.downloadImageAndSave(imgMediaId,saveTo);
        System.out.println(String.format("name: %s; size: %d",img.getName(),img.size()));
    }

    //下载文件
    public void testDownloadFile() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String fileMediaId = "20acef71f6d6b5b83e7ae8638057acd4-4593";
        FileInfo file = msgClient.downloadFile(fileMediaId);
        System.out.println(String.format("name: %s; size: %d",file.getName(),file.size()));
    }

    //下载文件并保存
    public void testDownloadAndSaveFile() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String fileMediaId = "20acef71f6d6b5b83e7ae8638057acd4-4593";
        String saveTo = "D:\\work\\sdk\\java\\test";
        FileInfo file = msgClient.downloadFileAndSave(fileMediaId,saveTo);
        System.out.println(String.format("name: %s; size: %d",file.getName(),file.size()));
    }

    //发送隐式链接消息
    public void testSendLinkMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        LinkBody body = new LinkBody("https://youdu.im", "你好，有度！！",0);
        msgClient.sendLinkMsg(toUser,toDept,body);
    }

    //发送外链消息
    public void testSendExLinkMsg() throws ParamParserException, HttpRequestException, AESCryptoException, FileIOException {
        String mediaId1 = msgClient.uploadImage("",testImg);
        String mediaId2 = msgClient.uploadImage("",testImg2);
        System.out.println(mediaId1);
        System.out.println(mediaId2);
        ExlinkBodyCell cell1 = new ExlinkBodyCell("有度","https://youdu.im","有度官网", mediaId1);
        ExlinkBodyCell cell2 = new ExlinkBodyCell("有度下载","https://youdu.im/download.html","有度下载", mediaId2);
        ExlinkBodyCell cell3 = new ExlinkBodyCell("有度帮助","https://youdu.im/docs.html","有度帮助", mediaId1);
        ExlinkBody body = new ExlinkBody();
        body.addCell(cell1);
        body.addCell(cell2);
        body.addCell(cell3);
        msgClient.sendExlinkMsg(toUser,toDept,body);
    }

    //发送图文消息
    public void testSendMpnewsMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String mediaId = msgClient.uploadImage("",testImg);
        MpnewsBodyCell cell1 = new MpnewsBodyCell("你好有度！", "有度", "工作需要张弛有度", mediaId,0);
        MpnewsBodyCell cell2 = new MpnewsBodyCell("你好有度！", "有度", "工作需要张弛有度", mediaId,1);
        MpnewsBody body = new MpnewsBody();
        body.addCell(cell1);
        body.addCell(cell2);
        msgClient.sendMpnewsMsg(toUser,toDept,body);
    }

    //发送系统消息
    public void testSendSysMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        SysMsgBody sysMsg = new SysMsgBody();
        sysMsg.setTitle("有度即时通");
        sysMsg.addTextBody("欢迎使用有度即时通：");
        sysMsg.addLinkBody("https://www.youdu.im","有度官网",0);
        msgClient.sendSysMsg(toUser,toDept,sysMsg);
    }

    //发送应用红点数通知
    //多次测试请修改count的值
    public void testSetAppNotice1() throws ParamParserException, HttpRequestException, AESCryptoException {
        String account = "cs1";
        String tip = "test";
        int count = 8;
        msgClient.setAppNotice(account,count,tip);
    }

    //发送应用红点数通知
    //多次测试请修改count的值
    public void testSetAppNotice2() throws ParamParserException, HttpRequestException, AESCryptoException {
        String account = "cs1";
        String tip = "test";
        int count = 9;
        AppNoticeBody body = new AppNoticeBody(account,count,tip);
        msgClient.setAppNotice(body);
    }

    //发送应用红点数通知
    //多次测试请修改count的值
    public void testSetAppNotice3() throws ParamParserException, HttpRequestException, AESCryptoException {
        String account = "cs1";
        String tip = "test";
        int count = 10;
        PopWindow win = new PopWindow();
        win.setUrl("https://youdu.im");
        win.setHeight(500);
        win.setWidth(500);
        win.setDuration(10); //单位：秒
        AppNoticeBody body = new AppNoticeBody(account,count,tip, win);
        msgClient.setAppNotice(body);
    }

    //测试弹窗
    public void testPopWindow() throws ParamParserException, HttpRequestException, AESCryptoException {
        String toUser = "pom.huang";
        PopWindowInfo win = new PopWindowInfo();
        win.setToUser(toUser);
        win.setUrl("https://youdu.im"); //访问URL
        win.setTip("欢迎登录有度即时通"); //弹提示框
        win.setTitle("有度即时通");//弹窗标题
        win.setWidth(500);
        win.setHeight(500);
        win.setDuration(10); //单位：秒
//        win.setDuration(Const.Duration_Forever); //弹窗永不消失
        win.setPosition(Const.Position_BottomRight); //右下角弹出
        win.setNoticeId(APP_ID); //同样的noticeId，永远只有一个窗口，后面的覆盖掉前面的
        msgClient.popWindow(win);
    }
}
