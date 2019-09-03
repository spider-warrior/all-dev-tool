package cn.t.tool.cryptotool.constants;

public enum  FileType {

    文件((byte)0),
    文件夹((byte)1)
    ;

    public final byte value;

    FileType(byte value) {
        this.value = value;
    }

    public static FileType getFileType(byte value) {
        for(FileType type: values()) {
            if(type.value == value) {
                return type;
            }
        }
        return null;
    }
}
