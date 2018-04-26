package im.youdu.sdk.entity;

import java.util.Arrays;

/**
 * Created by administrator on 2017/1/6.
 */
public class UserDetail {
    private UserInfo user;
    private DeptUserInfo[] deptUserInfo;

    public UserInfo getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "user=" + user +
                ", deptUserInfo=" + Arrays.toString(deptUserInfo) +
                '}';
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public DeptUserInfo[] getDeptUserInfo() {
        return deptUserInfo;
    }

    public void setDeptUserInfo(DeptUserInfo[] deptUserInfo) {
        this.deptUserInfo = deptUserInfo;
    }
}
