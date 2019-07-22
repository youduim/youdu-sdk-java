package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

public class EventBody extends MessageBody {
    private String id;
    private String operation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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

        this.id = Helper.getString("appId", json.getAsJsonObject());
        this.operation = Helper.getString("operation", json.getAsJsonObject());
        return this;
    }

    @Override
    public String toJsonString() {
        return "Event{" +
                "id=" + id +
                ", operation=" + operation +
                '}';
    }

    @Override
    public JsonElement toJsonElement() {
        return null;
    }
}
