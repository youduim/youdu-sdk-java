package im.youdu.sdk.client;

import com.google.gson.JsonObject;
import im.youdu.sdk.encrypt.AESCrypto;
import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.*;
import im.youdu.sdk.util.Helper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.Date;

public class AppClient {
    private int buin;
    private String host;
    private String appId;
    private String appAeskey;

    private String token;
    private long tokenExpireTime;
    private AESCrypto crypto;
    private AppTokenClient tokenClient;

    private final static String MessageTypeText = "text";
    private final static String MessageTypeFile = "file";
    private final static String MessageTypeImage = "image";
    private final static String MessageTypeSms = "sms";
    private final static String MessageTypeLink = "link";
    private final static String MessageTypeExlink = "exlink";
    private final static String MessageTypeMpnews = "mpnews";

    public AppClient(YDApp app) {
        this.buin = app.getBuin();
        this.host = app.getHost();
        this.appId = app.getAppId();
        this.appAeskey = app.getAppAesKey();
        this.crypto = new AESCrypto(appId, appAeskey);
        this.tokenClient = new AppTokenClient(buin,host,appId,appAeskey);
    }

    /**
     * 上传图片
     *
     * @param name 图片名称
     * @param path 图片路径
     * @return mediaId 文件的资源ID
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws FileIOException 文件读写失败
     * @throws HttpRequestException http请求失败
     */
    public String uploadImage(String name, String path) throws ParamParserException, HttpRequestException, AESCryptoException, FileIOException {
        if(null == name || "".equals(name.trim())){
            name = Helper.readFileNameFromPath(path);
        }
        byte[] data = this.readMedia(path);
        return this.uploadMedia(YdApi.MEDIA_TYPE_IMG, name,data);
    }

    /**
     * 上传图片
     *
     * @param name 图片名称
     * @param data 图片数据
     * @return mediaId 文件的资源ID
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public String uploadImageWithBytes(String name, byte[] data) throws ParamParserException, HttpRequestException, AESCryptoException {
        if(null == name || "".equals(name.trim())){
            throw new ParamParserException("image name can't be null", null);
        }
        return this.uploadMedia(YdApi.MEDIA_TYPE_IMG, name,data);
    }

    /**
     * 上传文件
     *
     * @param name 文件名称
     * @param path 文件路径
     * @return mediaId 文件的资源ID
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws FileIOException 文件读写失败
     * @throws HttpRequestException http请求失败
     */
    public String uploadFile(String name, String path) throws ParamParserException, HttpRequestException, AESCryptoException, FileIOException {
        if(null == name || "".equals(name.trim())){
            name = Helper.readFileNameFromPath(path);
        }
        byte[] data = this.readMedia(path);
        return this.uploadMedia(YdApi.MEDIA_TYPE_FILE, name,data);
    }

