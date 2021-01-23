package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;


public class ExlinkBodyCell extends MessageBody {

    private String title;

    private String url;

    private String digest;

    private String mediaId;

    private int linkType;

    private String subTitle;

    private String linkDesc;

    private int dateHide;

    private int tailHide;

    public ExlinkBodyCell(){}

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
        json.addProperty("title", this.title != null ? this.title : "");
        json.addProperty("url", this.url != null ? this.url : "");
        json.addProperty("digest", this.digest != null ? this.digest : "");
        json.addProperty("media_id", this.mediaId != null ? this.mediaId : "");
        json.addProperty("link_type", this.linkType);
        json.addProperty("subtitle", this.subTitle != null ? this.subTitle : "");
        json.addProperty("link_desc", this.linkDesc != null ? this.linkDesc : "");
        json.addProperty("date_hide", this.dateHide);
        json.addProperty("tail_hide", this.tailHide);
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

    public int getLinkType() {
        return linkType;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getLinkDesc() {
        return linkDesc;
    }

    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc;
    }

    public int getDateHide() {
        return dateHide;
    }

    public void setDateHide(int dateHide) {
        this.dateHide = dateHide;
    }

    public int getTailHide() {
        return tailHide;
    }

    public void setTailHide(int tailHide) {
        this.tailHide = tailHide;
    }
}
