package im.youdu.sdk.client;

import com.google.gson.JsonObject;
import im.youdu.sdk.encrypt.AESCrypto;
import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

public class MailMsgClient {
    private int buin;
    private String host;
    private String appId = "sysExmailAssistant";
    private String appAeskey;

    private String token;
    private long tokenExpireTime;
    private AESCrypto crypto;
    private AppTokenClient tokenClient;

    private final static String MessageTypeMail = "mail";

    public MailMsgClient(int buin, String host, String exmailAppAESKey) {
        this.buin = buin;
        this.host = host;
        this.appAeskey = exmailAppAESKey;
        this.crypto = new AESCrypto(appId, appAeskey);
        this.tokenClient = new AppTokenClient(buin,host,appId,appAeskey);
    }


    public void sendMailMsg(String toUser, String toEmail, EmailBody emailMsg) throws ParamParserException, HttpRequestException, AESCryptoException {
        Message msg = new Message(toUser, "",toEmail, MessageTypeMail, emailMsg);
        String cipherText = this.crypto.encrypt(Helper.utf8Bytes(msg.toJson()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherText);
        Helper.postJson(uriSendMsg(), param.toString());
    }

    private String uriSendMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_APP_SEND_MSG, this.tokenClient.getToken()) ;
    }
}
