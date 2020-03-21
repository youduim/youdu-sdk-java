package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PopWindow{
    private String title;
    private Integer width;
    private Integer height;
    private String content;
    private Integer popMode;
    private Integer position;
    private String contentType;
    private String popSessionId;
    private Integer stayDuration;

    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("title", this.title);
        json.addProperty("width", this.width);
        json.addProperty("height", this.height);
        json.addProperty("content", this.content);
        json.addProperty("popMode", this.popMode);
        json.addProperty("position", this.position);
        json.addProperty("contentType", this.contentType);
        json.addProperty("stayDuration", this.stayDuration);
        json.addProperty("popSessionId", this.popSessionId);
        return json;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPopMode() {
        return popMode;
    }

    public void setPopMode(Integer popMode) {
        this.popMode = popMode;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPopSessionId() {
        return popSessionId;
    }

    public void setPopSessionId(String popSessionId) {
        this.popSessionId = popSessionId;
    }

    public Integer getStayDuration() {
        return stayDuration;
    }

    public void setStayDuration(Integer stayDuration) {
        this.stayDuration = stayDuration;
    }
}
