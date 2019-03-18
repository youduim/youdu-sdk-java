package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.GeneralEntAppException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

// 接收消息类
public class ReceiveAuth {
    private long createTime;
    private String fromUser = "";
    private String passwd ="";
    private String packgeId = "";
    private String msgType = "";

    public ReceiveAuth() {}

    public ReceiveAuth fromJson(String json) throws ParamParserException {
        JsonObject jObj = Helper.parseJson(json);
        String msgType = Helper.getString("msgType", jObj);
        this.createTime = Helper.getLong("createTime", jObj);
        this.fromUser = Helper.getString("fromUser", jObj);
        this.passwd = Helper.getString("passwd", jObj);
        this.packgeId = Helper.getString("packageId", jObj);

        return this;
    }

    @Override
    public String toString() {
        return "ReceiveAuth{" +
                "fromUser='" + fromUser + '\'' +
                ", createTime=" + createTime +
                ", packgeId=" + packgeId +
                ", msgType='" + msgType + '\'' +
                ", passwd=" + passwd +
                '}';
    }

    /**
     * @return 登录的用户
     */
    public String getFromUser() {
        return fromUser;
    }

    /**
     * @return 消息的发送时间
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * @return packageId，需要返回给有度服务表示接收成功
     */
    public String getPackgeId() {
        return packgeId;
    }

    /**
     * @return 消息类型
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * @return 登录用户密码
     */
    public String getPasswd() {
        return passwd;
    }

}
