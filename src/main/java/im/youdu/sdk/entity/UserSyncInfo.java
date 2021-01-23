package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;

public class UserSyncInfo extends UserInfo {
    private Integer authType;
    private String password;

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String check(){
        String str = "";
        for(;;){
            if(Helper.isEmpty(this.getUserId())){
                str = "userId is null";
                break;
            }
            if(Helper.isEmpty(getName())){
                str = "user-name is null";
                break;
            }
            if(null == getGender()){
                str = "user-gender is null";
                break;
            }
            if(null == getDept() || getDept().length==0){
                str = "user-dept is null";
            }
            break;
        }
        return  str;
    }

    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("userId", this.getUserId());
        json.addProperty("name", this.getName());
        json.addProperty("gender", this.getGender());
        json.addProperty("enableState", this.getEnableState());

        if(null != this.getMobile()){
            json.addProperty("mobile", this.getMobile());
        }
        if(null != this.getPhone()){
            json.addProperty("phone", this.getPhone());
        }
        if(null != this.getEmail()){
            json.addProperty("email", this.getEmail());
        }
        if(null != this.getAuthType()){
            json.addProperty("authType", this.getAuthType());
        }
        if(null != this.getPassword()){
            json.addProperty("passwd", this.getPassword());
        }

        int[] deptIds = this.getDept();
        if(null != deptIds && deptIds.length>0){
            JsonArray array = new JsonArray();
            for(int id : deptIds){
                array.add(id);
            }
            json.add("dept", array);
        }
        UserDeptPosition[] ps = this.getDeptDetail();
        if(null !=ps && ps.length>0){
            JsonArray array = new JsonArray();
            for(UserDeptPosition p : ps){
                JsonObject obj = new JsonObject();
                obj.addProperty("deptId",p.getDeptId());
                if(!Helper.isEmpty(p.getPosition())){
                    obj.addProperty("position",p.getPosition());
                }
                if(null != p.getWeight()){
                    obj.addProperty("weight",p.getWeight());
                }
                if(null != p.getSortId()){
                    obj.addProperty("sortId",p.getSortId());
                }
                array.add(obj);
            }
            json.add("deptDetail", array);
        }
        return json;
    }
}
