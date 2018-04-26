package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;


public class MpnewsBodyCell extends MessageBody {

    private String title;

    private String mediaId;

    private String digest;

    private String content;

    private int showFront;

    /**
     * @param title 标题
     * @param mediaId 封面图片的Id
     * @param digest 摘要
     * @param content 正文
     */
    public MpnewsBodyCell(String title, String digest, String content, String mediaId, int showFront) {
        this.title = title != null ? title : "";
        this.mediaId = mediaId != null ? mediaId : "";
        this.digest = digest != null ? digest : "";
        this.content = content != null ? content : "";
        this.showFront = showFront;
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        throw new IllegalAccessError("Not Support");
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("title", this.title);
        json.addProperty("media_id", this.mediaId);
        json.addProperty("digest", this.digest);
        json.addProperty("content", this.content);
        json.addProperty("showFront", this.showFront);
        return json;
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        throw new IllegalAccessError("Not Support");
    }

    @Override
    public String toString() {
        return "MpnewsBodyCell{" +
                "title='" + title + '\'' +
                ", mediaId='" + mediaId + '\'' +
                ", digest='" + digest + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getDigest() {
        return digest;
    }

    public String getContent() {
        return content;
    }

    public int getShowFront() {
        return showFront;
    }
}
