package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

// 文件消息体
public class AudioBody extends MessageBody {

    private String mediaId ="";
    private long size;

    public AudioBody() {}

    /**
     * @param mediaId 资源Id
     */
    public AudioBody(String mediaId) {
        this.mediaId = mediaId != null ? mediaId : "";
    }

    public AudioBody(String mediaId, long size) {
        this.size = size;
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
        return "AudioBody{" +
                "mediaId='" + mediaId + '\'' +
                '}';
    }

    public String getMediaId() {
        return mediaId;
    }

    public long getSize() {
        return size;
    }
}
