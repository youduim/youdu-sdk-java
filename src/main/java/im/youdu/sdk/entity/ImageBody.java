package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;
import im.youdu.sdk.exception.ParamParserException;

// 图片消息体
public class ImageBody extends MessageBody {

    private String mediaId="";
    private String name = "";
    private long size;

    public ImageBody() {}

    /**
     * @param mediaId 资源Id
     */
    public ImageBody(String mediaId) {
        this.mediaId = mediaId != null ? mediaId : "";
    }

    public ImageBody(String name, String mediaId, long size) {
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
            throw new ParamParserException("json字段类型不匹配", null);
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
        return "ImageBody{" +
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
