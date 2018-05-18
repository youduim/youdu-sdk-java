package im.youdu.sdk.entity;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String id;
    private String name;
    private String admin;
    private List<GroupMember> members = new ArrayList<GroupMember>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMember> members) {
        this.members = members;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
