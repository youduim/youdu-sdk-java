package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;

public class EmailBody extends MessageBody {
    private String action;
    private String subject;
    private String fromUser;
    private String fromEmail;
    private String link;
    private long timex;
    private int unreadCount;

    public EmailBody() {
    }

    public EmailBody(String action, String subject, String fromUser, String fromEmail,String link, long timex) {
        this.action = action;
        this.subject = subject;
        this.fromUser = fromUser;
        this.fromEmail = fromEmail;
        this.link = link;
        this.timex = timex;
    }

    public EmailBody(String action, int unreadCount, long timex){
        this.action = action;
        this.unreadCount = unreadCount;
        this.timex = timex;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public long getTimex() {
        return timex;
    }

    public void setTimex(long timex) {
        this.timex = timex;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        throw new IllegalAccessError("Not support");
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        throw new IllegalAccessError("Not support");
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("action", this.action);
        json.addProperty("time", this.timex);

        if(null != this.subject && this.subject.length()>0){
            json.addProperty("subject", this.subject);
        }
        if(null != this.fromUser && this.fromUser.length()>0){
            json.addProperty("fromUser", this.fromUser);
        }
        if(null != this.fromEmail && this.fromEmail.length()>0){
            json.addProperty("fromEmail", this.fromEmail);
        }
        if(null != this.link && this.link.length()>0){
            json.addProperty("link", this.link);
        }
        json.addProperty("unreadCount", this.unreadCount);
        return json;
    }
}
