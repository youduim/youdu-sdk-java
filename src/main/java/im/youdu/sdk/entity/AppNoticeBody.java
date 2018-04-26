package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

public class AppNoticeBody extends MessageBody {
    private String account;
    private String tip;
    private int count;
    private PopWindow popWindow;

    public AppNoticeBody() {}

    public AppNoticeBody(String account, int count, String tip) {
        this.account = account;
        this.count = count;
        this.tip = tip;
    }

    public AppNoticeBody(String account, int count, String tip, PopWindow popWindow) {
        this.account = account;
        this.tip = tip;
        this.count = count;
        this.popWindow = popWindow;
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
        this.account = Helper.getString("account", json.getAsJsonObject());
        this.count = Helper.getInt("count", json.getAsJsonObject());
        this.tip = Helper.getString("tip", json.getAsJsonObject());
        return this;
    }

    @Override
    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("tip", this.tip);
        json.addProperty("count", this.count);
        json.addProperty("account", this.account);
        if(null != this.popWindow){
            json.add("pop_wind", this.popWindow.toJsonElement());
        }
        return json;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
