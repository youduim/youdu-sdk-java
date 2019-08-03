package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

import java.util.*;

// 文件消息体
public class RecvSystemMsgBody extends MessageBody {
    private String title;
    private List<Map<String,MessageBody>> msg;

    private List<Dept> selectDept;
    private List<String> selectUser;

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
        JsonObject obj = json.getAsJsonObject();
        this.title = Helper.getString("title",obj);

        //解析接收用户数组
        JsonArray jUserArray =  Helper.getArray("select_user", obj);
        this.selectUser = new ArrayList<String>();
        if(null != jUserArray){
            for(JsonElement e : jUserArray){
                if(e.isJsonPrimitive()){
                    this.selectUser.add(e.getAsString());
                }
            }
        }

        //解析接收部门数组
        JsonArray jDeptArray =  Helper.getArray("select_dept", obj);
        this.selectDept = new ArrayList<Dept>();
        if(null != jDeptArray){
            for(JsonElement e : jDeptArray){
                if(e.isJsonObject()){
                    JsonObject jObj = e.getAsJsonObject();
                    int deptId = Helper.getInt("id",jObj);
                    String deptName = Helper.getString("name",jObj);
                    Dept dept = new Dept(deptId,deptName);
                    this.selectDept.add(dept);
                }
            }
        }

        //解析消息体
        JsonArray jContentArray =  Helper.getArray("complex", obj);
        this.msg = Helper.parseComplexMsgBody(jContentArray);
        return this;
    }

    @Override
    public String toJsonString() {
        return "";
    }

    @Override
    public JsonElement toJsonElement() {
        return null;
    }

    @Override
    public String toString() {
        return "SysMsgBody{" +
                "title='" + title + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Dept> getSelectDept() {
        return selectDept;
    }

    public void setSelectDept(List<Dept> selectDept) {
        this.selectDept = selectDept;
    }

    public List<String> getSelectUser() {
        return selectUser;
    }

    public void setSelectUser(List<String> selectUser) {
        this.selectUser = selectUser;
    }

    public List<Map<String, MessageBody>> getMsg() {
        return msg;
    }

    public void setMsg(List<Map<String, MessageBody>> msg) {
        this.msg = msg;
    }
}
