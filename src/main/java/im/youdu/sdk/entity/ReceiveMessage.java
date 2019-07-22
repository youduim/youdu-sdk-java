package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.GeneralEntAppException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

// 接收消息类
public class ReceiveMessage {
    private static final String MessageTypeText = "text";    //文本消息
    private static final String MessageTypeFile = "file";    //文件消息
    private static final String MessageTypeImage = "image";    //文件消息

    private long msgId;
    private long createTime;
    private long sessionVersion;

    private String sessionId = "";
    private String fromUser = "";
    private String receiver ="";
    private String packgeId = "";
    private String msgType = "";

    private MessageBody msgBody;

    public ReceiveMessage() {}

    public ReceiveMessage fromJson(String json) throws ParamParserException {
        JsonObject jObj = Helper.parseJson(json);
        String _msgType = Helper.getString("msgType", jObj);
        JsonElement jsonBody = jObj.get(_msgType);
        if (jsonBody == null) {
            throw new ParamParserException("找不到消息体", null);
        }

        this.msgId = Helper.getLong("msgId", jObj);
        this.createTime = Helper.getLong("createTime", jObj);
        this.sessionVersion = Helper.getLong("version",jObj);

        this.receiver = Helper.getString("receiver", jObj);
        this.fromUser = Helper.getString("fromUser", jObj);
        this.packgeId = Helper.getString("packageId", jObj);
        this.sessionId = Helper.getString("sessionId", jObj);
        this.msgType = this.parseMsgType(this.sessionId,this.receiver,_msgType);

        if (this.msgType.equals(Const.Message_Session_Type_Text) ) { //文本消息
            this.msgBody = new TextBody();
        } else if (this.msgType.equals(Const.Message_Session_Type_Image)) { //图片消息
            this.msgBody = new ImageBody();
        } else if (this.msgType.equals(Const.Message_Session_Type_File)) { //文件消息
            this.msgBody = new FileBody();
        } else if (this.msgType.equals(Const.Message_App_Type_Sms)) { //短信消息
            this.msgBody = new SmsBody();
        }else if (this.msgType.equals(Const.Message_Session_Type_Audio)) { //语音消息
            this.msgBody = new AudioBody();
        }else if (this.msgType.equals(Const.Message_Session_Type_System)) { //系统消息
            this.msgBody = new RecvSystemMsgBody();
        } else if (this.msgType.equals(Const.Message_Session_Type_Complex)) { //混合消息
            this.msgBody = new ComplexBody();
        } else if (this.msgType.equals(Const.Message_Session_Type_Broadcast)) { //广播消息
            this.msgBody = new RecvBroadcastBody();
        } else if (this.msgType.equals(Const.Message_Session_Create)){
            this.msgBody = new SessionCreateBody();
        } else if (this.msgType.equals(Const.Message_Session_Update)){
            this.msgBody = new SessionUpdateBody();
        } else if (this.msgType.equals(Const.Message_App_Type_Event)) {
            this.msgBody = new EventBody();
        } else {
            throw new ParamParserException(String.format("无法识别的消息类型 %s", this.msgType), null);
        }
        this.msgBody.fromJsonElement(jsonBody);
        return this;
    }

    @Override
    public String toString() {
        return "ReMessage{" +
                "fromUser='" + fromUser + '\'' +
                ", createTime=" + createTime +
                ", packgeId=" + packgeId +
                ", msgType='" + msgType + '\'' +
                ", msgBody=" + msgBody.toString() +
                '}';
    }

    /**
     * @return 发送消息的用户
     */
    public String getFromUser() {
        return fromUser;
    }

    /**
     * @return 消息的发送时间
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * @return packageId，需要返回给有度服务表示接收成功
     */
    public String getPackgeId() {
        return packgeId;
    }

    /**
     * @return 消息类型
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * @return 消息体
     */
    public MessageBody getMsgBody() {
        return msgBody;
    }

    //文本消息
    public String getAsTextMsg() throws GeneralEntAppException {
        if(!this.msgType.equals(Const.Message_Session_Type_Text)){
            throw new GeneralEntAppException("this is not a text msg",null);
        }
        TextBody body = this.msgBody.getAsTextBody();
        return body.getContent();
    }

    //图片消息
    public ImageBody getAsImageMsg() throws GeneralEntAppException {
        if(!this.msgType.equals(Const.Message_Session_Type_Image)){
            throw new GeneralEntAppException("this is not a image msg",null);
        }
        ImageBody body = this.msgBody.getAsImageBody();
        return body;
    }

