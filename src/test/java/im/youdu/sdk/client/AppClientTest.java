package im.youdu.sdk.client;

import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import junit.framework.TestCase;

public class AppClientTest extends TestCase {
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_NAME = "音视频应用"; //应用名称
    private static final String APP_ID = "ydA7139AD03524491BAF899D14CBF6B6F3"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "ZrWkHDXfFjzF0fdTAxGgzZUpr7EnndTFuSfklHpMKZ8="; // 请填写企业应用的EncodingaesKey

    private AppClient appClient;

    public AppClientTest() throws Exception {
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, APP_NAME, APP_ID, "", APP_AESKEY);
        appClient = new AppClient(app);
    }

    //发送文本消息
    public void testSendTxtMsg() throws Exception {
        String receiveUsers = "cs1|cs2";
        String receiveDepts = "1|2|3";
        String text = "Hello, YD!!";
        appClient.sendTextMsg(receiveUsers,receiveDepts,text);
    }

    //发送图片消息
    public void testSendImgMsg() throws Exception {
        String receiveUsers = "test1|test2";
        String receiveDepts = "1|2|3";
        String imgName = "2018-01-01.jp";
        String imgPath = "D:\\pics\\2018\\2018-01-01.jpg";
        appClient.sendImageMsg(receiveUsers,receiveDepts,imgName,imgPath);
    }

    //发送文件消息
    public void testSendFileMsg() throws Exception{
        String receiveUsers = "test1|test2";
        String receiveDepts = "1|2|3";
        String fileName = "有度企业应用集成 .docx";
        String filePath = "D:\\docs\\有度企业应用集成 .docx";
        appClient.sendFileMsg(receiveUsers,receiveDepts,fileName,filePath);
    }

    //上传文件
    public void testUploadFile() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String fileName = "有度企业应用集成 .docx";
        String filePath = "D:\\docs\\有度企业应用集成 .docx";
        String mediaId = appClient.uploadFile(fileName,filePath);
        System.out.println("upload file success, mediaId: "+mediaId);
    }

    //上传图片
    public void testUploadImage() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String imgName = "2018-01-01.jp";
        String imgPath = "D:\\pics\\2018\\2018-01-01.jpg";
        String mediaId = appClient.uploadImage(imgName,imgPath);
        System.out.println("upload image success, mediaId: "+mediaId);
    }

    //下载图片并保存
    public void testDownloadAndSaveImg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String imgMediaId = "989475c35fc2a8565d9345796997ce16-251071";
        String saveTo = "D:\\work\\sdk\\java\\imges";
        FileInfo img = appClient.downloadImageAndSave(imgMediaId,saveTo);
        System.out.println(String.format("download img success, name: %s; size: %d, path: %s",img.getName(),img.size(), img.getPath()));
    }

    //下载文件并保存
    public void testDownloadAndSaveFile() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String fileMediaId = "20acef71f6d6b5b83e7ae8638057acd4-4593";
        String saveTo = "D:\\work\\sdk\\java\\files";
        FileInfo file = appClient.downloadFileAndSave(fileMediaId,saveTo);
        System.out.println(String.format("download file success, name: %s; size: %d, path: %s",file.getName(), file.size(), file.getPath()));
    }

    //发送隐式链接消息
    public void testSendLinkMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        String receiveUsers = "test1|test2";
        String receiveDepts = "1|2|3";
        LinkBody body = new LinkBody("https://youdu.cn", "你好，有度！！",0);
        appClient.sendLinkMsg(receiveUsers,receiveDepts,body);
    }

    //发送外链消息
    public void testSendExLinkMsg() throws ParamParserException, HttpRequestException, AESCryptoException, FileIOException {
        String imgName1 = "2018-01-01.jp";
        String imgPath1 = "D:\\pics\\2018\\2018-01-01.jpg";
        String imgName2 = "2018-01-02.jp";
        String imgPath2 = "D:\\pics\\2018\\2018-01-02.jpg";
        String receiveUsers = "test1|test2";
        String receiveDepts = "1|2|3";

        String mediaId1 = appClient.uploadImage(imgName1,imgPath1);
        String mediaId2 = appClient.uploadImage(imgName2,imgPath2);

        ExlinkBodyCell cell1 = new ExlinkBodyCell("有度","https://youdu.cn","有度官网", mediaId1);
        ExlinkBodyCell cell2 = new ExlinkBodyCell("有度下载","https://youdu.cn/download.html","有度下载", mediaId2);
        ExlinkBodyCell cell3 = new ExlinkBodyCell("有度帮助","https://youdu.cn/docs.html","有度帮助", "");
        ExlinkBody body = new ExlinkBody();
        body.addCell(cell1);
        body.addCell(cell2);
        body.addCell(cell3);
        appClient.sendExlinkMsg(receiveUsers,receiveDepts,body);
    }

    //发送图文消息
    public void testSendMpnewsMsg() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String imgName = "2018-01-01.jp";
        String imgPath = "D:\\pics\\2018\\2018-01-01.jpg";
        String receiveUsers = "test1|test2";
        String receiveDepts = "1|2|3";

        String mediaId = appClient.uploadImage(imgName,imgPath);
        MpnewsBodyCell cell1 = new MpnewsBodyCell("你好有度！", "有度", "工作需要张弛有度", mediaId,0);
        MpnewsBodyCell cell2 = new MpnewsBodyCell("你好有度！", "有度", "工作需要张弛有度", mediaId,1);
        MpnewsBody body = new MpnewsBody();
        body.addCell(cell1);
        body.addCell(cell2);
        appClient.sendMpnewsMsg(receiveUsers,receiveDepts,body);
    }

    //设置工作台应用角标
    //多次测试请修改count的值
    public void testSetAppNotice() throws ParamParserException, HttpRequestException, AESCryptoException {
        String account = "test1";
        String tip = "test";
        int count = 8;
        appClient.setAppNotice(account,count,tip);
    }

    //清理工作台应用角标
    public void testClearAppNotice() throws ParamParserException, HttpRequestException, AESCryptoException {
        String account = "test1";
        String tip = "test";
        int count = 0;
        appClient.setAppNotice(account,count,tip);
    }

    public void testSmsMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        appClient.sendSmsMsg("max.chen","","13111111111","123");
    }

    //测试弹窗
    public void testPopWindow() throws ParamParserException, HttpRequestException, AESCryptoException {
        String receiveUsers = "test1|test2";
        String receiveDepts = "1|2|3";
        PopWindowInfo win = new PopWindowInfo();
        win.setToUser(receiveUsers);
        win.setToDept(receiveDepts);
        win.setUrl("https://youdu.cn"); //访问URL
//        win.setTip("欢迎登录有度即时通"); //弹提示框
        win.setTitle("有度即时通");//弹窗标题
        win.setWidth(500);
        win.setHeight(500);
        win.setDuration(10); //单位：秒
//        win.setDuration(Const.Duration_Forever); //弹窗永不消失
        win.setPosition(Const.Position_BottomRight); //右下角弹出
        win.setNoticeId(APP_ID); //同样的noticeId，永远只有一个窗口，后面的覆盖掉前面的
//        win.setPopMode(1); //使用浏览器打开
        win.setPopMode(2); //使用窗口打开
        appClient.popWindow(win);
    }

    public void testGetFaceConf() throws ParamParserException, HttpRequestException, AESCryptoException{
        FaceConf conf = appClient.getFaceConfig();
        System.out.println(String.format("key %s, secret: %s", conf.getKey(), conf.getSecret()));
    }

    public void testPublishAudioVideoEvent(){
        String toGid = "100448|100397|100462|100422";
        String toUser = "cs1|cs2|cs3|cs4|cs5";
        String toDept = "1|2|3";
        String title = "测试音视频震铃弹窗";
        String content = "http://10.0.0.168:8080/notice_pc.html?token=$Token$";

        PopWindow popWindow = new PopWindow();
        popWindow.setTitle(title);
        popWindow.setPopMode(Const.Window_Pop_Mode_YDCefBrowser);
        popWindow.setPopSessionId(APP_ID);
        popWindow.setStayDuration(60);
        popWindow.setWidth(300);
        popWindow.setHeight(230);

        PopWindowEventDetail window = new PopWindowEventDetail();
        window.setWindowType(Const.Event_Content_Type_Url);
        window.setAction(Const.Window_Pop_Action_Start);
        window.setTips("test tips");
        window.setTimeStamp(System.currentTimeMillis());
        window.setPopWindow(popWindow);

        PopWindowEvent event = new PopWindowEvent();
        event.setEventType(Const.Event_Type_Popwindow);
        event.setToGid(toGid);
        event.setToDept(toDept);
        event.setToUser(toUser);
        event.setEventDetail(window);
        try {
            appClient.publishPopWindowEvent(event);
            System.out.println("test Event ok");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("test Event faild: "+e);
        }
    }
}
