package im.youdu.sdk.entity;

import com.google.gson.JsonObject;

public class PopWindowEvent extends EventBase {
    private PopWindowEventDetail eventDetail;

    public PopWindowEventDetail getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(PopWindowEventDetail eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    public JsonObject toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("eventId", this.getEventId());
        json.addProperty("eventType", this.getEventType());
        json.addProperty("toGid", this.getToGid());
        json.addProperty("toUser", this.getToUser());
        json.addProperty("toDept", this.getToDept());
        json.add(this.getEventType(), this.eventDetail.toJsonElement());
        return json;
    }
}
