package im.youdu.sdk.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.encrypt.AESCrypto;
import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SessionMsgClient {
    private int buin;
    private String host;
    private String appId;
    private String appAeskey;
    private YDApp app;

    private AESCrypto crypto;
    private AppTokenClient tokenClient;
    private AppClient appClient;

    private final static String MessageTypeText = "text";
    private final static String MessageTypeFile = "file";
    private final static String MessageTypeImage = "image";
    private final static String MessageTypeSms = "sms";
    private final static String MessageTypeLink = "link";
    private final static String MessageTypeExlink = "exlink";
    private final static String MessageTypeMpnews = "mpnews";
    private final static String MessageTypeSystem = "sysMsg";

    public SessionMsgClient(YDApp app) {
        this.app = app;
        this.buin = app.getBuin();
        this.host = app.getHost();
        this.appId = app.getAppId();
        this.appAeskey = app.getAppAesKey();
        this.crypto = new AESCrypto(appId, appAeskey);
        this.tokenClient = new AppTokenClient(buin,host,appId,appAeskey);
    }

    //创建会话
    public SessionInfo createSession(SessionCreateBody body) throws AESCryptoException, ParamParserException, HttpRequestException {
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(body.toJsonString()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        JsonObject jsonRsp = Helper.postJson(this.uriCreateSession(), param.toString());
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject rspBody = Helper.parseJson(new String(decryptRsp));
        return readSessionFromJson(rspBody);
    }

    //获取会话
    public SessionInfo getSession(String sessionId) throws HttpRequestException, UnsupportedEncodingException, AESCryptoException, ParamParserException {
        if(null == sessionId || "".equals(sessionId.trim())){
            throw new ParamParserException("sessionId is null",null);
        }

        JsonObject jsonRsp = Helper.getUrlV2(uriGetSession(sessionId));
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject rspBody = Helper.parseJson(new String(decryptRsp));
        return readSessionFromJson(rspBody);
    }

    //修改会话
    public SessionInfo updateSession(SessionUpdateBody body) throws ParamParserException, AESCryptoException, HttpRequestException {
        String msg = body.checkForUpdate();
        if(!"".equals(msg)){
            throw new ParamParserException(msg,null);
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("sessionId", body.getSessionId());
        obj.addProperty("opUser", body.getOpUser());
        if(null != body.getTitle() && !"".equals(body.getTitle().trim())){
            obj.addProperty("title", body.getTitle());
        }
        List<String> addMembers = body.getAddMember();
        if(null != addMembers && addMembers.size()>0){
            JsonArray array = new JsonArray();
            for(String mem : addMembers){
                array.add(mem);
            }
            obj.add("addMember",array);
        }
        List<String> delMembers = body.getDelMember();
        if(null != delMembers && delMembers.size()>0){
            JsonArray array = new JsonArray();
            for(String mem : delMembers){
                array.add(mem);
            }
            obj.add("delMember",array);
        }
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        JsonObject jsonRsp = Helper.postJson(this.uriUpdateSession(), param.toString());
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject rspBody = Helper.parseJson(new String(decryptRsp));
        return readSessionFromJson(rspBody);
    }

    private SessionInfo readSessionFromJson(JsonObject rspBody){
        SessionInfo sess = new SessionInfo();
        sess.setSessionId(Helper.getString("sessionId",rspBody));
        sess.setTitle(Helper.getString("title",rspBody));
        sess.setOwner(Helper.getString("owner",rspBody));
        sess.setVersion(Helper.getLong("version",rspBody));
        sess.setType(Helper.getString("type",rspBody));
        JsonArray array = Helper.getArray("member", rspBody);
        if(null == array || array.size()==0){
            return  sess;
        }

        List<String> mems = new ArrayList<String>();
        for(JsonElement e:array){
            mems.add(e.getAsString());
        }
        sess.setMember(mems);
        return  sess;
    }
//----------------------------------------------------------------------------------------------------------------------
    private String uriCreateSession() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_SESSION_CREATE,this.tokenClient.getToken()) ;
    }

    private String uriGetSession(String sessionId) throws ParamParserException, HttpRequestException, AESCryptoException, UnsupportedEncodingException {
        return String.format("%s%s%s?accessToken=%s&sessionId=%s", YdApi.SCHEME,this.host,YdApi.API_SESSION_GET,this.tokenClient.getToken(), URLEncoder.encode(sessionId,"utf-8")) ;
    }

    private String uriUpdateSession() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_SESSION_UPDATE,this.tokenClient.getToken()) ;
    }
