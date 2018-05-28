package im.youdu.sdk.client;

import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;
import junit.framework.TestCase;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class OrgClientTest extends TestCase {
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "sysOrgAssistant"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "n76ut0qxPWozXbMxGMt8s9pgxUZKUxu/GJ5R5dz+u4g="; // 请填写企业应用的EncodingaesKey

    private OrgClient orgClient;

    public OrgClientTest() throws Exception {
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, "", APP_ID, "", APP_AESKEY);
        orgClient = new OrgClient(app);
    }

    //测试创建部门
    public void testCreateDept() throws ParamParserException, HttpRequestException, AESCryptoException {
        Dept dept = new Dept();
        dept.setName("测试部门创建");
        dept.setParentId(0);
        dept.setAlias("abc");
        int deptId = orgClient.createDept(dept);
        System.out.println("create dept ok. deptId: "+deptId);
    }

    //测试修改部门
    public void testUpdateDept() throws ParamParserException, HttpRequestException, AESCryptoException {
        int deptId = 1;
        Dept dept = new Dept();
        dept.setId(deptId);
        dept.setName("test测试-1");
        dept.setAlias("abcd");
        orgClient.updateDept(dept);
        System.out.println("update dept with id "+deptId+" ok.");
    }

    //测试获取部门
    public void testGetDept() throws ParamParserException, HttpRequestException, AESCryptoException {
        int deptId = 1;
        Dept dept = orgClient.getDept(deptId);
        System.out.println("get dept with id "+deptId+" ok: "+dept);
    }

    //测试获取当前部门及直属子部门列表
    public void testListDeptSelfAndChildren() throws ParamParserException, HttpRequestException, AESCryptoException {
        int deptId = 1;
        List<Dept> depts = orgClient.listDeptSelfAndChildren(deptId);
        if(depts.size()==0){
            System.out.println("list dept children ok, found no children: "+deptId);
            return;
        }
        for(Dept dept : depts){
            System.out.println("get dept child: "+dept);
        }
    }

    //测试获取直属子部门列表
    public void testListDeptChildren() throws ParamParserException, HttpRequestException, AESCryptoException {
        int deptId = 1;
        List<Dept> depts = orgClient.listDeptChildren(deptId);
        if(depts.size()==0){
            System.out.println("list dept children ok, found no children: "+deptId);
            return;
        }
        for(Dept dept : depts){
            System.out.println("get dept children: "+dept);
        }
    }

    //测试通过别名获取部门ID
    public void testGetDeptIdsByAlias() throws ParamParserException, HttpRequestException, AESCryptoException {
        String alias = "alias_test";
       int deptId = orgClient.getDeptIdByAlias(alias);
       System.out.println(String.format("get deptId by alias ok: %s,%d",alias,deptId) );
    }

    //测试获取所有有别名的部门ID列表
    public void testGetAllDeptIdsHasAlias() throws ParamParserException, HttpRequestException, AESCryptoException {
        List<AliasDept> depts = orgClient.listAliasDept();
        if(depts.size()==0){
            System.out.println("no dept was found has alias");
            return;
        }
        for(AliasDept dept : depts){
            System.out.println(String.format("get alias dept: %s:%d",dept.getAlias(),dept.getDeptId()));
        }
    }

    //测试删除部门
    public void testDeleteDept() throws ParamParserException, HttpRequestException, AESCryptoException {
        int deptId = 1;
        orgClient.deleteDept(deptId);
        System.out.println("delete dept with id "+deptId+" ok.");
    }

    //------------------------------------------------------------------------------------------------------------------
    //创建用户
    public void testCreateUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserInfo user = new UserInfo();
        user.setUserId("test1");
        user.setGender(Const.Gender_Male);
        user.setChsName("张三");
        user.setDept(new int[]{0});
        orgClient.createUser(user);
        System.out.println("create user ok: test1 张三");
    }

    //修改用户基本信息
    public void testUpdateUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserInfo user = new UserInfo();
        user.setUserId("test1");
        user.setGender(Const.Gender_Male);
        user.setChsName("李四");
        user.setPhone("13112345678");
        orgClient.updateUser(user);
        System.out.println("update user ok: test1 李四");
    }

    //修改用户在某个部门下的职务信息
    public void testUpdateUserPosition() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserDeptPosition position = new UserDeptPosition();
        position.setUserId("test1");
        position.setDeptId(0);
        position.setPosition("工程师");
        position.setWeight(0);
        position.setSortId(10);
        orgClient.updateUserPosition(position);
        System.out.println("update user dept position ok: test1 工程师");
    }

    //获取用户信息
    public void testGetUserInfo() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserInfo user = orgClient.getUserInfo("test1");
        System.out.println("get userInfo ok:"+user);
    }

    //获取部门用户简单信息
    public void testListDeptUserSimple() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserInfo[] users = orgClient.listDeptUserSimple(0);
        for(UserInfo user : users){
            System.out.println("get dept user simple ok:"+user);
        }
    }

    //获取部门用户详细信息
    public void testListDeptUserDetail() throws ParamParserException, HttpRequestException, AESCryptoException{
        UserDetail[] users = orgClient.listDeptUserDetail(0);
        for(UserDetail user : users){
            System.out.println("get dept user simple ok:"+user);
        }
    }

    //设置用户密码
    public void testSetUserPwd() throws ParamParserException, HttpRequestException, AESCryptoException, NoSuchAlgorithmException {
        orgClient.setUserPwd("test1","1a7434b0243d345b4fdb5b3d434c9aba52a5"); //1次md5
        System.out.println("set user auth ok");
    }

    //设置用户登录认证方式
    public void testSetUserLoginAuthType() throws ParamParserException, HttpRequestException, AESCryptoException, NoSuchAlgorithmException {
        orgClient.setUserLoginAuthType("test1",Const.AuthType_Youdu); //设置为有度认证
//        orgClient.setUserLoginAuthType("test1",Const.AuthType_Rtx); //设置为rtx认证
//        orgClient.setUserLoginAuthType("test1",Const.AuthType_Other); //设置为其他第三方认证方式
        System.out.println("set user auth ok");
    }

    //删除用户
    public void testDeleteUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        orgClient.deleteUser("add");
        System.out.println("delete user auth ok");
    }

    //批量删除用户
    public void testBatchDeleteUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        String[] users = new String[]{"add","tst1"};
        orgClient.batchDeleteUser(users);
        System.out.println("batch delete user auth ok");
    }

    //设置用户头像
    public void testSetAvatar1() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String userId = "test1";
        String avatarFile = "D:\\pics\\avatars\\avatar1.png";
        orgClient.setUserAvatar(userId, avatarFile);
    }

    //设置用户头像
    public void testSetAvatar2() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String userId = "test1";
        String avatarFile = "D:\\pics\\avatars\\avatar1.png";
        byte[] data = Helper.readFile(avatarFile);
        String name = Helper.readFileNameFromPath(avatarFile);
        orgClient.setUserAvatarWithData(userId, name, data);
    }

    //下载小头像
    public void testDownloadSmallAvatar() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String userId = "test1";
        String avatarDir = "D:\\pics\\avatars\\";
        String path = orgClient.DownloadUserAvatarAndSave(userId, Const.Avatar_Small, avatarDir);
        System.out.println("download avatar small ok: "+path);
    }

    //下载大头像
    public void testDownloadLargeAvatar() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String userId = "test1";
        String avatarDir = "D:\\pics\\avatars\\";
        String path = orgClient.DownloadUserAvatarAndSave(userId, Const.Avatar_Large, avatarDir);
        System.out.println("download avatar large ok: "+path);
    }
}
