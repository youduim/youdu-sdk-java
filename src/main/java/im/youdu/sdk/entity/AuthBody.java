package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;
import im.youdu.sdk.exception.ParamParserException;

/**
 * Created by derek on 2019/3/12.
 */
public class AuthBody extends MessageBody {

    private Long createTime;
    private String fromUser;
    private String password;

    public AuthBody() {

    }

    /**
     * @param createTime 登陆时间
     * @param fromUser 登陆的用户账号
     * @param password 登录的用户密码
     */
    public AuthBody(Long createTime, String fromUser, String password) {
        this.createTime = createTime;
        this.fromUser = fromUser;
        this.password = password;
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        JsonObject result = Helper.parseJson(json);
        return this.fromJsonElement(result);
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        if (!json.isJsonObject()) {
            throw new ParamParserException("json字段类型不匹配", null);
        }

        this.createTime = Helper.getLong("createTime", json.getAsJsonObject());
        this.fromUser = Helper.getString("fromUser", json.getAsJsonObject());
        this.password = Helper.getString("passwd", json.getAsJsonObject());
        return this;
    }

    @Override
    public String toJsonString() {
        return null;
    }

    @Override
    public JsonElement toJsonElement() {
        return null;
    }

    @Override
    public String toString() {
        return "AuthBody{" +
                "createTime='" + createTime + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", passwd='" + password + '\'' +
                '}';
    }

    public Long getCreateTime() {
        return  createTime;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getPassword() {
        return password;
    }
}
