package im.youdu.sdk.entity;

import java.util.ArrayList;
import java.util.List;

public class DeptExpandInfo {
    private List<Dept> deptList = new ArrayList<>();
    private List<UserInfo> userList = new ArrayList<>();

    public List<Dept> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<Dept> deptList) {
        this.deptList = deptList;
    }

    public List<UserInfo> getUserList() {
        return userList;
    }

    public void setUserList(List<UserInfo> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "DeptExpandInfo{" +
                "deptList=" + deptList +
                ", userList=" + userList +
                '}';
    }
}
