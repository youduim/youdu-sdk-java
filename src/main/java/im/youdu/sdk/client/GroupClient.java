package im.youdu.sdk.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.encrypt.AESCrypto;
import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GroupClient {
    private int buin;
    private String host;
    private String appId;
    private String appAeskey;

    private AESCrypto crypto;
    private AppTokenClient tokenClient;

    public GroupClient(YDApp app) {
        this.buin = app.getBuin();
        this.host = app.getHost();
        this.appId = app.getAppId();
        this.appAeskey = app.getAppAesKey();
        this.crypto = new AESCrypto(appId, appAeskey);
        this.tokenClient = new AppTokenClient(buin,host,appId,appAeskey);
    }
//----------------------------------------------------------------------------------------------------------------------
    //创建群
    public String createGroup(String groupName) throws ParamParserException, AESCryptoException, HttpRequestException {
        if(null == groupName || "".equals(groupName.trim())){
            throw new ParamParserException("groupName is null",null);
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("name", groupName);
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        JsonObject jsonRsp = Helper.postJson(this.uriCreateGroup(), param.toString());
        String encrypt = Helper.getString("encrypt", jsonRsp);
        if (encrypt.isEmpty()) {
            throw new ParamParserException("找不到返回结果的加密字段", null);
        }
        byte[] rspBuffer = this.crypto.decrypt(encrypt);
        JsonObject jsonResult = Helper.parseJson(Helper.utf8String(rspBuffer));
        String groupId = Helper.getString("id", jsonResult);
        return groupId;
    }

    //列出所有群
    public List<GroupBase> listAllGroups() throws ParamParserException, HttpRequestException, AESCryptoException {
        String url = this.uriListGroup("");
        List<GroupBase> groups = doGroupList(url);
        return  groups;
    }

    //列出用户所在的群
    public List<GroupBase> listUserGroups(String userId) throws ParamParserException, HttpRequestException, AESCryptoException {
        if(null == userId || "".equals(userId.trim())){
            throw new ParamParserException("userId is null",null);
        }
        String url = this.uriListGroup(userId);
        List<GroupBase> groups = doGroupList(url);
        return  groups;
    }

    private List<GroupBase> doGroupList(String url) throws ParamParserException, HttpRequestException, AESCryptoException {
        JsonObject jsonRsp = Helper.getUrlV2(url);
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject jsonObj = Helper.parseJson(new String(decryptRsp));

        JsonArray jGroupArray =  Helper.getArray("groupList", jsonObj);
        List<GroupBase> groups = new ArrayList<GroupBase>();
        for(JsonElement e : jGroupArray){
            if(!e.isJsonObject()){
                continue;
            }
            JsonObject jGroup = e.getAsJsonObject();
            GroupBase group = new GroupBase();
            group.setId(Helper.getString("id",jGroup));
            group.setName(Helper.getString("name",jGroup));
            groups.add(group);
        }
        return  groups;
    }

    //删除群
    public void deleteGroup(String groupId) throws ParamParserException, AESCryptoException, HttpRequestException, UnsupportedEncodingException {
        if(null == groupId || "".equals(groupId.trim())){
            throw new ParamParserException("groupId is null",null);
        }
        String url = uriDeleteGroup(groupId);
        Helper.getUrlV1(url);
    }

    //修改群名称
    public void updateGroupName(String groupId, String groupName) throws ParamParserException, AESCryptoException, HttpRequestException {
        if(null == groupId || "".equals(groupId.trim())){
            throw new ParamParserException("groupId is null",null);
        }
        if(null == groupName || "".equals(groupName.trim())){
            throw new ParamParserException("groupName is null",null);
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("id", groupId);
        obj.addProperty("name", groupName);
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriUpdateGroup(), param.toString());
    }

    //获取群信息
    public Group groupInfo(String groupId) throws ParamParserException, HttpRequestException, AESCryptoException, UnsupportedEncodingException {
        if(null == groupId || "".equals(groupId.trim())){
            throw new ParamParserException("groupId is null",null);
        }

        String url = uriGroupInfo(groupId);
        JsonObject jsonRsp =  Helper.getUrlV2(url);
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject jsonObj = Helper.parseJson(new String(decryptRsp));
        Group group = new Group();
        group.setId(Helper.getString("id",jsonObj));
        group.setName(Helper.getString("name",jsonObj));
        group.setAdmin(Helper.getString("admin",jsonObj));
        JsonArray jMemberArray =  Helper.getArray("members", jsonObj);
        if(null == jMemberArray || jMemberArray.size()==0){
            return  group;
        }

        List<GroupMember> mems = new ArrayList<GroupMember>();
        for(JsonElement e : jMemberArray){
            if(!e.isJsonObject()){
                continue;
            }
            JsonObject jMem = e.getAsJsonObject();
            GroupMember mem = new GroupMember();
            mem.setAccount(Helper.getString("account",jMem));
            mem.setName(Helper.getString("name",jMem));
            mem.setMobile(Helper.getString("mobile",jMem));
            mems.add(mem);
        }
        group.setMembers(mems);
        return  group;
    }

    //添加群成员
    public void addGroupMember(String groupId, String[] addUserList) throws ParamParserException, AESCryptoException, HttpRequestException {
        if(null == addUserList || addUserList.length == 0){
            throw new ParamParserException("addUserList is null",null);
        }

        JsonArray array = new JsonArray();
        for (int i = 0; i < addUserList.length; i++) {
            array.add(addUserList[i]);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("id", groupId);
        obj.add("userList", array);
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriAddGroupMember(), param.toString());
    }

    //删除群成员
    public void delGroupMember(String groupId, String[] delUserList) throws ParamParserException, AESCryptoException, HttpRequestException {
        if(null == delUserList || delUserList.length == 0){
            throw new ParamParserException("delUserList is null",null);
        }

        JsonArray array = new JsonArray();
        for (int i = 0; i < delUserList.length; i++) {
            array.add(delUserList[i]);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("id", groupId);
        obj.add("userList", array);
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriDelGroupMember(), param.toString());
    }

    //是否群成员
    public boolean isGroupMember(String groupId, String userId) throws ParamParserException, UnsupportedEncodingException, HttpRequestException, AESCryptoException {
        if(null == groupId || "".equals(groupId.trim())){
            throw new ParamParserException("groupId is null",null);
        }
        if(null == userId || "".equals(userId.trim())){
            throw new ParamParserException("userId is null",null);
        }

        String url = uriIsGroupMember(groupId,userId);
        JsonObject jsonRsp =  Helper.getUrlV2(url);
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject jsonObj = Helper.parseJson(new String(decryptRsp));
        boolean belong = jsonObj.get("belong").getAsBoolean();
        return belong;
    }

//----------------------------------------------------------------------------------------------------------------------
    private String uriCreateGroup() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_GROUP_CREATE,this.tokenClient.getToken()) ;
    }

    private String uriDeleteGroup(String groupId) throws ParamParserException, HttpRequestException, AESCryptoException, UnsupportedEncodingException {
        return String.format("%s%s%s?accessToken=%s&id=%s", YdApi.SCHEME,this.host,YdApi.API_GROUP_DELETE,this.tokenClient.getToken(), URLEncoder.encode(groupId,"utf-8")) ;
    }

    private String uriUpdateGroup() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_GROUP_UPDATE,this.tokenClient.getToken()) ;
    }

    private String uriGroupInfo(String groupId) throws ParamParserException, HttpRequestException, AESCryptoException, UnsupportedEncodingException {
        return String.format("%s%s%s?accessToken=%s&id=%s", YdApi.SCHEME,this.host,YdApi.API_GROUP_INFO,this.tokenClient.getToken(), URLEncoder.encode(groupId,"utf-8")) ;
    }

    private String uriListGroup(String userId) throws ParamParserException, HttpRequestException, AESCryptoException {
        if(null == userId || "".equals(userId)){
            return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_GROUP_LIST,this.tokenClient.getToken()) ;
        }
        return String.format("%s%s%s?accessToken=%s&userId=%s", YdApi.SCHEME,this.host,YdApi.API_GROUP_LIST,this.tokenClient.getToken(),userId) ;
    }

    private String uriAddGroupMember() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_GROUP_ADDMEMBER,this.tokenClient.getToken()) ;
    }

    private String uriDelGroupMember() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_GROUP_DELMEMBER,this.tokenClient.getToken()) ;
    }

    private String uriIsGroupMember(String groupId, String userId) throws ParamParserException, HttpRequestException, AESCryptoException, UnsupportedEncodingException {
        groupId = URLEncoder.encode(groupId,"utf-8");
        return String.format("%s%s%s?accessToken=%s&id=%s&userId=%s", YdApi.SCHEME,this.host,YdApi.API_GROUP_ISMEMBER,this.tokenClient.getToken(),groupId,userId) ;
    }
}
