package im.youdu.sdk.client;

import com.google.gson.JsonObject;
import im.youdu.sdk.encrypt.AESCrypto;
import im.youdu.sdk.entity.YdApi;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;

import java.util.Date;

public class AppTokenClient {
    private int buin;
    private String host;
    private String appId;
    private String appAeskey;
    private String token;
    private long tokenExpireTime;

    private AESCrypto crypto;

    public AppTokenClient() {
    }

    public AppTokenClient(int buin, String host, String appId, String appAeskey) {
        this.buin = buin;
        this.host = host;
        this.appId = appId;
        this.appAeskey = appAeskey;
        this.crypto = new AESCrypto(appId, appAeskey);
    }

    public String getToken() throws AESCryptoException, ParamParserException, HttpRequestException {
        long now = new Date().getTime() / 1000;
        if (this.token != null && this.tokenExpireTime > now+5) { //预留5秒的时间
            return this.token;
        }
        this.freshToken();
        return  this.token;
    }

    private void freshToken() throws AESCryptoException, ParamParserException, HttpRequestException {
        long now = new Date().getTime() / 1000;
        String timestamp = String.format("%d", now);
        String encryptTime = this.crypto.encrypt(timestamp.getBytes());
        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", encryptTime);
        JsonObject jsonRsp = Helper.postJson(uriGetToken(), param.toString());
        String encrypt = Helper.getString("encrypt", jsonRsp);
        if (encrypt.isEmpty()) {
            throw new ParamParserException("找不到返回结果的加密字段encrypt", null);
        }
        byte[] rspBuffer = this.crypto.decrypt(encrypt);
        JsonObject jsonToken = Helper.parseJson(Helper.utf8String(rspBuffer));
        String token = Helper.getString("accessToken", jsonToken);
        int expire = Helper.getInt("expireIn", jsonToken);
        if (token.isEmpty() || expire <= 0) {
            throw new ParamParserException(String.format("返回结果不合法，token: %s, expire: %d\n", token, expire), null);
        }
        this.token = token;
        this.tokenExpireTime = now+expire;
    }

    private String uriGetToken(){
        return String.format("%s%s%s", YdApi.SCHEME , this.host, YdApi.API_GET_TOKEN);
    }

    public int getBuin() {
        return buin;
    }

    public void setBuin(int buin) {
        this.buin = buin;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppAeskey() {
        return appAeskey;
    }

    public void setAppAeskey(String appAeskey) {
        this.appAeskey = appAeskey;
    }
}


