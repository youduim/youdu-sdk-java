package im.youdu.sdk.entity;

public class SessionTextMsg {
    private String sessionId;
    private String receiver;
    private String sender;
    private String msgType;
    private String content;

    public SessionTextMsg(String sessionId, String receiver, String sender, String content) {
        this.sessionId = sessionId;
        this.receiver = receiver;
        this.sender = sender;
        this.content = content;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
