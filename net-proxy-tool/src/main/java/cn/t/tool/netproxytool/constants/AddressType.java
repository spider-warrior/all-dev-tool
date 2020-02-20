package cn.t.tool.netproxytool.constants;

public enum AddressType {

    IPV4((byte) 0X01),
    DOMAIN((byte) 0X03),
    IPV6((byte) 0X04);


    public final byte value;

    AddressType(byte value) {
        this.value = value;
    }

    public static AddressType getAddressType(byte value) {
        for(AddressType type: values()) {
            if(type.value == value) {
                return type;
            }
        }
        return null;
    }
}
