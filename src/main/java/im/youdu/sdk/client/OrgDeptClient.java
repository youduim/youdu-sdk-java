package im.youdu.sdk.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrgDeptClient {
    private int buin;
    private String host;
    private String appId;
    private String appAeskey;

    private AESCrypto crypto;
    private AppTokenClient tokenClient;

    public OrgDeptClient(YDApp app) {
        this.buin = app.getBuin();
        this.host = app.getHost();
        this.appId = app.getAppId();
        this.appAeskey = app.getAppAesKey();
        this.crypto = new AESCrypto(appId, appAeskey);
        this.tokenClient = new AppTokenClient(buin,host,appId,appAeskey);
    }
//----------------------------------------------------------------------------------------------------------------------
    /**
     * 获取部门列表
     */
    public List<Dept> listDeptChildren(int deptId) throws ParamParserException, HttpRequestException, AESCryptoException {
        JsonObject jsonRsp = Helper.getUrlV2(this.uriListDeptChildren(deptId));
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject jsonObj = Helper.parseJson(new String(decryptRsp));

        JsonArray jDeptArray =  Helper.getArray("deptList", jsonObj);
        List<Dept> deptList = new ArrayList<Dept>();
        for(JsonElement e : jDeptArray){
            if(!e.isJsonObject()){
                continue;
            }
            JsonObject jDept = e.getAsJsonObject();
            Dept dept = new Dept();
            dept.setId(Helper.getInt("id",jDept));
            dept.setName(Helper.getString("name",jDept));
            dept.setParentId(Helper.getInt("parentId",jDept));
            dept.setSortId(Helper.getInt("sortId",jDept));
            dept.setAlias(Helper.getString("alias",jDept));
            deptList.add(dept);
        }
        return  deptList;
    }

    /**
     * 创建部门
     */
    public int createDept(Dept dept) throws AESCryptoException, ParamParserException, HttpRequestException {
        if(null == dept.getName()){
            throw new ParamParserException("name of dept is null",null);
        }
        if(null == dept.getParentId()){
            throw new ParamParserException("parentId of dept is null",null);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("name", dept.getName());
        obj.addProperty("parentId", dept.getParentId());
        if(null != dept.getId()){
            obj.addProperty("id", dept.getId());
        }
        if(null != dept.getSortId()) {
            obj.addProperty("sortId", dept.getSortId());
        }
        if(null != dept.getAlias()){
            obj.addProperty("alias", dept.getAlias());
        }
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        JsonObject jsonRsp = Helper.postJson(this.uriCreateDept(), param.toString());
        String encrypt = Helper.getString("encrypt", jsonRsp);
        if (encrypt.isEmpty()) {
            throw new ParamParserException("找不到返回结果的加密字段", null);
        }
        byte[] rspBuffer = this.crypto.decrypt(encrypt);
        JsonObject jsonResult = Helper.parseJson(Helper.utf8String(rspBuffer));
        int deptId = Helper.getInt("id", jsonResult);
        return deptId;
    }

    /**
     * 修改部门
     */
    public void updateDept(Dept dept) throws ParamParserException, AESCryptoException, HttpRequestException {
        if(null == dept.getId()){
            throw new ParamParserException("id of dept is null",null);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("id", dept.getId());
        if(null != dept.getName()){
            obj.addProperty("name", dept.getName());
        }
        if(null != dept.getParentId()){
            obj.addProperty("parentId", dept.getParentId());
        }
        if(null != dept.getSortId()) {
            obj.addProperty("sortId", dept.getSortId());
        }
        if(null != dept.getAlias()){
            obj.addProperty("alias", dept.getAlias());
        }
        String cipherStr = this.crypto.encrypt(Helper.utf8Bytes(obj.toString()));
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherStr);
        Helper.postJson(this.uriUpdateDept(), param.toString());
    }

    /**
     * 获取部门
     */
    public Dept getDept(int deptId) throws ParamParserException, HttpRequestException, AESCryptoException {
        JsonObject jsonRsp = Helper.getUrlV2(this.uriGetDept(deptId));
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject jsonObj = Helper.parseJson(new String(decryptRsp));
        Dept dept = new Dept();
        JsonArray jDeptArray =  Helper.getArray("aliasList", jsonObj);
        dept.setId(Helper.getInt("id",jsonObj));
        dept.setName(Helper.getString("name",jsonObj));
        dept.setSortId(Helper.getInt("sortId",jsonObj));
        dept.setAlias(Helper.getString("alias", jsonObj));
        dept.setParentId(Helper.getInt("parentId",jsonObj));
        return  dept;
    }

    /**
     * 删除部门
     */
    public void deleteDept(int deptId) throws HttpRequestException, ParamParserException, AESCryptoException {
        Helper.getUrlV1(this.uriDeleteDept(deptId));
    }

    /**
     * 获取部门列表根据部门别名
     */
    public List<Dept> listDeptIdByAlias(String alias) throws ParamParserException, HttpRequestException, AESCryptoException {
        JsonObject jsonRsp = Helper.getUrlV2(this.uriGetDeptIdByAlias(alias));
        String cipherRsp = jsonRsp.get("encrypt").getAsString();
        byte[] decryptRsp = this.crypto.decrypt(cipherRsp);
        JsonObject jsonObj = Helper.parseJson(new String(decryptRsp));
        List<Dept> deptList = new ArrayList<Dept>();
        if(!Helper.isEmpty(alias)){
            int deptId = Helper.getInt("id", jsonObj);
            Dept dept = new Dept();
            dept.setId(deptId);
            dept.setAlias(alias);
            deptList.add(dept);
            return deptList;
        }
        JsonArray jDeptArray =  Helper.getArray("aliasList", jsonObj);
        for(JsonElement e : jDeptArray){
            if(!e.isJsonObject()){
                continue;
            }
            JsonObject jDept = e.getAsJsonObject();
            Dept dept = new Dept();
            dept.setId(Helper.getInt("id",jDept));
            if (null != alias && !alias.trim().equals("")){
                dept.setAlias(alias);
            }else{
                dept.setAlias(Helper.getString("alias", jDept));
            }
            deptList.add(dept);
        }
        return  deptList;
    }

    //------------------------------------------------
    private String uriCreateDept() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s",YdApi.SCHEME,this.host,YdApi.API_DEPT_CREATE,this.tokenClient.getToken()) ;
    }

    private String uriUpdateDept() throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s",YdApi.SCHEME,this.host,YdApi.API_DEPT_UPDATE,this.tokenClient.getToken()) ;
    }

    private String uriDeleteDept(int id) throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s&id=%s",YdApi.SCHEME,this.host,YdApi.API_DEPT_DELETE,this.tokenClient.getToken(),id) ;
    }

    private String uriListDeptChildren(int deptId) throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s&id=%d",YdApi.SCHEME,this.host,YdApi.API_DEPT_LISTCHILDREN,this.tokenClient.getToken(),deptId) ;
    }

    private String uriGetDept(int deptId) throws ParamParserException, HttpRequestException, AESCryptoException {
        return String.format("%s%s%s?accessToken=%s&id=%d",YdApi.SCHEME,this.host,YdApi.API_DEPT_GET,this.tokenClient.getToken(),deptId) ;
    }

    private String uriGetDeptIdByAlias(String alias) throws ParamParserException, HttpRequestException, AESCryptoException {
        if (null != alias && !alias.trim().equals("")){
            return String.format("%s%s%s?accessToken=%s&alias=%s",YdApi.SCHEME,this.host,YdApi.API_DEPT_GETID,this.tokenClient.getToken(),alias) ;
        }else{
            return String.format("%s%s%s?accessToken=%s",YdApi.SCHEME,this.host,YdApi.API_DEPT_GETID,this.tokenClient.getToken()) ;
        }
    }
}
