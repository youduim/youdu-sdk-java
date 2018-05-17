package im.youdu.sdk.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import im.youdu.sdk.encrypt.AESCrypto;
import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

public class OrgUserClient {
    private int buin;
    private String host;
    private String appId;
    private String appAeskey;

    private AESCrypto crypto;
    private AppTokenClient tokenClient;

    public OrgUserClient(YDApp app) {
        this.buin = app.getBuin();
        this.host = app.getHost();
        this.appId = app.getAppId();
        this.appAeskey = app.getAppAesKey();
        this.crypto = new AESCrypto(appId, appAeskey);
        this.tokenClient = new AppTokenClient(buin,host,appId,appAeskey);
    }
//----------------------------------------------------------------------------------------------------------------------
    //创建用户
    public void createUser(UserInfo user) throws ParamParserException, AESCryptoException, HttpRequestException {
        if(Helper.isEmpty(user.getUserId())){
            throw new ParamParserException("userId of user is null",null);
        }
        if(Helper.isEmpty(user.getChsName())){
            throw new ParamParserException("name of user is null",null);
        }
        if(null == user.getGender()){
            throw new ParamParserException("gender of user is null",null);
        }
        if(null == user.getDept()){
            throw new ParamParserException("depts of user is null",null);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("userId", user.getUserId());
        obj.addProperty("name", user.getChsName());
        obj.addProperty("gender", user.getGender());
        JsonArray array = new JsonArray();
        int depts[] = user.getDept();
        for (int i = 0; i < depts.length; i++) {
            array.add(depts[i]);
        }
        obj.add("dept", array);
        if(null != user.getMobile()){
            obj.addProperty("mobile", user.getMobile());
        }
        if(null != user.getPhone()) {
            obj.addProperty("phone", user.getPhone());
        }
        if(null != user.getEmail()){
            obj.addProperty("email", user.getEmail());
        }
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriCreateUser(), param.toString());
    }
//
    //更新用户
    public void updateUser(UserInfo user) throws ParamParserException, AESCryptoException, HttpRequestException {
        if(Helper.isEmpty(user.getUserId())){
            throw new ParamParserException("userId of user is null",null);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("userId", user.getUserId());
        if(null != user.getChsName()){
            obj.addProperty("name", user.getChsName());
        }
        if(null != user.getGender()){
            obj.addProperty("gender", user.getGender());
        }
        if(null != user.getDept()) {
            JsonArray array = new JsonArray();
            int depts[] = user.getDept();
            for (int i = 0; i < depts.length; i++) {
                array.add(depts[i]);
            }
            obj.add("dept", array);
        }
        if(null != user.getMobile()){
            obj.addProperty("mobile", user.getMobile());
        }
        if(null != user.getPhone()) {
            obj.addProperty("phone", user.getPhone());
        }
        if(null != user.getEmail()){
            obj.addProperty("email", user.getEmail());
        }
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriUpdateteUser(), param.toString());
    }
//
    //更新用户部门职务
    public void updateUserPosition(UserDeptPosition position) throws ParamParserException, AESCryptoException, HttpRequestException {
        if(Helper.isEmpty(position.getUserId())){
            throw new ParamParserException("position is null",null);
        }
        if(null == position.getDeptId()){
            throw new ParamParserException("dept is null",null);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("userId", position.getUserId());
        obj.addProperty("deptId",position.getDeptId());
        if(null != position.getPosition()){
            obj.addProperty("mobile", position.getPosition());
        }
        if(null != position.getWeight()) {
            obj.addProperty("phone", position.getWeight());
        }
        if(null != position.getSortId()){
            obj.addProperty("email", position.getSortId());
        }
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriUpdateUserPosition(), param.toString());
    }
//
    //删除用户
    public void deleteUser(String userId) throws ParamParserException, HttpRequestException, AESCryptoException {
        Helper.getUrlV1(this.uriDeleteUser(userId));
    }
//
    //批量删除用户
    public void batchDeleteUser(String[] delList) throws ParamParserException, AESCryptoException, HttpRequestException {
        if(null == delList || delList.length==0){
            throw new ParamParserException("delete list is empty",null);
        }
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        for (int i = 0; i < delList.length; i++) {
            array.add(delList[i]);
        }
        obj.add("delList", array);
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriBatchDeleteUser(), param.toString());
    }
