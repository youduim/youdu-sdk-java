package im.youdu.sdk.client;

import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;
import junit.framework.TestCase;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static im.youdu.sdk.entity.Const.*;

public class OrgClientTest extends TestCase {
    private static final int BUIN = 14797363; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "sysOrgAssistant"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "qbxKEgW8lMl2nS/F6qdZ9ZIRQtX023wrUCvAoqVjPl8="; // 请填写企业应用的EncodingaesKey

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

    //测试获取所有子部门
    public void testListDeptAllChildren() throws ParamParserException, HttpRequestException, AESCryptoException {
        int deptId = 0;
        List<Dept> depts = orgClient.listDeptAllChildren(deptId);
        if(depts.size()==0){
            System.out.println("list dept children ok, found no children: "+deptId);
            return;
        }
        for(Dept dept : depts) {
            System.out.println("get dept: "+dept);
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
        user.setName("张三");
        user.setDept(new int[]{0});
        user.setEnableState(EnableState_Authorized);
        orgClient.createUser(user);
        System.out.println("create user ok: test1 张三");
    }

    //修改用户基本信息
    public void testUpdateUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserInfo user = new UserInfo();
        user.setUserId("test1");
        user.setGender(Const.Gender_Male);
        user.setName("李四");
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
        System.out.println("get userInfo ok:");
        System.out.println(user);
    }
    
    //获取用户信息
    public void testGetUserInfoList() throws ParamParserException, HttpRequestException, AESCryptoException {
    	long gidList[] = new long[] {100484};
        UserInfo[] users = orgClient.getUserInfoListByYdGid(gidList);
        if (users != null) {
            System.out.println("get userInfo ok:");
            for (UserInfo user : users) {
                System.out.println(user.toString());
            }
        }
    }

