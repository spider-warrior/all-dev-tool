package cn.t.tool.netproxytool.constants;

/**
 * CMD响应
 */
public enum CmdExecutionStatus {
    SUCCEEDED((byte) 0X00, (byte) 0X00),
    GENERAL_SOCKS_SERVER_FAILURE((byte) 0X01, (byte) 0X01),
    CONNECTION_NOT_ALLOWED_BY_RULESET((byte) 0X02, (byte) 0X02),
    NETWORK_UNREACHABLE((byte) 0X03, (byte) 0X03),
    HOST_UNREACHABLE((byte) 0X04, (byte) 0X04),
    CONNECTION_REFUSED((byte) 0X05, (byte) 0X05),
    TTL_EXPIRED((byte) 0X06, (byte) 0X06),
    COMMAND_NOT_SUPPORTED((byte) 0X07, (byte) 0X07),
    ADDRESS_TYPE_NOT_SUPPORTED((byte) 0X08, (byte) 0X08),
    UNASSIGNED((byte) 0X09, (byte) 0XFF);

    public final byte rangeStart;
    public final byte rangeEnd;

    CmdExecutionStatus(byte rangeStart, byte rangeEnd) {
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
    }
}
