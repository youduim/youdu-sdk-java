package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class SessionUpdateBody extends MessageBody {
    private String owner;
    private String title;
    private String opUser;
    private String sessionId;
    private List<String> addMember;
    private List<String> delMember;

    public SessionUpdateBody() {
        addMember = new ArrayList<String>();
        delMember = new ArrayList<String>();
    }

    public String checkForUpdate(){
        if(null == sessionId || "".equals(sessionId)){
            return "sessionId is null";
        }
        if(null == opUser || "".equals(opUser)){
            return "opUser is null";
        }
        return "";
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
        this.owner = Helper.getString("owner", obj);
        this.title = Helper.getString("title", obj);
        JsonArray addMemberArray = Helper.getArray("addMember",obj);
        if (null != addMemberArray) {
            for (JsonElement item : addMemberArray) {
                if (item.isJsonPrimitive()) {
                    this.addMember.add(item.getAsString());
                }
            }
        }
        JsonArray delMemberArray = Helper.getArray("delMember",obj);
        if (null != delMemberArray) {
            for (JsonElement item : delMemberArray) {
                if (item.isJsonPrimitive()) {
                    this.delMember.add(item.getAsString());
                }
            }
        }
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

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAddMember() {
        return addMember;
    }

    public List<String> getDelMember() {
        return delMember;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAddMember(List<String> addMember) {
        this.addMember = addMember;
    }

    public void setDelMember(List<String> delMember) {
        this.delMember = delMember;
    }
}

