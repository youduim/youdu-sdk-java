package im.youdu.sdk.entity;

public class AvatarInfo {
    private byte[] data;
    private String name;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int size(){
        if (null == this.data){
            return 0;
        }
        return this.data.length;
    }
}
