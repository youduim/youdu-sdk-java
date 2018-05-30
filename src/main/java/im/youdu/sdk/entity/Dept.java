package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;

public class Dept {
    private Integer id;
    private String name;
    private Integer parentId;
    private Integer sortId;
    private String alias;

    public Dept() {
    }

    public Dept(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Dept(int id, String name, int parentId, int sortId, String alias) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.sortId = sortId;
        this.alias = alias;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", sortId=" + sortId +
                ", alias=" + alias +
                '}';
    }

    public String check(){
        String str = "";
        for(;;){
            if(null == id){
                str = "dept-id is null";
                break;
            }
            if(Helper.isEmpty(name)){
                str = "dept-name is null";
                break;
            }
            if(null == parentId){
                str = "dept-parentId is null";
            }
            break;
        }
        return  str;
    }

    public String toJsonString() {
        return this.toJsonElement().toString();
    }

    public JsonElement toJsonElement() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id",this.id);
        obj.addProperty("name",this.name);
        obj.addProperty("parentId",this.parentId);
        if(null != this.sortId){
            obj.addProperty("sortId",this.sortId);
        }
        if(null != this.alias){
            obj.addProperty("alias",this.alias);
        }
        return  obj;
    }
}
