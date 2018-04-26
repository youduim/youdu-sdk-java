package im.youdu.sdk.entity;

/**
 * Created by administrator on 2017/1/6.
 */
public class DeptUserInfo {
    private String userId;
    private int deptId;
    private String position;
    private int weight;
    private int sortId;

    @Override
    public String toString() {
        String str = "";
        if(null == userId || userId==""){
            str = "DeptUserInfo{" +
                    "deptId=" + deptId +
                    ", position='" + position + '\'' +
                    ", weight=" + weight +
                    ", sortId=" + sortId +
                    '}';
        }else{
            str =  "DeptUserInfo{" +
                    "userId='" + userId + '\'' +
                    ", deptId=" + deptId +
                    ", position='" + position + '\'' +
                    ", weight=" + weight +
                    ", sortId=" + sortId +
                    '}';
        }
        return str;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }
}
