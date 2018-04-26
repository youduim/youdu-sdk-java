package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;
import im.youdu.sdk.exception.ParamParserException;


// 文字消息体
public class TextBody extends MessageBody {

    private String content;

    public TextBody() {
    }

    /**
     * @param content 消息内容
     */
    public TextBody(String content) {
        this.content = content != null ? content : "";
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        if (!json.isJsonObject()) {
            throw new ParamParserException("json字段类型不匹配", null);
        }
        this.content = Helper.getString("content", json.getAsJsonObject());
        return this;
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("content", this.content);
        return json;
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        JsonObject result = Helper.parseJson(json);
        return this.fromJsonElement(result);
    }

    @Override
    public String toString() {
        return "TextBody{" +
                "content='" + content + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }
}
