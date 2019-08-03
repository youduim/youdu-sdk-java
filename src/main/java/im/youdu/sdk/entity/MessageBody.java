package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import im.youdu.sdk.exception.ParamParserException;

// 消息体接口
public abstract class MessageBody {

    public abstract MessageBody fromJsonString(String json) throws ParamParserException;

    public abstract MessageBody fromJsonElement(JsonElement json) throws ParamParserException;

    public abstract String toJsonString();

    public abstract JsonElement toJsonElement();

    public TextBody getAsTextBody() {
        if (this instanceof TextBody) {
            return (TextBody)this;
        }
        return null;
    }

    public ImageBody getAsImageBody() {
        if (this instanceof ImageBody) {
            return (ImageBody)this;
        }
        return null;
    }

    public FileBody getAsFileBody() {
        if (this instanceof FileBody) {
            return (FileBody)this;
        }
        return null;
    }

    public MpnewsBody getAsMpnewsBody() {
        if (this instanceof MpnewsBody) {
            return (MpnewsBody)this;
        }
        return null;
    }

    public ExlinkBody getAsExlinkBody() {
        if (this instanceof ExlinkBody) {
            return (ExlinkBody)this;
        }
        return null;
    }

    public SmsBody getAsSmsBody() {
        if (this instanceof SmsBody) {
            return (SmsBody)this;
        }
        return null;
    }

    public TestBody getAsTestBody() {
        if (this instanceof TestBody) {
            return (TestBody)this;
        }
        return null;
    }

    public StatisBody getAsStatisBody() {
        if (this instanceof StatisBody) {
            return (StatisBody)this;
        }
        return null;
    }

    public AudioBody getAsVoiceBody() {
        if (this instanceof AudioBody) {
            return (AudioBody)this;
        }
        return null;
    }

    public VideoBody getAsVideoBody() {
        if (this instanceof VideoBody) {
            return (VideoBody)this;
        }
        return null;
    }

    public ComplexBody getAsComplexBody() {
        if (this instanceof ComplexBody) {
            return (ComplexBody)this;
        }
        return null;
    }

    public RecvSystemMsgBody getAsRecvSysMsgBody() {
        if (this instanceof RecvSystemMsgBody) {
            return (RecvSystemMsgBody)this;
        }
        return null;
    }

    public RecvBroadcastBody getAsRecvBroadcastBody(){
        if (this instanceof RecvBroadcastBody) {
            return (RecvBroadcastBody)this;
        }
        return null;
    }

    public SessionCreateBody getAsSessionCreateBody(){
        if (this instanceof SessionCreateBody) {
            return (SessionCreateBody)this;
        }
        return null;
    }

    public SessionUpdateBody getAsSessionUpdateBody(){
        if (this instanceof SessionUpdateBody) {
            return (SessionUpdateBody)this;
        }
        return null;
    }

    public EventBody getAsEventBody(){
        if (this instanceof EventBody) {
            return (EventBody)this;
        }
        return null;
    }
}
