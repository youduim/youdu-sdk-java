package im.youdu.sdk.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.encrypt.AESCrypto;
import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class SessionMsgClient {
    private int buin;
    private String host;
    private String appId;
    private String appAeskey;

    private AESCrypto crypto;
    private AppTokenClient tokenClient;

    private final static String MessageTypeText = "text";
    private final static String MessageTypeFile = "file";
    private final static String MessageTypeImage = "image";
    private final static String MessageTypeSms = "sms";
    private final static String MessageTypeLink = "link";
    private final static String MessageTypeExlink = "exlink";
    private final static String MessageTypeMpnews = "mpnews";
    private final static String MessageTypeSystem = "sysMsg";

    public SessionMsgClient(YDApp app) {
        this.buin = app.getBuin();
        this.host = app.getHost();
        this.appId = app.getAppId();
        this.appAeskey = app.getAppAesKey();
        this.crypto = new AESCrypto(appId, appAeskey);
        this.tokenClient = new AppTokenClient(buin,host,appId,appAeskey);
    }

    public SessionInfo CreateSession(SessionCreateBody body) throws AESCryptoException, ParamParserException, HttpRequestException {
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(body.toJsonString()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        JsonObject jsonRsp = Helper.postJson(this.uriCreateSession(), param.toString());
        String ssId = Helper.getString("sessionId", jsonRsp);
        String title = Helper.getString("title", jsonRsp);
        String owner = Helper.getString("owner", jsonRsp);
        String type = Helper.getString("type", jsonRsp);

        Long version = Helper.getLong("version", jsonRsp);
       Long lastMsgId = Helper.getLong("lastMsgId", jsonRsp);
       Long activeTime = Helper.getLong("activeTime", jsonRsp);

        JsonArray array = Helper.getArray("member",jsonRsp);
        List<String> mems = new ArrayList<>();
        if(null != array && array.size()>0){
            for(JsonElement e:array) {
                mems.add(e.getAsString());
            }
        }

        SessionInfo sess = new SessionInfo();
        sess.setActiveTime(activeTime);
        sess.setLastMsgId(lastMsgId);
        sess.setVersion(version);
        sess.setSessionId(ssId);
        sess.setTitle(title);
        sess.setOwner(owner);
        sess.setType(type);
        sess.setMember(mems);
        return  sess;
    }

    public void sendSingleTextMsg(String fromUser, String toUser, String content) throws AESCryptoException, ParamParserException, HttpRequestException {
        JsonObject obj = new JsonObject();
        obj.addProperty("sender", fromUser);
        obj.addProperty("receiver", toUser);
        obj.addProperty("msgType", MessageTypeText);
        TextBody text = new TextBody(content);
        obj.add("text", text.toJsonElement());
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriSendMsg(), param.toString());
    }

    private String uriSendMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_SESSION_SEND_MSG,this.tokenClient.getToken()) ;
    }

    private String uriCreateSession() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_SESSION_CREATE,this.tokenClient.getToken()) ;
    }
}
