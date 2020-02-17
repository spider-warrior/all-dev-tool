package cn.t.tool.netproxytool.socks5;

/**
 * 要请求的地址类型
 */
public enum AddressType {
    IPV4((byte) 0X01, "the address is a version-4 IP address, with a length of 4 octets"),
    DOMAIN((byte) 0X03, "the address field contains a fully-qualified domain name.  The first\n" +
        "   octet of the address field contains the number of octets of name that\n" +
        "   follow, there is no terminating NUL octet."),
    IPV6((byte) 0X04, "the address is a version-6 IP address, with a length of 16 octets.");
    byte value;
    String description;

    AddressType(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AddressType convertToAddressType(byte value) {
        for (AddressType addressType : AddressType.values()) {
            if (addressType.value == value) {
                return addressType;
            }
        }
        return null;
    }
}
