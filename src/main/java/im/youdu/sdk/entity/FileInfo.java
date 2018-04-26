package im.youdu.sdk.entity;

public class FileInfo {
    private String name;
    private String mediaId;
    private byte[] data;
    private String path;

    public FileInfo() {}

    public FileInfo(String name, String mediaId, byte[] data) {
        this.name = name;
        this.mediaId = mediaId;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int size(){
        if (null == this.data){
            return 0;
        }
        return this.data.length;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