    /**
     * 上传文件
     *
     * @param name 文件名称
     * @param data 文件数据
     * @return mediaId 文件的资源ID
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public String uploadFileWithBytes(String name, byte[] data) throws ParamParserException, HttpRequestException, AESCryptoException {
        if(null == name || "".equals(name.trim())){
            throw new ParamParserException("file name can't be null",null);
        }
        return this.uploadMedia(YdApi.MEDIA_TYPE_FILE, name,data);
    }

    //上传语音
    public String uploadVoice(String name, byte[] data) throws ParamParserException, HttpRequestException, AESCryptoException {
        if(null == name || "".equals(name.trim())){
            name="voice.dat";
        }
        return this.uploadMedia(YdApi.MEDIA_TYPE_VOICE, name,data);
    }

    //上传视频
    public String uploadVideo(String name, byte[] data) throws ParamParserException, HttpRequestException, AESCryptoException {
        if(null == name || "".equals(name.trim())){
            name="video.dat";
        }
        return this.uploadMedia(YdApi.MEDIA_TYPE_VIDEO, name,data);
    }

    /**
     * 下载文件并保存到指定目录
     *
     * @param mediaId 文件的资源ID
     * @param dir 保存的目录
     * @return FileInfo  文件信息，包括名称和原始数据、保存的路径
     * @throws AESCryptoException 加解密失败
     * @throws FileIOException 文件读写失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public FileInfo downloadFileAndSave(String mediaId, String dir) throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        FileInfo info = this.downloadMedia(mediaId);
        info.setPath(Helper.saveFile(info,dir));
        return info;
    }

    /**
     * 下载图片
     *
     * @param mediaId 图片的资源ID
     * @return FileInfo  图片信息，包括名称和原始数据、保存的路径
     * @throws AESCryptoException 加解密失败
     * @throws FileIOException 文件读写失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public FileInfo downloadImageAndSave(String mediaId, String dir) throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        FileInfo info = this.downloadMedia(mediaId);
        info.setPath(Helper.saveFile(info,dir));
        return info;
    }

    private byte[] readMedia(String path) throws FileIOException {
        FileInputStream file = null;
        try {
            file = new FileInputStream(path);
            byte[] buffer = Helper.readInputStream(file);
            return buffer;
        } catch (IOException e) {
            throw new FileIOException(e.getMessage(), e);
        } finally {
            Helper.close(file);
        }
    }

    private String uploadMedia(String mediaType, String mediaName, byte[] data) throws HttpRequestException, ParamParserException, AESCryptoException {
        String encryptFile = this.crypto.encrypt(data);
        JsonObject fileNameJson = new JsonObject();
        fileNameJson.addProperty("type", mediaType);
        fileNameJson.addProperty("name", mediaName);
        String cipherName = this.crypto.encrypt(Helper.utf8Bytes(fileNameJson.toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;
        try {
            HttpPost httpPost = new HttpPost(this.uriUploadMedia());
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("buin", String.format("%d", this.buin));
            builder.addTextBody("appId", this.appId);
            builder.addTextBody("encrypt", cipherName);
            builder.addBinaryBody("file", encryptFile.getBytes(), ContentType.TEXT_PLAIN, "file");
            httpPost.setEntity(builder.build());

            httpRsp = httpClient.execute(httpPost);
            Helper.parseHttpStatus(httpRsp);
            JsonObject jsonResult = Helper.readHttpResponse(httpRsp);
            Helper.parseApiStatus(jsonResult);
            String encryptResult = Helper.getString("encrypt", jsonResult);
            byte[] decryptBuffer = this.crypto.decrypt(encryptResult);
            String decryptResult = Helper.utf8String(decryptBuffer);
            jsonResult = Helper.parseJson(decryptResult);
            String mediaId = Helper.getString("mediaId", jsonResult);
            if (mediaId.isEmpty()) {
                throw new ParamParserException("mediaId 为空", null);
            }
            return mediaId;

        } catch (Exception e) {
            throw new HttpRequestException(0, e.getMessage(), e);
        } finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }

    private FileInfo downloadMedia(String mediaId) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        JsonObject mediaIdJson = new JsonObject();
        mediaIdJson.addProperty("mediaId", mediaId);
        String cipherId = this.crypto.encrypt(Helper.utf8Bytes(mediaIdJson.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherId);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;
        try {
            HttpPost httpPost = new HttpPost(this.uriDownloadMedia());
            StringEntity entity = new StringEntity(param.toString(), ContentType.APPLICATION_JSON);
            entity.setContentEncoding("UTF-8");
            httpPost.setEntity(entity);

            httpRsp = httpClient.execute(httpPost);
            Header header = httpRsp.getLastHeader("encrypt");
            if (header == null) {
                Helper.parseApiStatus(Helper.readHttpResponse(httpRsp));
                throw new ParamParserException("Header找不到加密内容字段", null);
            }
            String fileInfo = header.getValue();

            byte[] fileInfoBuffer = this.crypto.decrypt(fileInfo);
            String fileInfoResult = Helper.utf8String(fileInfoBuffer);
            JsonObject fileInfoJson = Helper.parseJson(fileInfoResult);
            String name = Helper.getString("name", fileInfoJson);
            long size = Helper.getLong("size", fileInfoJson);
            if (name.isEmpty() || size <= 0) {
                throw new ParamParserException("空文件", null);
            }
            byte[] fileData = this.decryptFileData(httpRsp.getEntity());
            return new FileInfo(name,mediaId,fileData);
        } catch (IOException e) {
            throw new HttpRequestException(0, e.getMessage(), e);
        } finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }

    private byte[] decryptFileData(HttpEntity rspEntity) throws FileIOException, ParamParserException, AESCryptoException {
            InputStream in = null;
            try {
                in = rspEntity.getContent();
                byte[] encryptBuffer = Helper.readInputStream(in);
                String encryptContent = Helper.utf8String(encryptBuffer);
                byte[] fileContent = this.crypto.decrypt(encryptContent);
                return fileContent;
            } catch (IOException e) {
                throw new FileIOException(e.getMessage(), e);
            } finally {
                Helper.close(in);
            }
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * 发送文本消息
     *
     * @param toUser 接收消息的用户
     * @param toDept 接收消息的部门
     * @param content 消息内容
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendTextMsg(String toUser, String toDept, String content) throws ParamParserException, AESCryptoException, HttpRequestException {
        TextBody body = new TextBody(content);
        Message msg = new Message(toUser,toDept, MessageTypeText, body);
        this.sendMsg(msg);
    }

    /***
     * 发送文件消息
     *
     * @param toUser 接收消息的用户
     * @param toDept 接收消息的部门
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @throws FileIOException 文件读写失败
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendFileMsg(String toUser, String toDept, String fileName, String filePath) throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String mediaId = uploadFile(fileName, filePath);
        FileBody body = new FileBody(mediaId);
        Message msg = new Message(toUser,toDept, MessageTypeFile, body);
        this.sendMsg(msg);
    }

    /***
     * 发送文件消息
     *
     * @param toUser 接收消息的用户
     * @param toDept 接收消息的部门
     * @param mediaId 文件资源ID
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendFileMsgWithMediaId(String toUser, String toDept, String mediaId) throws ParamParserException, HttpRequestException, AESCryptoException {
        FileBody body = new FileBody(mediaId);
        Message msg = new Message(toUser,toDept, MessageTypeFile, body);
        this.sendMsg(msg);
    }

    /***
     * 发送图片消息
     *
     * @param toUser 接收消息的用户
     * @param toDept 接收消息的部门
     * @param imageName 图片名称
     * @param imagePath 图片路径
     * @throws FileIOException 文件读写失败
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendImageMsg(String toUser, String toDept, String imageName, String imagePath) throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String mediaId = uploadImage(imageName, imagePath);
        ImageBody body = new ImageBody(mediaId);
        Message msg = new Message(toUser,toDept, MessageTypeImage, body);
        this.sendMsg(msg);
    }

    /***
     * 发送图片消息
     *
     * @param toUser 接收消息的用户
     * @param toDept 接收消息的部门
     * @param mediaId 图片资源ID
     * @throws FileIOException 文件读写失败
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendImageMsgWithMediaId(String toUser, String toDept, String mediaId) throws ParamParserException, HttpRequestException, AESCryptoException {
        ImageBody body = new ImageBody(mediaId);
        Message msg = new Message(toUser,toDept, MessageTypeImage, body);
        this.sendMsg(msg);
    }

    /***
     * 发送隐式链接消息
     *
     * @param toUser 接收消息的用户
     * @param toDept 接收消息的部门
     * @param link 隐式链接对象
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendLinkMsg(String toUser, String toDept, LinkBody link) throws ParamParserException, HttpRequestException, AESCryptoException {
        Message msg = new Message(toUser, toDept, MessageTypeLink, link);
        this.sendMsg(msg);
    }

    /***
     * 发送外链消息
     *
     * @param toUser 接收消息的用户
     * @param toDept 接收消息的部门
     * @param exLink 外链接对象
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendExlinkMsg(String toUser, String toDept, ExlinkBody exLink) throws ParamParserException, HttpRequestException, AESCryptoException {
        Message msg = new Message(toUser, toDept, MessageTypeExlink, exLink);
        this.sendMsg(msg);
    }

    /***
     * 发送外链消息
     *
     * @param toGid 接收消息的用户gid
     * @param toDept 接收消息的部门
     * @param exLink 外链接对象
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendExlinkMsgToYdGid(String toGid, String toDept, ExlinkBody exLink) throws ParamParserException, HttpRequestException, AESCryptoException {
        Message msg = new Message();
        msg.setToGid(toGid);
        msg.setToDept(toDept);
        msg.setMsgType(MessageTypeExlink);
        msg.setMsgBody(exLink);
        this.sendMsg(msg);
    }

    /***
     * 发送图文消息
     *
     * @param toUser 接收消息的用户
     * @param toDept 接收消息的部门
     * @param mpnews 图文对象
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendMpnewsMsg(String toUser, String toDept, MpnewsBody mpnews) throws ParamParserException, HttpRequestException, AESCryptoException {
        Message msg = new Message(toUser, toDept, MessageTypeMpnews, mpnews);
        this.sendMsg(msg);
    }

    /**
     * 发送短信消息
     *
     * @param toUser 接收消息的用户
     * @param toDept 接收消息的部门
     * @param from 系统消息对象
     * @param content 文件内容
     * @throws AESCryptoException 加解密失败
     * @throws ParamParserException 参数解析失败
     * @throws HttpRequestException http请求失败
     */
    public void sendSmsMsg(String toUser, String toDept, String from, String content) throws ParamParserException, HttpRequestException, AESCryptoException {
        SmsBody body = new SmsBody(from, content);
        Message msg = new Message(toUser, toDept, MessageTypeSms, body);
        this.sendMsg(msg);
    }

