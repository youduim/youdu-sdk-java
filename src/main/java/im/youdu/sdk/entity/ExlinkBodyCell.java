package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;


public class ExlinkBodyCell extends MessageBody {

    private String title;

    private String url;

    private String digest;

    private String mediaId;

    /**
     * @param title 标题
     * @param url 链接
     * @param digest 摘要
     * @param mediaId 封面图片的Id
     */
    public ExlinkBodyCell(String title, String url, String digest, String mediaId) {
        this.title = title != null ? title : "";
        this.url = url != null ? url : "";
        this.digest = digest != null ? digest : "";
        this.mediaId = mediaId != null? mediaId : "";
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        throw new IllegalAccessError("Not Support");
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        throw new IllegalAccessError("Not Support");
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("title", this.title);
        json.addProperty("url", this.url);
        json.addProperty("digest", this.digest);
        json.addProperty("media_id", this.mediaId);
        return json;
    }

    @Override
    public String toString() {
        return "ExlinkBody{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", digest='" + digest + '\'' +
                ", mediaId='" + mediaId + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDigest() {
        return digest;
    }

    public String getMediaId() {
        return mediaId;
    }
}