//
    //获取用户信息
    public UserInfo getUserInfo(String userId) throws ParamParserException, HttpRequestException, AESCryptoException {
        JsonObject jsonRsp = Helper.getUrlV2(this.uriGetUser(userId));

        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject jsonObj = Helper.parseJson(new String(decryptRsp));
        UserInfo user = new UserInfo();
        user.setUserId(Helper.getString("userId",jsonObj));
        user.setChsName(Helper.getString("name",jsonObj));
        user.setGender(Helper.getInt("gender",jsonObj));
        user.setMobile(Helper.getString("mobile",jsonObj));
        user.setPhone(Helper.getString("phone",jsonObj));
        user.setEmail(Helper.getString("email",jsonObj));
        JsonArray deptArray = jsonObj.get("dept").getAsJsonArray();
        int dept[] = new int[deptArray.size()];
        for (int i = 0; i < deptArray.size(); i++) {
            dept[i] = deptArray.get(i).getAsInt();
        }
        user.setDept(dept);

        JsonArray deptUserInfoArrary = jsonObj.get("deptDetail").getAsJsonArray();
        UserDeptPosition deptPositions[] = new UserDeptPosition[deptUserInfoArrary.size()];
        for (int i = 0; i < deptUserInfoArrary.size(); i ++) {
            JsonObject deptPositonObj = deptUserInfoArrary.get(i).getAsJsonObject();
            UserDeptPosition position = new UserDeptPosition();
            position.setDeptId(Helper.getInt("deptId",deptPositonObj));
            position.setPosition(Helper.getString("position",deptPositonObj));
            position.setWeight(Helper.getInt("weight",deptPositonObj));
            position.setSortId(Helper.getInt("sortId",deptPositonObj));
            deptPositions[i] = position;
        }
        user.setDeptDetail(deptPositions);

        return user;
    }
//
    //获取部门用户详细信息
    public UserInfo[] listDeptUserSimple(int deptId) throws AESCryptoException, ParamParserException, HttpRequestException {
     JsonObject jsonRsp = Helper.getUrlV2(this.uriGetDeptUser(deptId)) ;
    String cipherRsp = jsonRsp.get("encrypt").getAsString();
    byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
    JsonObject jsonObj = Helper.parseJson(new String(decryptRsp));

    JsonArray userArray = jsonObj.get("userList").getAsJsonArray();
    UserInfo users[] = new UserInfo[userArray.size()];

    for (int i = 0; i < userArray.size(); i++) {
        JsonObject userObj = userArray.get(i).getAsJsonObject();
        UserInfo user = new UserInfo();
        user.setUserId(Helper.getString("userId",userObj));
        user.setChsName(Helper.getString("name",userObj));
        user.setGender(Helper.getInt("gender",userObj));

        JsonArray deptArray = userObj.get("dept").getAsJsonArray();
        int dept[] = new int[deptArray.size()];
        for (int j = 0; j < deptArray.size(); j++) {
            dept[j] = deptArray.get(j).getAsInt();
        }
        user.setDept(dept);
        users[i] = user;
    }
    return users;
}
//
    //获取部门用户详细信息
    public UserDetail[] listDeptUserDetail(int deptId) throws ParamParserException, HttpRequestException, AESCryptoException {
        JsonObject jsonRsp = Helper.getUrlV2(this.uriGetDeptUserDetail(deptId));
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject jsonObj = Helper.parseJson(new String(decryptRsp));

        JsonArray userListArray = jsonObj.get("userList").getAsJsonArray();
        UserDetail userDetails[] = new UserDetail[userListArray.size()];
        for (int i = 0; i < userListArray.size(); i++) {
            UserDetail detail = new UserDetail();

            UserInfo user = new UserInfo();
            JsonObject userObj = userListArray.get(i).getAsJsonObject();
            user.setUserId(userObj.get("userId").getAsString());
            user.setChsName(userObj.get("name").getAsString());
            user.setGender(userObj.get("gender").getAsInt());
            user.setMobile(userObj.get("mobile").getAsString());
            user.setPhone(userObj.get("phone").getAsString());
            user.setEmail(userObj.get("email").getAsString());
            JsonArray deptArray = userObj.get("dept").getAsJsonArray();
            int dept[] = new int[deptArray.size()];
            for (int j = 0; j < deptArray.size(); j++) {
                dept[j] = deptArray.get(j).getAsInt();
            }
            user.setDept(dept);

            detail.setUser(user);

            JsonArray deptDetailArray = userObj.get("deptDetail").getAsJsonArray();
            DeptUserInfo deptUserInfos[] = new DeptUserInfo[deptDetailArray.size()];
            for (int j = 0; j < deptDetailArray.size(); j++) {
                DeptUserInfo deptUser = new DeptUserInfo();
                JsonObject obj = deptDetailArray.get(j).getAsJsonObject();
                deptUser.setDeptId(obj.get("deptId").getAsInt());
                deptUser.setPosition(obj.get("position").getAsString());
                deptUser.setWeight(obj.get("weight").getAsInt());

                deptUserInfos[j] = deptUser;
            }
            detail.setDeptUserInfo(deptUserInfos);
            userDetails[i] = detail;
        }
        return userDetails;
    }
