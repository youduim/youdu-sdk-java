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

public class OrgUserClientTest extends TestCase {
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "sysOrgAssistant"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "8ZQRuCA4N7BJPLSWbzWD6744qP/axLwfs2kruYCJcMk="; // 请填写企业应用的EncodingaesKey

    private String userId = "hunter.lin"; // 用户账号
    private String avatarDir = "D:\\pics\\avatar";// 保存头像的路径
    private String avatarFile = "D:\\pics\\png\\xxx.png";// 保存头像的路径

    private OrgUserClient orgUserClient;

    public OrgUserClientTest() throws Exception {
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, "", APP_ID, "", APP_AESKEY);
        orgUserClient = new OrgUserClient(app);
    }

    //创建用户
    public void testCreateUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserInfo user = new UserInfo();
        user.setUserId("test1");
        user.setGender(Const.Gender_Female);
        user.setChsName("张三");
        user.setDept(new int[]{0});
        orgUserClient.createUser(user);
        System.out.println("create user ok: test1 张三");
    }

    //修改用户
    public void testUpdateUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserInfo user = new UserInfo();
        user.setUserId("test1");
        user.setGender(Const.Gender_Male);
        user.setChsName("李四");
        user.setPhone("13112345678");
        orgUserClient.updateUser(user);
        System.out.println("update user ok: test1 李四");
    }

    //修改用户部门职务
    public void testUpdateUserPosition() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserDeptPosition position = new UserDeptPosition();
        position.setUserId("test1");
        position.setDeptId(0);
        position.setPosition("工程师");
        position.setWeight(0);
        position.setSortId(10);
        orgUserClient.updateUserPosition(position);
        System.out.println("update user dept position ok: test1 工程师");
    }

    //获取用户信息
    public void testGetUserInfo() throws ParamParserException, HttpRequestException, AESCryptoException {
       UserInfo user = orgUserClient.getUserInfo("test1");
        System.out.println("get userInfo ok:"+user);
    }

    //获取部门用户简单信息
    public void testListDeptUserSimple() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserInfo[] users = orgUserClient.listDeptUserSimple(0);
        for(UserInfo user : users){
            System.out.println("get dept user simple ok:"+user);
        }
    }

    //获取部门用户详细信息
    public void testListDeptUserDetail() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserDetail[] users = orgUserClient.listDeptUserDetail(0);
        for(UserDetail user : users){
            System.out.println("get dept user simple ok:"+user);
        }
    }

    //设置用户密码
    public void testSetUserPwd() throws ParamParserException, HttpRequestException, AESCryptoException, NoSuchAlgorithmException {
        orgUserClient.setUserPwd("test1","1a7434b0243d345b4fdb5b3d434c9aba52a5"); //1次md5
        System.out.println("set user auth ok");
    }

    //删除用户
    public void testDeleteUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        orgUserClient.deleteUser("add");
        System.out.println("delete user auth ok");
    }

    //批量删除用户
    public void testBatchDeleteUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        String[] users = new String[]{"add","tst1"};
        orgUserClient.batchDeleteUser(users);
        System.out.println("batch delete user auth ok");
    }

    //设置用户头像
    public void testSetAvatar1() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        orgUserClient.setUserAvatar(userId, avatarFile);
    }

    //设置用户头像
    public void testSetAvatar2() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        byte[] data = Helper.readFile(avatarFile);
        String name = Helper.readFileNameFromPath(avatarFile);
        orgUserClient.setUserAvatarWithData(userId, name, data);
    }

    //下载小头像
    public void testDownloadSmallAvatar1() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        AvatarInfo info = orgUserClient.DownloadUserAvatar(userId, Const.Avatar_Small);
        System.out.println(String.format("download ok: %s; %d",info.getName(),info.size()));
    }

    //下载小头像
    public void testDownloadSmallAvatar() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String path = orgUserClient.DownloadUserAvatarAndSave(userId, Const.Avatar_Small, avatarDir);
        System.out.println("download ok: "+path);
    }

    //下载大头像
    public void testDownloadLargeAvatar() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String path = orgUserClient.DownloadUserAvatarAndSave(userId, Const.Avatar_Large, avatarDir);
        System.out.println("download ok: "+path);
    }

    //下载大头像
    public void testDownloadLargeAvatar1() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        AvatarInfo info = orgUserClient.DownloadUserAvatar(userId, Const.Avatar_Large);
        System.out.println(String.format("download ok: %s; %d",info.getName(),info.size()));
    }
}
