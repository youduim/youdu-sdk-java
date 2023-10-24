package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @Author H.
 * @Date 2023/10/20 11:40
 * @Version 1.0
 * @Desc
 */
public class UserAttr {

    public static JsonArray buildArr(JsonElement...attrs) {
        JsonArray arr = new JsonArray();
        for (int i = 0; i < attrs.length; i++) {
            arr.add(attrs[i]);
        }
        return arr;
    }
    public static JsonObject build(String name, String value) {
        JsonObject ele = new JsonObject();
        ele.addProperty("name", name);
        ele.addProperty("value", value);
        return ele;
    }
}