//----------------------------------------------------------------------------------------------------------------------
    //发送单人会话文字消息
    public void sendSingleTextMsg(String fromUser, String toUser, String content) throws AESCryptoException, ParamParserException, HttpRequestException {
        TextBody text = new TextBody(content);
        sendSingleMsg(fromUser,toUser,MessageTypeText,text);
    }

    //发送单人会话图片消息
    public void sendSingleImgMsgV1(String fromUser, String toUser, String imgPath) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if(null == appClient){
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String imgId = appClient.uploadImage("",imgPath);
        ImageBody img = new ImageBody(imgId);
        sendSingleMsg(fromUser,toUser,MessageTypeImage,img);
    }

    //发送单人会话图片消息
    public void sendSingleImgMsgV2(String fromUser, String toUser, String imgName, byte[] imgData) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if(null == appClient){
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String imgId = appClient.uploadImageWithBytes(imgName,imgData);
        ImageBody img = new ImageBody(imgId);
        sendSingleMsg(fromUser,toUser,MessageTypeImage,img);
    }

    //发送单人会话文件消息
    public void sendSingleFileMsgV1(String fromUser, String toUser, String filePath) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if(null == appClient){
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String mediaId = appClient.uploadFile("",filePath);
        FileBody f = new FileBody(mediaId);
        sendSingleMsg(fromUser,toUser,MessageTypeFile,f);
    }

    //发送单人会话文件消息
    public void sendSingleFileMsgV2(String fromUser, String toUser, String fileName, byte[] fileData) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if(null == appClient){
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String mediaId = appClient.uploadFileWithBytes(fileName,fileData);
        FileBody f = new FileBody(mediaId);
        sendSingleMsg(fromUser,toUser,MessageTypeFile,f);
    }

    private void sendSingleMsg(String fromUser, String toUser, String msgType, MessageBody body) throws ParamParserException, HttpRequestException, AESCryptoException {
        JsonObject obj = new JsonObject();
        obj.addProperty("sender", fromUser);
        obj.addProperty("receiver", toUser);
        obj.addProperty("msgType", msgType);
        obj.add(msgType, body.toJsonElement());
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriSendMsg(), param.toString());
    }

    //--------------------
    //发送多人会话图片消息
    public void sendSessionTextMsg(String fromUser, String sessionId, String content) throws ParamParserException, HttpRequestException, AESCryptoException {
        TextBody text = new TextBody(content);
        sendSessionMsg(fromUser,sessionId,MessageTypeText,text);
    }

    //发送多人会话图片消息
    public void sendSessionImgMsgV1(String fromUser, String sessionId, String imgPath) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if(null == appClient){
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String imgId = appClient.uploadImage("",imgPath);
        ImageBody img = new ImageBody(imgId);
        sendSessionMsg(fromUser,sessionId,MessageTypeImage,img);
    }

    //发送多人会话图片消息
    public void sendSessionImgMsgV2(String fromUser, String sessionId, String imgName, byte[] imgData) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if(null == appClient){
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String imgId = appClient.uploadImageWithBytes(imgName,imgData);
        ImageBody img = new ImageBody(imgId);
        sendSessionMsg(fromUser,sessionId,MessageTypeImage,img);
    }

    //发送多人会话文件消息
    public void sendSessionFileMsgV1(String fromUser, String sessionId, String filePath) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if(null == appClient){
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String mediaId = appClient.uploadFile("",filePath);
        FileBody f = new FileBody(mediaId);
        sendSessionMsg(fromUser,sessionId,MessageTypeFile,f);
    }

    //发送多人会话文件消息
    public void sendSessionFileMsgV2(String fromUser, String sessionId, String fileName, byte[] fileData) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if(null == appClient){
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String mediaId = appClient.uploadFileWithBytes(fileName,fileData);
        FileBody f = new FileBody(mediaId);
        sendSessionMsg(fromUser,sessionId,MessageTypeFile,f);
    }

    private void sendSessionMsg(String fromUser, String sessionId, String msgType, MessageBody body) throws ParamParserException, HttpRequestException, AESCryptoException {
        JsonObject obj = new JsonObject();
        obj.addProperty("sender", fromUser);
        obj.addProperty("sessionId", sessionId);
        obj.addProperty("msgType", msgType);
        obj.add(msgType, body.toJsonElement());
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriSendMsg(), param.toString());
    }

//----------------------------------------------------------------------------------------------------------------------
    private String uriSendMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_SESSION_SEND_MSG,this.tokenClient.getToken()) ;
    }

}