    private void sendMsg(Message msg) throws ParamParserException, AESCryptoException, HttpRequestException {
        String cipherText = this.crypto.encrypt(Helper.utf8Bytes(msg.toJson()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherText);
        Helper.postJson(uriSendMsg(), param.toString());
    }
//------------------------------------------------------------------------------------------------------------------

    /**
     * 设置应用通知（消息待办数）
     * @param account 账号
     * @param msgCount 待办数
     * @param tip 提示消息
     * @throws ParamParserException
     * @throws AESCryptoException
     * @throws HttpRequestException
     */
    public void setAppNotice(String account, int msgCount, String tip) throws ParamParserException, AESCryptoException, HttpRequestException {
        AppNoticeBody appNotice = new AppNoticeBody(account,msgCount,tip);
        this.setAppNotice(appNotice);
    }

    /**
     * 设置应用通知（消息待办数）
     * @param appNotice 初始化好了的应用通知
     * @throws ParamParserException
     * @throws AESCryptoException
     * @throws HttpRequestException
     */
    public void setAppNotice(AppNoticeBody appNotice) throws ParamParserException, AESCryptoException, HttpRequestException {
        String cipherText = this.crypto.encrypt(Helper.utf8Bytes(appNotice.toJsonString()));
        JsonObject param = new JsonObject();
        param.addProperty("app_id", this.appId);
        param.addProperty("msg_encrypt", cipherText);
        Helper.postJson(uriSetNotice(), param.toString());
    }

    public void popWindow(PopWindowInfo info) throws ParamParserException, AESCryptoException, HttpRequestException {
        JsonObject obj = new JsonObject();
        boolean toUserIsOK = !Helper.isEmpty(info.getToUser());
        boolean toDeptIsOK = !Helper.isEmpty(info.getToDept());
        if(!toUserIsOK && !toDeptIsOK){
            throw new ParamParserException("toUser and toDept can not be empty both",null);
        }
        if(toUserIsOK){
            obj.addProperty("toUser",info.getToUser());
        }
        if(toDeptIsOK){
            obj.addProperty("toDept",info.getToDept());
        }

        JsonObject popObj = new JsonObject();
        boolean urlIsOK = !Helper.isEmpty(info.getUrl());
        boolean tipIsOK = !Helper.isEmpty(info.getTip());
        if(!urlIsOK && !tipIsOK){
            throw new ParamParserException("url and tips can not be empty both",null);
        }
        if(urlIsOK){
            popObj.addProperty("url",info.getUrl());
        }
        if(tipIsOK){
            popObj.addProperty("tip",info.getTip());
        }
        if(!Helper.isEmpty(info.getTitle())){
            popObj.addProperty("title",info.getTitle());
        }
        if(null != info.getDuration()){
            popObj.addProperty("duration",info.getDuration());
        }
        if(null != info.getPosition()){
            popObj.addProperty("position",info.getPosition());
        }
        if(null != info.getWidth()){
            popObj.addProperty("width",info.getWidth());
        }
        if(null != info.getHeight()){
            popObj.addProperty("height",info.getHeight());
        }
        if(!Helper.isEmpty(info.getNoticeId())){
            popObj.addProperty("notice_id",info.getNoticeId());
        }
        if(null != info.getPopMode()){
            popObj.addProperty("pop_mode", info.getPopMode());
        }
        obj.add("popWindow",popObj);
        String cipherText = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));
        JsonObject param = new JsonObject();
        param.addProperty("app_id", this.appId);
        param.addProperty("msg_encrypt", cipherText);
        Helper.postJson(uriPopWindow(), param.toString());
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * 获取人脸识别的key，secret
     * @throws ParamParserException
     * @throws AESCryptoException
     * @throws HttpRequestException
     */
    public FaceConf getFaceConfig() throws ParamParserException, AESCryptoException, HttpRequestException {
        JsonObject jsonRsp = Helper.getUrlV2(this.uriGetFaceConf());
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject jsonObj = Helper.parseJson(new String(decryptRsp));
        FaceConf conf = new FaceConf();
        conf.setKey(Helper.getString("key", jsonObj));
        conf.setSecret(Helper.getString("secret", jsonObj));
        return conf;
    }

