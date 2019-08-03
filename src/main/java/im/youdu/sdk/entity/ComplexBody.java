package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplexBody extends MessageBody {
    private List<Map<String,MessageBody>> msg;

    public List<Map<String, MessageBody>> getBody() {
        return msg;
    }

    public void setBody(List<Map<String, MessageBody>> msg) {
        this.msg = msg;
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        JsonObject result = Helper.parseJson(json);
        return this.fromJsonElement(result);
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        if (!json.isJsonObject() && !json.isJsonArray()) {
            throw new ParamParserException("Json字段类型不匹配", null);
        }
        JsonArray jContentArray =  json.getAsJsonArray();
        this.msg = Helper.parseComplexMsgBody(jContentArray);
        return this;
    }

    @Override
    public String toJsonString() {
        return "";
    }

    @Override
    public JsonElement toJsonElement() {
        return null;
    }
}
