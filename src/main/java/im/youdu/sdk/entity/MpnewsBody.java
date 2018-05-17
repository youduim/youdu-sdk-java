package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

import java.util.ArrayList;
import java.util.List;

// 图文消息体
public class MpnewsBody extends MessageBody {

    private List<MpnewsBodyCell> msgList;

    public MpnewsBody() {
        this.msgList = new ArrayList<MpnewsBodyCell>();
    }

    /**
     * @param msgList MpnewsBodyCell的列表
     */
    public MpnewsBody(List<MpnewsBodyCell> msgList) {
        this.msgList = msgList != null ? msgList : new ArrayList<MpnewsBodyCell>();
    }

    public void addCell(MpnewsBodyCell cell){
        this.msgList.add(cell);
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        JsonObject result = Helper.parseJson(json);
        return this.fromJsonElement(result);
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        if(!json.isJsonArray()){
            return this;
        }
        JsonArray jArray = json.getAsJsonArray();
        if(null == jArray){
            return this;
        }
        for(JsonElement e : jArray){
            if(!e.isJsonObject()){
                continue;
            }
            JsonObject obj = e.getAsJsonObject();
            String title = Helper.getString("title", obj);
            String digest = Helper.getString("digest", obj);
            String content = Helper.getString("content", obj);
            String mediaId = Helper.getString("media_id", obj);
            int showFront = Helper.getInt("showFront", obj);
            MpnewsBodyCell cell = new MpnewsBodyCell(title,mediaId,digest,content,showFront);
            this.msgList.add(cell);
        }
        return  this;
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public JsonElement toJsonElement() {
        JsonArray array = new JsonArray();
        for (MpnewsBodyCell cell : this.msgList) {
            array.add(cell.toJsonElement());
        }
        return array;
    }

    @Override
    public String toString() {
        return "MpnewsBody{" +
                "msgList=" + msgList +
                '}';
    }

    public List<MpnewsBodyCell> getMsgList() {
        return msgList;
    }
}
