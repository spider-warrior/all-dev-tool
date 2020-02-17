package cn.t.tool.netproxytool.socks5;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端认证方法
 */
public enum METHOD {
    NO_AUTHENTICATION_REQUIRED((byte) 0X00, (byte) 0X00, "NO AUTHENTICATION REQUIRED"),
    GSSAPI((byte) 0X01, (byte) 0X01, "GSSAPI"),
    USERNAME_PASSWORD((byte) 0X02, (byte) 0X02, " USERNAME/PASSWORD"),
    IANA_ASSIGNED((byte) 0X03, (byte) 0X07, "IANA ASSIGNED"),
    RESERVED_FOR_PRIVATE_METHODS((byte) 0X80, (byte) 0XFE, "RESERVED FOR PRIVATE METHODS"),
    NO_ACCEPTABLE_METHODS((byte) 0XFF, (byte) 0XFF, "NO ACCEPTABLE METHODS");

    public final byte rangeStart;
    public final byte rangeEnd;
    public final String description;

    METHOD(byte rangeStart, byte rangeEnd, String description) {
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.description = description;
    }

    public boolean isMe(byte value) {
        return value >= rangeStart && value <= rangeEnd;
    }

    public static List<METHOD> convertToMethod(byte[] methodValues) {
        List<METHOD> methodList = new ArrayList<>();
        for (byte b : methodValues) {
            for (METHOD method : METHOD.values()) {
                if (method.isMe(b)) {
                    methodList.add(method);
                    break;
                }
            }
        }
        return methodList;
    }
}
