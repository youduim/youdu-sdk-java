package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class SessionCreateBody extends MessageBody {
    private String type;
    private String title;
    private String creator;
    private List<String> member;

    public SessionCreateBody() {
        this.member = new ArrayList<String>();
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        JsonObject result = Helper.parseJson(json);
        return this.fromJsonElement(result);
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        if (!json.isJsonObject()) {
            throw new ParamParserException(Const.Error_Not_Json, null);
        }
        JsonObject obj = json.getAsJsonObject();
        this.type = Helper.getString("type", obj);
        this.title = Helper.getString("title", obj);
        JsonArray memberArray = Helper.getArray("member",obj);
        if (null != memberArray) {
            for (JsonElement item : memberArray) {
                if (item.isJsonPrimitive()) {
                    this.member.add(item.getAsString());
                }
            }
        }
        return this;
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("title", this.title);
        json.addProperty("creator", this.creator);
        json.addProperty("type", this.type);
        if(null != member && member.size()>0){
            JsonArray array = new JsonArray();
            for (String str : this.member) {
                array.add(str);
            }
            json.add("member",array);
        }
        return json;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getMember() {
        return member;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setMember(List<String> member) {
        this.member = member;
    }
}
