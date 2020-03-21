package im.youdu.sdk.entity;

import com.google.gson.JsonObject;

public class AudioVideoEvent extends EventBase {
    private PopWindow popWindow;

    public PopWindow getPopWindow() {
        return popWindow;
    }

    public void setPopWindow(PopWindow popWindow) {
        this.popWindow = popWindow;
    }

    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    public JsonObject toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("eventType", this.getEventType());
        json.addProperty("toGid", this.getToGid());
        json.addProperty("toUser", this.getToUser());
        json.addProperty("toDept", this.getToDept());
        json.add(this.getEventType(), this.popWindow.toJsonElement());
        return json;
    }
}
