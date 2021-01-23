package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PopWindowEventDetail {
    private Integer action;
    private Long timeStamp;

    private String tips = "";
    private String windowType;
    private PopWindow popWindow;

    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("tips", null == this.tips?"":tips);
        json.addProperty("action", this.action);
        json.addProperty("timestamp", null == this.timeStamp? System.currentTimeMillis():timeStamp);
        json.addProperty("windowType", this.windowType);
        if(null != popWindow){
            json.add("windowInfo", this.popWindow.toJsonElement());
        }
        return json;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getWindowType() {
        return windowType;
    }

    public void setWindowType(String windowType) {
        this.windowType = windowType;
    }

    public PopWindow getPopWindow() {
        return popWindow;
    }

    public void setPopWindow(PopWindow popWindow) {
        this.popWindow = popWindow;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
