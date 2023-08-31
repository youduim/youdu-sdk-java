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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SessionClient {
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
    private final static String MessageTypeVoice = "voice";
    private final static String MessageTypeVideo = "video";

    public SessionClient(YDApp app) {
        this.app = app;
        this.buin = app.getBuin();
        this.host = app.getHost();
        this.appId = app.getAppId();
        this.appAeskey = app.getAppAesKey();
        this.crypto = new AESCrypto(appId, appAeskey);
        this.tokenClient = new AppTokenClient(buin, host, appId, appAeskey);
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
        if (null == sessionId || "".equals(sessionId.trim())) {
            throw new ParamParserException("sessionId is null", null);
        }

        JsonObject jsonRsp = Helper.getUrlV2(uriGetSession(sessionId));
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject rspBody = Helper.parseJson(new String(decryptRsp));
        return readSessionFromJson(rspBody);
    }

    //修改会话标题
    public SessionInfo updateSessionTitle(String sessionId, String opUser, String title) throws ParamParserException, AESCryptoException, HttpRequestException {
        if (null == sessionId || "".equals(sessionId.trim())) {
            throw new ParamParserException("title is null", null);
        }
        if (null == opUser || "".equals(opUser.trim())) {
            throw new ParamParserException("opUser is null", null);
        }
        if (null == title || "".equals(title.trim())) {
            throw new ParamParserException("title is null", null);
        }
        SessionUpdateBody body = new SessionUpdateBody();
        body.setSessionId(sessionId);
        body.setOpUser(opUser);
        body.setTitle(title);
        return updateSession(body);
    }

    //修改会话
    public SessionInfo updateSession(SessionUpdateBody body) throws ParamParserException, AESCryptoException, HttpRequestException {
        String msg = body.checkForUpdate();
        if (!"".equals(msg)) {
            throw new ParamParserException(msg, null);
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("sessionId", body.getSessionId());
        obj.addProperty("opUser", body.getOpUser());
        if (null != body.getTitle() && !"".equals(body.getTitle().trim())) {
            obj.addProperty("title", body.getTitle());
        }
        List<String> addMembers = body.getAddMember();
        if (null != addMembers && addMembers.size() > 0) {
            JsonArray array = new JsonArray();
            for (String mem : addMembers) {
                array.add(mem);
            }
            obj.add("addMember", array);
        }
        List<String> delMembers = body.getDelMember();
        if (null != delMembers && delMembers.size() > 0) {
            JsonArray array = new JsonArray();
            for (String mem : delMembers) {
                array.add(mem);
            }
            obj.add("delMember", array);
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

    private SessionInfo readSessionFromJson(JsonObject rspBody) {
        SessionInfo sess = new SessionInfo();
        sess.setSessionId(Helper.getString("sessionId", rspBody));
        sess.setTitle(Helper.getString("title", rspBody));
        sess.setOwner(Helper.getString("owner", rspBody));
        sess.setVersion(Helper.getLong("version", rspBody));
        sess.setType(Helper.getString("type", rspBody));
        JsonArray array = Helper.getArray("member", rspBody);
        if (null == array || array.size() == 0) {
            return sess;
        }

        List<String> mems = new ArrayList<String>();
        for (JsonElement e : array) {
            mems.add(e.getAsString());
        }
        sess.setMember(mems);
        return sess;
    }

    //----------------------------------------------------------------------------------------------------------------------
    private String uriCreateSession() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME, this.host, YdApi.API_SESSION_CREATE, this.tokenClient.getToken());
    }

    private String uriGetSession(String sessionId) throws ParamParserException, HttpRequestException, AESCryptoException, UnsupportedEncodingException {
        return String.format("%s%s%s?accessToken=%s&sessionId=%s", YdApi.SCHEME, this.host, YdApi.API_SESSION_GET, this.tokenClient.getToken(), URLEncoder.encode(sessionId, "utf-8"));
    }

    private String uriUpdateSession() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME, this.host, YdApi.API_SESSION_UPDATE, this.tokenClient.getToken());
    }

    //----------------------------------------------------------------------------------------------------------------------
    //发送单人会话文字消息
    public void sendSingleTextMsg(String fromUser, String toUser, String content) throws AESCryptoException, ParamParserException, HttpRequestException {
        TextBody text = new TextBody(content);
        sendSingleMsg(fromUser, toUser, MessageTypeText, text);
    }

    //发送单人会话图片消息
    public void sendSingleImgMsg(String fromUser, String toUser, String imgPath) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if (null == appClient) {
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String imgId = appClient.uploadImage("", imgPath);
        sendSingleImgMsgWithMediaId(fromUser, toUser, imgId);
    }

    //发送单人会话图片消息
    public void sendSingleImgMsgWithMediaId(String fromUser, String toUser, String imgId) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        ImageBody img = new ImageBody(imgId);
        sendSingleMsg(fromUser, toUser, MessageTypeImage, img);
    }

    //发送单人会话文件消息
    public void sendSingleFileMsg(String fromUser, String toUser, String filePath) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if (null == appClient) {
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String fileId = appClient.uploadFile("", filePath);
        sendSingleFileMsgWithFileId(fromUser, toUser, fileId);
    }

    //发送单人会话文件消息
    public void sendSingleFileMsgWithFileId(String fromUser, String toUser, String fileId) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        FileBody f = new FileBody(fileId);
        sendSingleMsg(fromUser, toUser, MessageTypeFile, f);
    }

    //发送单人会话语音消息
    public void sendSingleVoiceMsg(String fromUser, String toUser, byte[] voiceData) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if (null == appClient) {
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String id = appClient.uploadVoice("voice.dat", voiceData);
        AudioBody audio = new AudioBody(id);
        sendSingleMsg(fromUser, toUser, MessageTypeVoice, audio);
    }

    //发送单人会话视频消息
    public void sendSingleVideoMsg(String fromUser, String toUser, byte[] videoData) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if (null == appClient) {
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String id = appClient.uploadVideo("video.dat", videoData);
        VideoBody video = new VideoBody(id);
        sendSingleMsg(fromUser, toUser, MessageTypeVideo, video);
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
        sendSessionMsg(fromUser, sessionId, MessageTypeText, text);
    }

    //发送多人会话图片消息
    public void sendSessionImgMsg(String fromUser, String sessionId, String imgPath) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if (null == appClient) {
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String imgId = appClient.uploadImage("", imgPath);
        sendSessionImgMsgWithImgId(fromUser, sessionId, imgId);
    }

    //发送多人会话图片消息
    public void sendSessionImgMsgWithImgId(String fromUser, String sessionId, String imgId) throws AESCryptoException, ParamParserException, HttpRequestException {
        ImageBody img = new ImageBody(imgId);
        sendSessionMsg(fromUser, sessionId, MessageTypeImage, img);
    }

    //发送多人会话文件消息
    public void sendSessionFileMsg(String fromUser, String sessionId, String filePath) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if (null == appClient) {
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String fileId = appClient.uploadFile("", filePath);
        sendSessionFileMsgWithFileId(fromUser, sessionId, fileId);
    }

    //发送多人会话文件消息
    public void sendSessionFileMsgWithFileId(String fromUser, String sessionId, String fileId) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if (null == appClient) {
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        FileBody f = new FileBody(fileId);
        sendSessionMsg(fromUser, sessionId, MessageTypeFile, f);
    }

    //发送多人会话语音消息
    public void sendSessionVoiceMsg(String fromUser, String sessionId, byte[] voiceData) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if (null == appClient) {
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String id = appClient.uploadFileWithBytes("voice.dat", voiceData);
        AudioBody audio = new AudioBody(id);
        sendSessionMsg(fromUser, sessionId, MessageTypeVoice, audio);
    }

    //发送多人会话视频消息
    public void sendSessionVideoMsg(String fromUser, String sessionId, byte[] videoData) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        if (null == appClient) {
            appClient = new AppClient(app);
            appClient.setTokenClient(tokenClient);
        }
        String id = appClient.uploadFileWithBytes("video.dat", videoData);
        VideoBody video = new VideoBody(id);
        sendSessionMsg(fromUser, sessionId, MessageTypeVideo, video);
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
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME, this.host, YdApi.API_SESSION_SEND_MSG, this.tokenClient.getToken());
    }

    /**
     * 下载会话消息的zip文件并保存到dir目录
     *
     * @param fileId
     *         zip文件id
     * @param dir
     *         保存目录
     * @return
     * @throws IOException
     * @throws HttpRequestException
     * @throws FileIOException
     * @throws AESCryptoException
     * @throws ParamParserException
     */
    public FileInfo downloadMsgZipFileAndSave(String fileId, String dir) throws IOException, HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        byte[] bytes = this.downloadMsgZipFile(fileId);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setData(bytes);
        fileInfo.setName(fileId + ".zip");
        fileInfo.setMediaId(fileId);
        fileInfo.setPath(Helper.saveFile(fileInfo, dir));
        return fileInfo;
    }

    /**
     * 下载会话消息的zip文件
     *
     * @param fileId
     *         zip文件Id
     * @return
     * @throws HttpRequestException
     * @throws FileIOException
     * @throws AESCryptoException
     * @throws ParamParserException
     */
    public byte[] downloadMsgZipFile(String fileId) throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {

        JsonObject mediaIdJson = new JsonObject();
        mediaIdJson.addProperty("file_id", fileId);
        String cipherId = this.crypto.encrypt(Helper.utf8Bytes(mediaIdJson.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherId);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;

        String url = String.format("%s%s%s?accessToken=%s", YdApi.SCHEME, this.host, YdApi.API_SESSION_MSG_DOWNLOAD_ZIP, this.tokenClient.getToken());

        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(param.toString(), ContentType.APPLICATION_JSON);
            entity.setContentEncoding("UTF-8");
            httpPost.setEntity(entity);

            httpRsp = httpClient.execute(httpPost);
            InputStream inputStream = httpRsp.getEntity().getContent();

            byte[] encryptBuffer = Helper.readInputStream(inputStream);
            String encryptContent = Helper.utf8String(encryptBuffer);
            return this.crypto.decrypt(encryptContent);
        } catch (IOException e) {
            throw new HttpRequestException(0, e.getMessage(), e);
        } finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }

}
