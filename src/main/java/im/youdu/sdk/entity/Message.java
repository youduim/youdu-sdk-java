package im.youdu.sdk.entity;

import com.google.gson.JsonObject;

// 消息类
public class Message {

	private String toGid;
    private String toUser;
    private String toDept;
    private String toEmail;
    private String msgType;
    private MessageBody msgBody;
    
    public Message() {
    	
    }

    /**
     * @param toUser 发送目标用户，格式为"user1 | user2 | user3"
     * @param msgType 消息类型
     * @param msgBody 消息体
     */ 
    public Message(String toUser, String msgType, MessageBody msgBody) {
        this.toUser = toUser != null ? toUser : "";
        this.msgType = msgType != null ? msgType : "";
        this.msgBody = msgBody;
    }

    public Message(String toUser, String toDept, String msgType, MessageBody msgBody) {
        this.toUser = toUser != null ? toUser : "";
        this.toDept = toDept != null ? toDept:"";
        this.msgType = msgType != null ? msgType : "";
        this.msgBody = msgBody;
    }

    public Message(String toUser, String toDept, String toEmail, String msgType, MessageBody msgBody) {
        this.toUser = toUser != null ? toUser : "";
        this.toEmail = toEmail != null ? toEmail:"";
        this.msgType = msgType != null ? msgType : "";
        this.msgBody = msgBody;
    }

    public String toJson() {
        JsonObject json = new JsonObject();
        if(null != this.toGid) {
        	json.addProperty("toGid", this.toGid);
        }
        if(null != this.toUser){
            json.addProperty("toUser", this.toUser);
        }
        if(null != this.toDept){
            json.addProperty("toDept",this.toDept);
        }
        if(null != this.toEmail){
            json.addProperty("toEmail",this.toEmail);
        }
        json.addProperty("msgType", this.msgType);
        json.add(this.msgType, this.msgBody.toJsonElement());
        return json.toString();
    }

    @Override
    public String toString() {
        return "Message{" +
        		"toGid='" + toGid + '\'' +
                "toUser='" + toUser + '\'' +
                "toDept='" + toDept + '\'' +
                ", msgType='" + msgType + '\'' +
                ", msgBody=" + msgBody +
                '}';
    }
    
    public String getToGid() {
    	return toGid;
    }
    
    public void setToGid(String toGid) {
    	this.toGid = toGid;
    }

    public String getToUser() {
        return toUser;
    }
    
    public void setToUser(String toUser) {
    	this.toUser = toUser;
    }

    public String getToDept() {
        return toDept;
    }
    
    public void setToDept(String toDept) {
    	this.toDept = toDept;
    }

    public String getMsgType() {
        return msgType;
    }
    
    public void setMsgType(String msgType) {
    	this.msgType = msgType;
    }

    public MessageBody getMsgBody() {
        return msgBody;
    }
    
    public void setMsgBody(MessageBody msgBody) {
    	this.msgBody = msgBody;
    }
}