    public void publishPopWindowEvent(PopWindowEvent event) throws ParamParserException, AESCryptoException, HttpRequestException {
        String jsonStr = event.toJsonString();
        String cipherText = this.crypto.encrypt(Helper.utf8Bytes(jsonStr));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherText);
        Helper.postJson(uriPublishEvent(), param.toString());
    }

    //----------------------------------------------------------------------------------------------------------------------
    private String uriUploadMedia() throws ParamParserException, HttpRequestException, AESCryptoException {
        this.checkToken();
        return String.format("%s%s%s?accessToken=%s",YdApi.SCHEME,this.host,YdApi.API_UPLOAD_MEDIA,this.token) ;
    }

    private String uriDownloadMedia() throws ParamParserException, HttpRequestException, AESCryptoException {
        this.checkToken();
        return String.format("%s%s%s?accessToken=%s",YdApi.SCHEME,this.host,YdApi.API_DOWNLOAD_MEDIA,this.token) ;
    }

    private String uriSendMsg() throws ParamParserException, HttpRequestException, AESCryptoException {
        this.checkToken();
        return String.format("%s%s%s?accessToken=%s",YdApi.SCHEME,this.host,YdApi.API_APP_SEND_MSG,this.token) ;
    }

    private String uriGetToken(){
        return String.format("%s%s%s",YdApi.SCHEME , this.host, YdApi.API_GET_TOKEN);
    }

    private String uriSetNotice(){
        return String.format("%s%s%s",YdApi.SCHEME , this.host, YdApi.API_SET_NOTICE);
    }

    private String uriPopWindow(){
        return String.format("%s%s%s",YdApi.SCHEME , this.host, YdApi.API_POPWINDOW);
    }

    private String uriPublishEvent() throws ParamParserException, HttpRequestException, AESCryptoException {
        this.checkToken();
        return String.format("%s%s%s?accessToken=%s",YdApi.SCHEME , this.host, YdApi.API_EVENT_PUBLISH, this.token);
    }

    private void checkToken() throws AESCryptoException, ParamParserException, HttpRequestException {
        long now = new Date().getTime() / 1000;
        if (this.token != null && this.tokenExpireTime > now+5) { //预留5秒的时间
            return;
        }
        freshToken();
    }

    private String uriGetFaceConf() throws ParamParserException, HttpRequestException, AESCryptoException {
        this.checkToken();
        return String.format("%s%s%s?accessToken=%s",YdApi.SCHEME,this.host,YdApi.API_FACE_CONF_GET,this.token) ;
    }

    private void freshToken() throws AESCryptoException, ParamParserException, HttpRequestException {
        long now = new Date().getTime() / 1000;
        String timestamp = String.format("%d", now);
        String encryptTime = this.crypto.encrypt(timestamp.getBytes());
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", encryptTime);

        JsonObject jsonRsp = Helper.postJson(uriGetToken(), param.toString());
        String encrypt = Helper.getString("encrypt", jsonRsp);
        if (encrypt.isEmpty()) {
            throw new ParamParserException("找不到返回结果的加密字段", null);
        }
        byte[] rspBuffer = this.crypto.decrypt(encrypt);
        JsonObject jsonToken = Helper.parseJson(Helper.utf8String(rspBuffer));
        String token = Helper.getString("accessToken", jsonToken);
        int expire = Helper.getInt("expireIn", jsonToken);
        if (token.isEmpty() || expire <= 0) {
            throw new ParamParserException(String.format("返回结果不合法，token: %s, expire: %d\n", token, expire), null);
        }
        this.token = token;
        this.tokenExpireTime = now+expire;
    }