//
    //设置用户密码
    public void setUserPwd(String userId, String pwd) throws ParamParserException, AESCryptoException, HttpRequestException {
        if(Helper.isEmpty(userId)){
            throw new ParamParserException("userId is empty",null);
        }
        if(Helper.isEmpty(pwd)){
            throw new ParamParserException("password is empty",null);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("userId", userId);
        obj.addProperty("passwd", pwd);
        String cipherReq = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherReq);
        Helper.postJson(this.uriSetUserAuth(), param.toString());
    }

    //设置用户头像
    public void setUserAvatar(String userId, String avatarPath) throws HttpRequestException, ParamParserException, AESCryptoException, FileIOException {
        byte[] data = Helper.readFile(avatarPath);
        String name = Helper.readFileNameFromPath(avatarPath);
        this.setAvatar(userId,name,data);
    }

    public void setUserAvatarWithData(String userId, String avatarName,  byte[] data) throws HttpRequestException, ParamParserException, AESCryptoException, FileIOException {
        this.setAvatar(userId,avatarName,data);
    }

    private void setAvatar(String userId, String name, byte[] data) throws AESCryptoException, ParamParserException, HttpRequestException {
        String encryptFile = this.crypto.encrypt(data);
        JsonObject fileNameJson = new JsonObject();
        fileNameJson.addProperty("name", name);
        String cipherName = this.crypto.encrypt(Helper.utf8Bytes(fileNameJson.toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;
        try {
            String url = uriSetUserAvatar(userId);
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("buin", String.format("%d", this.buin));
            builder.addTextBody("appId", this.appId);
            builder.addTextBody("encrypt", cipherName);
            builder.addBinaryBody("file", encryptFile.getBytes(), ContentType.TEXT_PLAIN, "file");
            httpPost.setEntity(builder.build());

            httpRsp = httpClient.execute(httpPost);
            Helper.parseHttpStatus(httpRsp);
            JsonObject jsonResult = Helper.readHttpResponse(httpRsp);
            Helper.parseApiStatus(jsonResult);
        } catch (Exception e) {
            throw new HttpRequestException(0, e.getMessage(), e);
        } finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }

    //下载用户头像
    public AvatarInfo DownloadUserAvatar(String userId, int avatarType) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;
        try {
            String url = this.uriDownloadAvatar(userId,avatarType);
            HttpGet httpGet = new HttpGet(url);
            httpRsp = httpClient.execute(httpGet);
            Header header = httpRsp.getLastHeader("encrypt");
            if (header == null) {
                Helper.parseApiStatus(Helper.readHttpResponse(httpRsp));
                throw new ParamParserException("Header找不到加密内容字段", null);
            }
            String fileInfo = header.getValue();
            byte[] fileInfoBuffer = this.crypto.decrypt(fileInfo);
            String fileInfoResult = Helper.utf8String(fileInfoBuffer);
            JsonObject fileInfoJson = Helper.parseJson(fileInfoResult);
            String name = Helper.getString("name", fileInfoJson);
            long size = Helper.getLong("size", fileInfoJson);
            if(name.isEmpty()){
                name = this.getAvatarName(userId,avatarType,size);
            }
            byte[] data = this.decryptFileData(httpRsp.getEntity());
            AvatarInfo info = new AvatarInfo();
            info.setData(data);
            info.setName(name);
            return info;
        } catch (IOException e) {
            throw new HttpRequestException(0, e.getMessage(), e);
        } finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }

    public String DownloadUserAvatarAndSave(String userId, int avatarType, String dir) throws AESCryptoException, ParamParserException, HttpRequestException, FileIOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;
        try {
            String url = this.uriDownloadAvatar(userId,avatarType);
            HttpGet httpGet = new HttpGet(url);
            httpRsp = httpClient.execute(httpGet);
            Header header = httpRsp.getLastHeader("encrypt");
            if (header == null) {
                Helper.parseApiStatus(Helper.readHttpResponse(httpRsp));
                throw new ParamParserException("Header找不到加密内容字段", null);
            }
            String fileInfo = header.getValue();
            byte[] fileInfoBuffer = this.crypto.decrypt(fileInfo);
            String fileInfoResult = Helper.utf8String(fileInfoBuffer);
            JsonObject fileInfoJson = Helper.parseJson(fileInfoResult);
            String name = Helper.getString("name", fileInfoJson);
            long size = Helper.getLong("size", fileInfoJson);
            if(name.isEmpty()){
               name = this.getAvatarName(userId,avatarType,size);
            }
            byte[] data = this.decryptFileData(httpRsp.getEntity());
            FileInfo info = new FileInfo();
            info.setData(data);
            info.setName(name);
            String finalPath = Helper.saveFile(info,dir);
            return finalPath;
        } catch (IOException e) {
            throw new HttpRequestException(0, e.getMessage(), e);
        } finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }

    private byte[] decryptFileData(HttpEntity rspEntity) throws FileIOException, ParamParserException, AESCryptoException {
        InputStream in = null;
        try {
            in = rspEntity.getContent();
            byte[] encryptBuffer = Helper.readInputStream(in);
            String encryptContent = Helper.utf8String(encryptBuffer);
            byte[] fileContent = this.crypto.decrypt(encryptContent);
            return fileContent;
        } catch (IOException e) {
            throw new FileIOException(e.getMessage(), e);
        } finally {
            Helper.close(in);
        }
    }

    private String getAvatarName(String userId, int avatarType, long size){
        String name = "";
        if(avatarType == Const.Avatar_Small){
            name = userId+"_small.png";
        }else if(avatarType == Const.Avatar_Large){
            name = userId+"_large.png";
        }else{
            name = String.format("%s_%d.png",userId, size);
        }
        return name;
    }

