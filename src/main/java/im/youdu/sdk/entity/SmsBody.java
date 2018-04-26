package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;
import im.youdu.sdk.exception.ParamParserException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jin on 2017/8/19.
 */
public class SmsBody extends MessageBody {

    private List<String> mobileList;
    private String from;
    private String content;

    public SmsBody() {
        this.mobileList = new ArrayList<String>();
    }

    /**
     * @param from 回府短信的发送者手机号
     * @param content 短信内容
     */
    public SmsBody(String from, String content) {
        this.mobileList = new ArrayList<String>();
        this.from = from != null ? from : "";
        this.content = content != null ? content : "";
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        JsonObject result = Helper.parseJson(json);
        return this.fromJsonElement(result);
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        if (!json.isJsonObject()) {
            throw new ParamParserException("json字段类型不匹配", null);
        }
        JsonArray mobileArray = Helper.getArray("mobiles", json.getAsJsonObject());
        if (mobileArray != null) {
            for (JsonElement mobile : mobileArray) {
                if (mobile.isJsonPrimitive()) {
                    this.mobileList.add(mobile.getAsString());
                }
            }
        }
        this.content = Helper.getString("content", json.getAsJsonObject());
        return this;
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("from", this.from);
        json.addProperty("content", this.content);
        return json;
    }

    @Override
    public String toString() {
        return "SmsBody{" +
                "mobiles='" + mobileList + '\'' +
                ", from='" + from + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public List<String> getMobileList() {
        return  mobileList;
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
    }
}