    //文件消息
    public FileBody getAsFileMsg() throws GeneralEntAppException{
        if(!this.msgType.equals(Const.Message_Session_Type_File)){
            throw new GeneralEntAppException("this is not a file msg",null);
        }
        FileBody body = this.msgBody.getAsFileBody();
        return body;
    }

    //短信消息
    public SmsBody getAsSmsMsg() throws GeneralEntAppException {
        if(!this.msgType.equals(Const.Message_App_Type_Sms)){
            throw new GeneralEntAppException("this is not a sms msg",null);
        }
        SmsBody body = this.msgBody.getAsSmsBody();
        return body;
    }

    //会话创建事件
    public SessionCreateBody getAsSessionCreate() throws GeneralEntAppException {
        if(!this.msgType.equals(Const.Message_Session_Create)){
            throw new GeneralEntAppException("this is not a session create msg",null);
        }
        SessionCreateBody body = this.msgBody.getAsSessionCreateBody();
        return body;
    }

    //会话修改事件
    public SessionUpdateBody getAsSessionUpdate() throws GeneralEntAppException {
        if(!this.msgType.equals(Const.Message_Session_Update)){
            throw new GeneralEntAppException("this is not a session update msg",null);
        }
        SessionUpdateBody body = this.msgBody.getAsSessionUpdateBody();
        return body;
    }

    //语音消息
    public AudioBody getAsAudioMsg() throws GeneralEntAppException {
        if(!this.msgType.equals(Const.Message_Session_Type_Audio)){
            throw new GeneralEntAppException("this is not a audio msg",null);
        }
        AudioBody body = this.msgBody.getAsVoiceBody();
        return body;
    }

    //复合消息
    public ComplexBody getAsComplexMsg() throws GeneralEntAppException {
        if(!this.msgType.equals(Const.Message_Session_Type_Complex)){
            throw new GeneralEntAppException("this is not a complex msg",null);
        }
        ComplexBody body = this.msgBody.getAsComplexBody();
        return body;
    }

    //广播消息
    public RecvBroadcastBody getAsBroadcastMsg() throws GeneralEntAppException {
        if(!this.msgType.equals(Const.Message_Session_Type_Broadcast)){
            throw new GeneralEntAppException("this is not a broadcast msg",null);
        }
        RecvBroadcastBody body = this.msgBody.getAsRecvBroadcastBody();
        return body;
    }

    //系统消息
    public RecvSystemMsgBody getAsSystemMsg() throws GeneralEntAppException {
        if(!this.msgType.equals(Const.Message_Session_Type_System)){
            throw new GeneralEntAppException("this is not a system msg",null);
        }
        RecvSystemMsgBody body = this.msgBody.getAsRecvSysMsgBody();
        return body;
    }

    //打开应用会话事件
    public EventBody getAsEvent() throws GeneralEntAppException{
        if(!this.msgType.equals(Const.Message_App_Type_Event)){
            throw new GeneralEntAppException("this is not a event msg",null);
        }
        EventBody body = this.msgBody.getAsEventBody();
        return body;
    }

    //视频消息
//    public VideoBody getAsVideoMsg() throws GeneralEntAppException {
//        if(!this.msgType.equals(Const.Message_Session_Type_Video)){
//            throw new GeneralEntAppException("this is not a video msg",null);
//        }
//        VideoBody body = this.msgBody.getAsVideoBody();
//        return body;
//    }

    //外链消息
//    public ExlinkBody getAsExlinkMsg() throws GeneralEntAppException {
//        if(!this.msgType.equals(Const.Message_Session_Type_Exlink)){
//            throw new GeneralEntAppException("this is not an exlink msg",null);
//        }
//        ExlinkBody body = this.msgBody.getAsExlinkBody();
//        return body;
//    }

    //图文消息
//    public MpnewsBody getAsMpnewsMsg() throws GeneralEntAppException {
//        if(!this.msgType.equals(Const.Message_Session_Type_Mpnews)){
//            throw new GeneralEntAppException("this is not a mpnews msg",null);
//        }
//        MpnewsBody body = this.msgBody.getAsMpnewsBody();
//        return body;
//    }

    public String getSessionId() {
        return sessionId;
    }

    public String getReceiver() {
        return receiver;
    }

    public long getMsgId() {
        return msgId;
    }

    public long getSessionVersion() {
        return sessionVersion;
    }

    private String parseMsgType(String sessionId, String receiver, String msgType ){
        if(msgType == MessageTypeText) {
            msgType = Const.Message_Session_Type_Text;
        } else if(msgType == MessageTypeFile) {
            msgType = Const.Message_Session_Type_File;
        } else if(msgType == MessageTypeImage) {
            msgType = Const.Message_Session_Type_Image;
        }

        return msgType;
    }
}
