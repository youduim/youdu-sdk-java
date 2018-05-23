package im.youdu.sdk.client;

import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import junit.framework.TestCase;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class GroupClientTest extends TestCase {
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "sysOrgAssistant"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "n76ut0qxPWozXbMxGMt8s9pgxUZKUxu/GJ5R5dz+u4g="; // 请填写企业应用的EncodingaesKey

    private GroupClient groupClient;

    public GroupClientTest() throws Exception {
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, "", APP_ID, "", APP_AESKEY);
        groupClient = new GroupClient(app);
    }

    //测试创建群
    public void testCreateGroup() throws ParamParserException, HttpRequestException, AESCryptoException {
        String groupId = groupClient.createGroup("新的群组");
        System.out.println("create group ok. groupId: "+groupId);
    }

    //测试修改群名称
    public void testUpdateGroupName() throws ParamParserException, HttpRequestException, AESCryptoException, UnsupportedEncodingException {
        String groupId = "{62A4B7FC-8648-4365-A1C4-2F5FDEB26C7F}";
        String groupName = "新的群组改名";
        groupClient.updateGroupName(groupId,groupName);
        System.out.println("update group name ok. groupId: "+groupId+", groupName: "+groupName);
    }

    //测试增加群成员
    public void testAddMember() throws ParamParserException, HttpRequestException, AESCryptoException {
        String[] mem = new String[5];
        mem[0]="test1";
        mem[1]="test2";
        mem[2]="test3";
        mem[3]="test4";
        mem[4]="test5";
        String groupId = "{62A4B7FC-8648-4365-A1C4-2F5FDEB26C7F}";
        groupClient.addGroupMember(groupId,mem);
    }

    //测试删除群成员
    public void testDelMember() throws ParamParserException, HttpRequestException, AESCryptoException {
        String[] mem = new String[1];
        mem[0]="test4";
        String groupId = "{62A4B7FC-8648-4365-A1C4-2F5FDEB26C7F}";
        groupClient.delGroupMember(groupId,mem);
    }

    //测试获取群信息
    public void testGetGroup() throws HttpRequestException, UnsupportedEncodingException, AESCryptoException, ParamParserException {
        String groupId = "{62A4B7FC-8648-4365-A1C4-2F5FDEB26C7F}";
        Group group = groupClient.groupInfo(groupId);
        System.out.println(group.getId()+":"+group.getName()+":"+group.getAdmin());
        for(GroupMember mem:group.getMembers()){
            System.out.println(mem.getAccount()+":"+mem.getName()+":"+mem.getMobile());
        }
    }

    //测试是否群成员
    public void testIsGroupMember() throws HttpRequestException, UnsupportedEncodingException, AESCryptoException, ParamParserException {
        String userId = "test5";
        String groupId = "{62A4B7FC-8648-4365-A1C4-2F5FDEB26C7F}";
        boolean is = groupClient.isGroupMember(groupId,userId);
        System.out.println(is);
    }

    //测试获取所有群列表
    public void testListGroup() throws ParamParserException, HttpRequestException, AESCryptoException {
        String userId = "test5";
        List<GroupSimple> groups = groupClient.groupList(userId);
        for(GroupSimple group : groups){
            System.out.println(group.getId()+":"+group.getName());
        }
    }

    //测试获取用户的群列表
    public void testListUserGroup() throws ParamParserException, HttpRequestException, AESCryptoException {
        String userId = "test1";
        List<GroupSimple> groups = groupClient.groupList(userId);
        for(GroupSimple group : groups){
            System.out.println(group.getId()+":"+group.getName());
        }
    }

    //测试删除群
    public void testDeleteGroup() throws ParamParserException, HttpRequestException, AESCryptoException, UnsupportedEncodingException {
        String delGroupId = "{B86D528C-C21F-468C-B5E6-89862AB1F1D5}";
        groupClient.deleteGroup(delGroupId);
        System.out.println("delete group ok. groupId: "+delGroupId);
    }

}
