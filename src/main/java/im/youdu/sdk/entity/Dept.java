package im.youdu.sdk.entity;

public class Dept {
    private Integer id;
    private String name;
    private Integer parentId;
    private Integer sortId;
    private String alias;

    public Dept() {
    }

    public Dept(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Dept(int id, String name, int parentId, int sortId, String alias) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.sortId = sortId;
        this.alias = alias;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", sortId=" + sortId +
                ", alias=" + alias +
                '}';
    }
}
