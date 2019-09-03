package cn.t.tool.cryptotool.constants;

public enum  FileType {

    FILE((byte)0),
    DIRECTORY((byte)1)
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