//----------------------------------------------------------------------------------------------------------------------

    public ReceiveMessage receive(InputStream in) throws GeneralEntAppException, IOException {
        byte[]  contentBuffer = Helper.readInputStream(in);
        Helper.close(in);

        String content = Helper.utf8String(contentBuffer);
        JsonObject reqJson = Helper.parseJson(content);
        int toBuin = Helper.getInt("toBuin", reqJson);
        String toApp = Helper.getString("toApp", reqJson);
        String encrypt = Helper.getString("encrypt", reqJson);
        if(toBuin != this.buin){
            String message = String.format("buin is unequal.local:%d; callcak: %d",this.buin,toBuin);
            throw new GeneralEntAppException(message, null);
        }
        if(toApp != this.appId){
            String message = String.format("buin is unequal.local:%d; callcak: %d",this.buin,toBuin);
            throw new GeneralEntAppException(message, null);
        }
        byte[] data = this.crypto.decrypt(encrypt);
        ReceiveMessage msg = new ReceiveMessage();
        msg.fromJson(Helper.utf8String(data));
        return msg;
    }

    public ReceiveMessage decrypt(String encrypt) throws GeneralEntAppException, IOException {
        byte[] data = this.crypto.decrypt(encrypt);
        ReceiveMessage msg = new ReceiveMessage();
        msg.fromJson(Helper.utf8String(data));
        return msg;
    }

    public ReceiveAuth decryptAuth(String encrypt) throws GeneralEntAppException, IOException {
        byte[] data = this.crypto.decrypt(encrypt);
        ReceiveAuth auth = new ReceiveAuth();
        auth.fromJson(Helper.utf8String(data));
        return auth;
    }

//----------------------------------------------------------------------------------------------------------------------
    public int getBuin() {
        return buin;
    }

    public void setBuin(int buin) {
        this.buin = buin;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppAeskey() {
        return appAeskey;
    }

    public void setAppAeskey(String appAeskey) {
        this.appAeskey = appAeskey;
    }

    public AESCrypto getCrypto() {
        return crypto;
    }

    public void setCrypto(AESCrypto crypto) {
        this.crypto = crypto;
    }

    public AppTokenClient getTokenClient() {
        return tokenClient;
    }

    public void setTokenClient(AppTokenClient tokenClient) {
        this.tokenClient = tokenClient;
    }
}
