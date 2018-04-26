package im.youdu.sdk.entity;

public class UserInfo {
    private String userId;
    private String chsName;
    private String mobile;
    private String phone;
    private String email;
    private Integer gender;
    private int[] dept;
    private UserDeptPosition[] deptDetail;

    public UserInfo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChsName() {
        return chsName;
    }

    public void setChsName(String chsName) {
        this.chsName = chsName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public int[] getDept() {
        return dept;
    }

    public void setDept(int[] dept) {
        this.dept = dept;
    }

    public UserDeptPosition[] getDeptDetail() {
        return deptDetail;
    }

    public void setDeptDetail(UserDeptPosition[] deptDetail) {
        this.deptDetail = deptDetail;
    }

    @Override
    public String toString() {
        return String.format("userId: %s;\ngender: %d;\nchsName: %s;\nmobile: %s;\nphone: %s;\nemail: %s;\n", this.userId, this.gender, this.chsName, this.mobile, this.phone, this.email);
    }
}
