package im.youdu.sdk.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.util.Helper;
import im.youdu.sdk.exception.ParamParserException;

public class StatisPosition extends MessageBody implements StatisData {

    private long timestamp;

    private double longitude;

    private double latitude;

    private int type;

    private String typeName = "";

    private float accuracy;

    private String country;

    private String province;

    private String city;

    private String district;

    private String street;

    private String streetNum;

    private String cityCode;

    private String adCode;

    private String address;

    public long getTimestamp() {
        return timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getAdCode() {
        return adCode;
    }

    public String getAddress() {
        return address;
    }

    public StatisPosition() {
    }

    @Override
    public String toString() {
        return "StatisPosition{" +
                "timestamp=" + timestamp +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", type=" + type +
                ", typeName='" + typeName + '\'' +
                ", accuracy=" + accuracy +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", streetNum='" + streetNum + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", adCode='" + adCode + '\'' +
                ", address='" + address + '\'' +
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
        JsonObject jsonObj = json.getAsJsonObject();
        this.timestamp = Helper.getLong("timestamp", jsonObj);
        this.longitude = Helper.getDouble("longitude", jsonObj);
        this.latitude = Helper.getDouble("latitude", jsonObj);
        this.type = Helper.getInt("type", jsonObj);
        this.typeName = Helper.getString("typeName", jsonObj);
        this.accuracy = Helper.getFloat("accuracy", jsonObj);
        this.country = Helper.getString("country", jsonObj);
        this.province = Helper.getString("province", jsonObj);
        this.city = Helper.getString("city", jsonObj);
        this.district = Helper.getString("district", jsonObj);
        this.street = Helper.getString("street", jsonObj);
        this.streetNum = Helper.getString("streetNum", jsonObj);
        this.cityCode = Helper.getString("cityCode", jsonObj);
        this.adCode = Helper.getString("adCode", jsonObj);
        this.address = Helper.getString("address", jsonObj);
        return this;
    }

    @Override
    public String toJsonString() {
        throw new IllegalAccessError("Not Support");
    }

    @Override
    public JsonElement toJsonElement() {
        throw new IllegalAccessError("Not Support");
    }
}
