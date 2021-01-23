package im.youdu.sdk.entity;

public class EventBase {
    private Long eventId;
    private String eventType;
    private String toGid;
    private String toUser;
    private String toDept;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getToDept() {
        return toDept;
    }

    public void setToDept(String toDept) {
        this.toDept = toDept;
    }

    public String getToGid() {
        return toGid;
    }

    public void setToGid(String toGid) {
        this.toGid = toGid;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
