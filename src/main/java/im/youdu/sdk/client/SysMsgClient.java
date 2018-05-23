package im.youdu.sdk.client;

import com.google.gson.JsonObject;
import im.youdu.sdk.encrypt.AESCrypto;
import im.youdu.sdk.entity.Message;
import im.youdu.sdk.entity.SysMsgBody;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.entity.YdApi;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

public class SysMsgClient {

    private int buin;
    private String host;
    private String appId;
    private String appAeskey;

    private AESCrypto crypto;
    private AppTokenClient tokenClient;

    private final static String MessageTypeSystem = "sysMsg";

    public SysMsgClient(YDApp app) {
        this.buin = app.getBuin();
        this.host = app.getHost();
        this.appId = app.getAppId();
        this.appAeskey = app.getAppAesKey();
        this.crypto = new AESCrypto(appId, appAeskey);
        this.tokenClient = new AppTokenClient(buin,host,appId,appAeskey);
    }

    /**
     * 发送系统消息
     *
     * @param toUser 接收消息的用户
     * @param toDept 接收消息的部门
     * @param sysMsg 系统消息对象
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendSysMsg(String toUser, String toDept, SysMsgBody sysMsg) throws ParamParserException, HttpRequestException, AESCryptoException {
        Message msg = new Message(toUser, toDept, MessageTypeSystem, sysMsg);
        String cipherText = this.crypto.encrypt(Helper.utf8Bytes(msg.toJson()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherText);
        Helper.postJson(uriSendMsg(), param.toString());
    }

    private String uriSendMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_APP_SEND_MSG,this.tokenClient.getToken()) ;
    }
}
