package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import im.youdu.sdk.exception.ParamParserException;

/**
 * Created by jin on 2017/8/19.
 */
public class TestBody extends MessageBody {

    public TestBody() {
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        return this;
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        return this;
    }

    @Override
    public String toJsonString() {
        throw  new IllegalAccessError("Not support");
    }

    @Override
    public JsonElement toJsonElement() {
        throw  new IllegalAccessError("Not support");
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
