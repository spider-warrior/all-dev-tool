package cn.t.tool.cryptotool.constants;

public enum  EncryptType {
    SIMPLE((byte)0)
    ;

    public final byte value;

    EncryptType(byte value) {
        this.value = value;
    }
    public static EncryptType getEncryptType(byte value) {
        for(EncryptType type: values()) {
            if(type.value == value) {
                return type;
            }
        }
        return null;
    }
}
