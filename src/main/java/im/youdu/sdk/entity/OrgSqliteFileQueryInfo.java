package im.youdu.sdk.entity;

public class OrgSqliteFileQueryInfo {
    private String fileId;
    private long version;
    private boolean generatingNew;

    public OrgSqliteFileQueryInfo(){}

    public OrgSqliteFileQueryInfo(String fileId, long version, boolean generatingNew){
        this.fileId = fileId;
        this.version = version;
        this.generatingNew = generatingNew;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public boolean isGeneratingNew() {
        return generatingNew;
    }

    public void setGeneratingNew(boolean generatingNew) {
        this.generatingNew = generatingNew;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "OrgFileQueryInfo{" +
                "fileId='" + fileId + '\'' +
                ", version=" + version +
                ", generatingNew=" + generatingNew +
                '}';
    }
}
