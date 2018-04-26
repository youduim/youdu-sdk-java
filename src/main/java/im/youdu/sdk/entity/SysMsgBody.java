package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;
import im.youdu.sdk.exception.ParamParserException;

import java.util.*;

// 文件消息体
public class SysMsgBody extends MessageBody {
    private String title;
    private List<Map<String,MessageBody>> msg;

    private final static String MESSAGE_TEXT = "text";
    private final static String MESSAGE_LINK = "link";

    public SysMsgBody() {
        this.msg = new ArrayList<Map<String,MessageBody>>();
    }

    /**
     * @param title 标题
     */
    public SysMsgBody(String title) {
        this.title = title != null ? title : "";
        this.msg = new ArrayList<Map<String,MessageBody>>();
    }

    public SysMsgBody(String title,ArrayList<Map<String,MessageBody>>  msg) {
        this.title = title != null ? title : "";
        if (msg == null){
            msg = new ArrayList<Map<String,MessageBody>>();
        }
        this.msg = msg;
    }

    public void addTextBody(String txt){
        if(null == txt || "".equals(txt)){
            return;
        }
        TextBody body = new TextBody(txt);
        Map m = new HashMap<String,MessageBody>();
        m.put(MESSAGE_TEXT,body);
        this.msg.add(m);
    }

    public void addLinkBody(String url, String title, int action){
        if(null == title || "".equals(title)){
            return;
        }
        if(null == url || "".equals(url)){
            return;
        }
        LinkBody body = new LinkBody(url,title,action);
        Map m = new HashMap<String,MessageBody>();
        m.put(MESSAGE_LINK,body);
        this.msg.add(m);
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("title", this.title);
        JsonArray array = new JsonArray();
        for (Map map : this.msg) {
            Iterator<Map.Entry<String, MessageBody>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, MessageBody> entry = entries.next();
                JsonObject cellJson = new JsonObject();
                cellJson.add(entry.getKey(),entry.getValue().toJsonElement());
                array.add(cellJson);
            }
        }
        json.add("msg",array);
        return json;
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
        this.title = Helper.getString("title", json.getAsJsonObject());
        return this;
    }

    @Override
    public String toString() {
        return "SysMsgBody{" +
                "title='" + title + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
