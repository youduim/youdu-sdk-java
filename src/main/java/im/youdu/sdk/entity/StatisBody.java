package im.youdu.sdk.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;
import im.youdu.sdk.exception.ParamParserException;

import java.util.ArrayList;
import java.util.List;

public class StatisBody extends MessageBody {

    public int getType() {
        return type;
    }

    private int type;

    public int getDevType() {
        return devType;
    }

    private int devType;

    public List<StatisData> getDataList() {
        return dataList;
    }

    private List<StatisData> dataList;

    public StatisBody(int type, List<StatisData> dataList) {
        this.type = type;
        this.dataList = dataList != null ? dataList : new ArrayList<StatisData>();
    }

    public StatisBody() {
        this.dataList = new ArrayList<StatisData>();
    }

    @Override
    public String toString() {
        return "StatisBody{" +
                "type=" + type +
                ", devType=" + devType +
                ", dataList=" + dataList +
                '}';
    }

    @Override
    public MessageBody fromJsonString(String json) throws ParamParserException {
        JsonObject result = Helper.parseJson(json);
        return this.fromJsonElement(result);
    }

    @Override
    public MessageBody fromJsonElement(JsonElement json) throws ParamParserException {
        if (!json.isJsonObject()) {
            throw new ParamParserException("json字段类型不匹配", null);
        }
        this.type = Helper.getInt("type", json.getAsJsonObject());
        this.devType = Helper.getInt("devType", json.getAsJsonObject());
        JsonArray array = Helper.getArray("data", json.getAsJsonObject());
        if (array == null) {
            throw new ParamParserException("json字段类型不匹配", null);
        }
        for (JsonElement elem : array) {
            StatisData data = null;
            switch (this.type) {
                case 0:
                    StatisPosition pos = new StatisPosition();
                    pos.fromJsonElement(elem);
                    data = pos;
                    break;
                default:
                    throw new ParamParserException("不合法的数据类型", null);
            }
            this.dataList.add(data);
        }
        return this;
    }

    @Override
    public String toJsonString() {
        throw new IllegalAccessError("Not support");
    }

    @Override
    public JsonElement toJsonElement() {
        throw new IllegalAccessError("Not support");
    }
}
