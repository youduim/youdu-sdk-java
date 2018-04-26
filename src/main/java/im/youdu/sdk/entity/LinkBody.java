package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;
import im.youdu.sdk.exception.ParamParserException;

// 文件消息体
public class LinkBody extends MessageBody {

    private String url;
    private String title;
    private Integer action;

    public LinkBody() {}

    /**
     * @param url 要打开的链接
     * @param title 标题
     * @param action 是否需要带上token
     */
    public LinkBody(String url, String title, Integer action) {
        this.url = url != null ? url : "";
        this.title = title != null ? title : "";
        this.action = action != null ? action : 0;
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        JsonObject result = Helper.parseJson(json);
        return this.fromJsonElement(result);
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        if (!json.isJsonObject()) {
            throw new ParamParserException("Json字段类型不匹配", null);
        }
        this.url = Helper.getString("url", json.getAsJsonObject());
        this.title = Helper.getString("title", json.getAsJsonObject());
        this.action = Helper.getInt("action", json.getAsJsonObject());
        return this;
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("url", this.url);
        json.addProperty("title", this.title);
        json.addProperty("action", this.action);
        return json;
    }

    @Override
    public String toString() {
        return "LinkBody{" +
                "url='" + url + '\'' +
                "title='" + title + '\'' +
                "action='" + action + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
}
