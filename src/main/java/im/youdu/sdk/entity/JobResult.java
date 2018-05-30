package im.youdu.sdk.entity;

public class JobResult {
    private String id;
    private String type;
    private Integer result;
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", result=" + result +
                ", desc=" + desc +
                '}';
    }
}