    //搜索账号和姓名
    public void testSearchUserInfoList() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserInfo[] users = orgClient.searchUserInfoList("test1");
        if (users != null) {
            System.out.println("get userInfo ok:");
            for (UserInfo user : users) {
                System.out.println(user.toString());
            }
        }
    }

    //获取部门用户简单信息
    public void testListDeptUserSimple() throws ParamParserException, HttpRequestException, AESCryptoException {
        UserInfo[] users = orgClient.listDeptUserSimple(64);
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

    //获取部门下所有用户详细信息
    public void testListDeptAllUserDetail() throws ParamParserException, HttpRequestException, AESCryptoException{
        UserDetail[] users = orgClient.listDeptAllUserDetail(71);
        for(UserDetail user : users){
            System.out.println(user.getUser().getUserId());
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
        String path = orgClient.downloadUserAvatarAndSave(userId, Const.Avatar_Small, avatarDir);
        System.out.println("download avatar small ok: "+path);
    }

    //下载大头像
    public void testDownloadLargeAvatar() throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        String userId = "test1";
        String avatarDir = "D:\\pics\\avatars\\";
        String path = orgClient.downloadUserAvatarAndSave(userId, Const.Avatar_Large, avatarDir);
        System.out.println("download avatar large ok: "+path);
    }

    //设置用户启用状态
    public void testSetEnableState() throws HttpRequestException, AESCryptoException, ParamParserException {
        String userId = "test1";
        int enableState = EnableState_Disabled;
        orgClient.setEnableState(userId, enableState);
    }

    //------------------------------------------------------------------------------------------------------------------
    //测试全同步
    public void testReplaceAll() throws ParamParserException, HttpRequestException, AESCryptoException {
        Dept dept1 = new Dept();
        dept1.setId(1);
        dept1.setName("部门1");
        dept1.setParentId(0);

        Dept dept2 = new Dept();
        dept2.setId(2);
        dept2.setName("部门2");
        dept2.setParentId(0);

        List<Dept> depts = new ArrayList<>();
        depts.add(dept1);
        depts.add(dept2);

        int[] depts1 = {1};
        int[] depts2 = {2};

        UserSyncInfo user1 = new UserSyncInfo();
        user1.setUserId("test2");
        user1.setName("测试2");
        user1.setGender(Const.Gender_Male);
        user1.setDept(depts1);
        user1.setEnableState(EnableState_Disabled);

        UserSyncInfo user2 = new UserSyncInfo();
        user2.setUserId("test3");
        user2.setName("测试3");
        user2.setGender(Const.Gender_Female);
        user2.setDept(depts2);
        user2.setMobile("");
        user2.setEnableState(EnableState_Authorized);

        UserSyncInfo user3 = new UserSyncInfo();
        user3.setUserId("test4");
        user3.setName("测试4");
        user3.setGender(Const.Gender_Female);
        user3.setDept(depts2);
        user3.setMobile("");

        List<UserSyncInfo> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        String jobId = orgClient.orgReplaceAll(depts,users);
        System.out.println("send org replace all ok, get jobId:"+jobId);
    }

    public void testXmlSync() throws ParamParserException, HttpRequestException, AESCryptoException {
        String jobId = orgClient.orgXmlSync("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root>\n" +
                "    <strategy>\n" +
                "        <dept_spliter >/</dept_spliter>\n" +
                "    </strategy>\n" +
                "    <deptList>\n" +
                "        <dept path=\"总经办\"/>\n" +
                "        <dept path=\"采购部\"/>\n" +
                "        <dept path=\"采购部/国内采购部\" id=\"1\"/>\n" +
                "        <dept path=\"采购部/国际采购部\" id=\"2\"/>\n" +
                "        <dept path=\"研发部\"/>\n" +
                "        <dept path=\"研发部/IT部门\"/>\n" +
                "        <dept path=\"研发部/IT部门2\"/>\n" +
                "    </deptList>\n" +
                "    <userList>\n" +
                "        <user id=\"cs1\" name=\"测试1\" gender=\"0\" mobile=\"\" email=\"abc@163.com\" phone=\"0756-12345678\" authtype=\"0\" password=\"1a7434b0243d345b4fdb5b3d434c9aba\" enableState=\"1\">\n" +
                "            <posInfo deptPath=\"研发部/IT部门\" position=\"测试部主管\" sortId=\"2\"/>\n" +
                "            <posInfo deptPath=\"总经办\"/>\n" +
                "        </user>\n" +
                "        <user id=\"cs2\" name=\"测试2\" gender=\"0\" authType=\"2\" enableState=\"-1\">\n" +
                "            <posInfo deptPath=\"研发部/IT部门\" position=\"测试部工程师\"/>\n" +
                "            <posInfo deptPath=\"1\" position=\"测试部工程师2\"/>\n" +
                "        </user>\n" +
                "        <user id=\"cs3\" name=\"测试3\" gender=\"0\" authType=\"2\">\n" +
                "            <posInfo deptPath=\"研发部/IT部门\" position=\"测试部工程师\"/>\n" +
                "            <posInfo deptPath=\"1\" position=\"测试部工程师2\"/>\n" +
                "        </user>\n" +
                "    </userList>\n" +
                "</root>");
        System.out.println("send org replace all ok, get jobId:"+jobId);
    }

    //测试获取全同步任务结果
    public void testGetReplaceAllResult() throws ParamParserException, HttpRequestException, AESCryptoException {
        String jobId = "e21556ac894a219b7750f7c37046209d";
        JobResult result  = orgClient.getJobResult(jobId);
        System.out.print("get org replace all result:");
        int resultCode = result.getResult();
        if(resultCode == Const.Job_Running){
            System.out.println("任务正在进行......");
        }else if(resultCode == Const.Job_Done){
            System.out.println("任务已成功完成");
        }else if(resultCode == Const.Job_Exist){
            System.out.println("有相同的任务正在执行");
        }else if(resultCode == Const.Job_Failed){
            System.out.println("任务执行失败："+result.getDesc());
        }
    }

    public void testDeptExpandInVisible(){
        String userId = "test1";
        int deptId = 0;
        try {
            DeptExpandInfo info = orgClient.deptExpandInVisible(userId, deptId);
            System.out.println(info);
        } catch (ParamParserException e) {
            e.printStackTrace();
        } catch (HttpRequestException e) {
            e.printStackTrace();
        } catch (AESCryptoException e) {
            e.printStackTrace();
        }
    }

    public void testOrgSqliteFileQuery(){
        String userId = "test1";
        long version = 164584984;
        try {
            OrgSqliteFileQueryInfo info = orgClient.queryOrgSqliteFileInfo(userId, version);
            System.out.println("get org file info:"+info);
        } catch (ParamParserException e) {
            e.printStackTrace();
        } catch (HttpRequestException e) {
            e.printStackTrace();
        } catch (AESCryptoException e) {
            e.printStackTrace();
        }
    }

    public void testOrgSqliteFileDownload(){
        String fileId = "3d2452bca9d1c87b806d9c89bf74f934-6587";
        String saveDir = "D:\\youdu\\orgdb";
        try {
                String filePath = orgClient.downloadOrgSqliteFile(fileId, saveDir, "");
                System.out.println("download file ok: "+filePath);
        } catch (ParamParserException e) {
            e.printStackTrace();
        } catch (HttpRequestException e) {
            e.printStackTrace();
        } catch (AESCryptoException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        }
    }

}
