package im.youdu.sdk.entity;

public class YDApp {
    private int buin;
    private String host;

    private String appId;
    private String appName;
    private String token;
    private String appAesKey;

    public YDApp() {
    }

    public YDApp(int buin, String srvHost, String appName, String appId, String token, String appAesKey) {
        this.buin = buin;
        this.host = srvHost;
        this.appName = appName;
        this.appId = appId;
        this.token = token;
        this.appAesKey = appAesKey;
    }

    public int getBuin() {
        return buin;
    }

    public void setBuin(int buin) {
        this.buin = buin;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppAesKey() {
        return appAesKey;
    }

    public void setAppAesKey(String appAesKey) {
        this.appAesKey = appAesKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAppName() {
        if(null == appName || "".equals(appName)){
            return appId;
        }
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
