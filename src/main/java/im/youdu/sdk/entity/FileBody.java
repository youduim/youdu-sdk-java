package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;
import im.youdu.sdk.exception.ParamParserException;

// 文件消息体
public class FileBody extends MessageBody {

    private String mediaId = "";
    private String name = "";
    private long size;

    public FileBody() {}

    /**
     * @param mediaId 资源Id
     */
    public FileBody(String mediaId) {
        this.mediaId = mediaId != null ? mediaId : "";
    }

    public FileBody(String name, String mediaId, long size) {
        this.size = size;
        this.name = name != null ? name : "";
        this.mediaId = mediaId != null ? mediaId : "";
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
        JsonObject obj = json.getAsJsonObject();
        this.mediaId = Helper.getString("media_id", obj);
        this.name = Helper.getString("name", obj);
        this.size = Helper.getLong("size", obj);
        return this;
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("media_id", this.mediaId);
        return json;
    }

    @Override
    public String toString() {
        return "FileBody{" +
                "mediaId='" + mediaId + '\'' +
                '}';
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
}
