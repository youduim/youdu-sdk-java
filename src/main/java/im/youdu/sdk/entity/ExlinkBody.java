package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

import java.util.ArrayList;
import java.util.List;

// 外链消息体
public class ExlinkBody extends MessageBody {

    private List<ExlinkBodyCell> msgList;

    public ExlinkBody() {
        this.msgList = new ArrayList<ExlinkBodyCell>();
    }

    public ExlinkBody(List<ExlinkBodyCell> msgList) {
        this.msgList = msgList != null ? msgList : new ArrayList<ExlinkBodyCell>();
    }

    public void addCell(ExlinkBodyCell cell){
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
            String url = Helper.getString("url", obj);
            String title = Helper.getString("title", obj);
            String digest = Helper.getString("digest", obj);
            String mediaId = Helper.getString("media_id", obj);
            ExlinkBodyCell cell = new ExlinkBodyCell(title,url,digest,mediaId);
            this.msgList.add(cell);
        }
        return this;
    }

    @Override
    public String toJsonString() {
        if(null == msgList){
            return  "";
        }
        return this.toJsonElement().toString();
    }

    @Override
    public JsonElement toJsonElement() {
        JsonArray array = new JsonArray();
        for (ExlinkBodyCell cell : this.msgList) {
            array.add(cell.toJsonElement());
        }
        return array;
    }

    @Override
    public String toString() {
        return "ExlinkBody{" +
                "msgList=" + msgList +
                '}';
    }

    public List<ExlinkBodyCell> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<ExlinkBodyCell> msgList) {
        this.msgList = msgList;
    }
}
