package im.youdu.sdk.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import im.youdu.sdk.entity.DeptExpandInfo;
import im.youdu.sdk.entity.OrgSqliteFileQueryInfo;

public class JsonUtil {

    private JsonUtil(){}

    public static DeptExpandInfo parseDeptExpandJson(JsonObject jsonObject){
        DeptExpandInfo info;
        Gson gson = new Gson();
        info = gson.fromJson(jsonObject, DeptExpandInfo.class);
        return info;
    }

    public static OrgSqliteFileQueryInfo parseOrgSqliteFileQueryJson(JsonObject jsonObject){
        OrgSqliteFileQueryInfo info;
        Gson gson = new Gson();
        info = gson.fromJson(jsonObject, OrgSqliteFileQueryInfo.class);
        return info;
    }

}
