package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;

public class PopWindow extends MessageBody{
    private String url;
    private Integer duration;
    private Integer position;
    private Integer width;
    private Integer height;
    private String noticeId;



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("url", this.url);
        json.addProperty("duration", this.duration);
        json.addProperty("position", this.position);
        json.addProperty("width", this.width);
        json.addProperty("height", this.height);
        json.addProperty("notice_id", this.noticeId);
        return json;
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        return null;
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        return null;
    }
}