//----------------------------------------------------------------------------------------------------------------------
    public void ReplaceAllOrg(){}

//----------------------------------------------------------------------------------------------------------------------
    private String uriSetUserAvatar(String userId) throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s&userId=%s", YdApi.SCHEME,this.host,YdApi.API_USER_AVATAR_SET,this.tokenClient.getToken(),userId);
    }

    private String uriDownloadAvatar(String userId, int size) throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s&userId=%s&size=%d", YdApi.SCHEME,this.host,YdApi.API_USER_AVATAR_DOWNLOAD,this.tokenClient.getToken(),userId,size);
    }

    private String uriCreateUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_USER_CREATE,this.tokenClient.getToken());
    }

    private String uriUpdateteUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_USER_UPDATE,this.tokenClient.getToken());
    }

    private String uriUpdateUserPosition() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_USER_UPDATE_POSITION,this.tokenClient.getToken());
    }

    private String uriDeleteUser(String userId) throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s&userId=%s", YdApi.SCHEME,this.host,YdApi.API_USER_DELETE,this.tokenClient.getToken(),userId);
    }

    private String uriBatchDeleteUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_USER_DELETE_BATCH,this.tokenClient.getToken());
    }

    private String uriGetUser(String userId) throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s&userId=%s", YdApi.SCHEME,this.host,YdApi.API_USER_GET,this.tokenClient.getToken(),userId);
    }

    private String uriGetDeptUser(int deptId) throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s&deptId=%d", YdApi.SCHEME,this.host,YdApi.API_USER_GET_DEPT,this.tokenClient.getToken(),deptId);
    }

    private String uriGetDeptUserDetail(int deptId) throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s&deptId=%d", YdApi.SCHEME,this.host,YdApi.API_USER_GET_DEPT_DETAIL,this.tokenClient.getToken(),deptId);
    }

    private String uriSetUserAuth() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s", YdApi.SCHEME,this.host,YdApi.API_USER_SET_AUTH,this.tokenClient.getToken());
    }
}
