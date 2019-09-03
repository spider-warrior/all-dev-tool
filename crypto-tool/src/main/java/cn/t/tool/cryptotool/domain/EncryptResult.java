package cn.t.tool.cryptotool.domain;

public class EncryptResult {
    private String key;
    private String dataFile;
    private long dataFileSize;
    private byte encryptType;
    private long createTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public long getDataFileSize() {
        return dataFileSize;
    }

    public void setDataFileSize(long dataFileSize) {
        this.dataFileSize = dataFileSize;
    }

    public byte getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(byte encryptType) {
        this.encryptType = encryptType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
