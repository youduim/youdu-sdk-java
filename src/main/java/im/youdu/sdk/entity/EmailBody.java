package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;

public class EmailBody extends MessageBody {
    private String action;
    private String title;
    private String sender;
    private long timex;
    private String digest;
    private int unread;

    public EmailBody() {
    }

    public EmailBody(String action, String title, String sender, long timex, String digest, int unread) {
        this.action = action;
        this.title = title;
        this.sender = sender;
        this.timex = timex;
        this.digest = digest;
        this.unread = unread;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTimex() {
        return timex;
    }

    public void setTimex(long timex) {
        this.timex = timex;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
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
        json.addProperty("title", this.title);
        json.addProperty("sender", this.sender);
        json.addProperty("timex", this.timex);
        json.addProperty("digest", this.digest);
        json.addProperty("unread", this.unread);
        return json;
    }
}
