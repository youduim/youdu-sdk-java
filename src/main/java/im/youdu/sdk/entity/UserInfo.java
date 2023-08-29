package im.youdu.sdk.entity;

public class UserInfo {
	private Long gid = 0L;
    private String userId;
    private String name;
    private String mobile;
    private String phone;
    private String email;
    private Integer gender = 0;
    private Integer enableState = 0;
    private int[] dept;
    private String shortCode;
    private UserDeptPosition[] deptDetail;

    public UserInfo() {
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Long getGid() {
    	return gid;
    }
    
    public void setGid(Long gid) {
    	this.gid = gid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getEnableState() {
        return enableState;
    }

    public void setEnableState(Integer enableState) {
        this.enableState = enableState;
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
        return String.format("gid: %d;\nuserId: %s;\ngender: %d;\nchsName: %s;\nmobile: %s;\nphone: %s;\nemail: %s;\nshortCode: %s;\n",
                this.gid, this.userId, this.gender, this.name, this.mobile, this.phone, this.email,this.shortCode);
    }
}
