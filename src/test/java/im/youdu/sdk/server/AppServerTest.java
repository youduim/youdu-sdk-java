package im.youdu.sdk.server;

import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.ServiceException;
import junit.framework.TestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AppServerTest extends TestCase {
    //MAX.CHEN
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String APP_NAME = "A应用"; //请填写企业应用名称
    private static final String APP_ID = "ydEF8652BEA32847E8ADB3C826875D7793"; // 请填写企业应用AppId
    private static final String APP_TOKEN = "u7upckpiy635znb5"; //请填写企业应用回调token
    private static final String APP_AESKEY = "9r4LpzwHbviiOQwGx16O+tNkZXVTYf2FC2fpkdJyyOU="; // 请填写企业应用EncodingAesKey
    private static final int SERVER_PORT = 8080;


    private AppServer server;
    public AppServerTest() {
        try {
            server = new AppServer(SERVER_PORT);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public void testStart() throws Exception {
        YdMsgDealFunc func = new YdMsgDealFunc();
        YDApp ydApp = new YDApp(BUIN, "", APP_NAME, APP_ID, APP_TOKEN, APP_AESKEY);
        this.server.setAppHandler("/ydapp/a/receive",ydApp, func);
        this.server.start();
        CountDownLatch cdl = new CountDownLatch(5);
        cdl.await(600, TimeUnit.SECONDS); //10分钟后服务退出
    }

//----------------------------------------------------------------------------------------------------------------------
    class YdMsgDealFunc implements IReceiveYdMsg {
        @Override
        public void receive(ReceiveMessage msg) {
            this.dealMsg(msg);
        }

        private void dealMsg(ReceiveMessage ydMsg){
            try {
                String msgType = ydMsg.getMsgType();
                switch (msgType) {
                    case Const.Message_App_Type_Event: {
                        EventBody event = ydMsg.getAsEvent();
                        System.out.println(String.format("receive app event, appId:%s, operation:%s ", event.getId(), event.getOperation() ));
                        break;
                    }
                    case Const.Message_App_Type_Sms: {
                        SmsBody smsMsg = ydMsg.getAsSmsMsg();
                        System.out.println(String.format("receive app sms msg: %s",smsMsg.getContent()));
                        break;
                    }
                    case Const.Message_Session_Type_Text: {
                        String text = ydMsg.getAsTextMsg();
                        System.out.println(String.format("receive text msg: %s", text));
                        break;
                    }
                    case Const.Message_Session_Type_File: {
                        FileBody appFile = ydMsg.getAsFileMsg();
                        System.out.println(String.format("receive file msg, mediaId:: %s, you can download it by AppClient::downloadFileAndSave", appFile.getMediaId()));
                        break;
                    }
                    case Const.Message_Session_Type_Image: {
                        ImageBody appImg = ydMsg.getAsImageMsg();
                        System.out.println(String.format("receive img msg mediaId: %s, you can download it by AppClient::downloadImageAndSave",appImg.getMediaId()));
                        break;
                    }
                    case Const.Message_Session_Create: {
                        SessionCreateBody sessionCreate = ydMsg.getAsSessionCreate();
                        System.out.println(String.format("receive session create event: %s",sessionCreate.toJsonString()));
                        break;
                    }
                    case Const.Message_Session_Update: {
                        SessionUpdateBody sessionUpdate = ydMsg.getAsSessionUpdate();
                        System.out.println(String.format("receive sesion update event: %s",sessionUpdate.toJsonString()));
                        break;
                    }
                    case Const.Message_Session_Type_Audio: {
                        AudioBody audio = ydMsg.getAsAudioMsg();
                        System.out.println(String.format("receive audio msg, mediaId:%s",audio.getMediaId()));
                        break;
                    }
                    case Const.Message_Session_Type_Complex: {
                        ComplexBody complexBody = ydMsg.getAsComplexMsg();
                        System.out.println(String.format("receive complex msg: %s",complexBody.getBody()));
                        break;
                    }
                    case Const.Message_Session_Type_System: {
                        RecvSystemMsgBody systemMsgBody = ydMsg.getAsSystemMsg();
                        System.out.println(String.format("receive system msg: %s",systemMsgBody.getMsg()));
                        break;
                    }
                    case Const.Message_Session_Type_Broadcast: {
                        RecvBroadcastBody broadcastBody = ydMsg.getAsBroadcastMsg();
                        System.out.println(String.format("receive broadcast msg: %s", broadcastBody.getMsg()));
                        break;
                    }
                    default: {
                        break;
                    }
                };
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
