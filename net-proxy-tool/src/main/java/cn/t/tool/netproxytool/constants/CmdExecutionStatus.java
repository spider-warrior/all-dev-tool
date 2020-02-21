package cn.t.tool.netproxytool.constants;

/**
 * CMD响应
 */
public enum CmdExecutionStatus {
    SUCCEEDED((byte) 0X00),
    GENERAL_SOCKS_SERVER_FAILURE((byte) 0X01),
    CONNECTION_NOT_ALLOWED_BY_RULESET((byte) 0X02),
    NETWORK_UNREACHABLE((byte) 0X03),
    HOST_UNREACHABLE((byte) 0X04),
    CONNECTION_REFUSED((byte) 0X05),
    TTL_EXPIRED((byte) 0X06),
    COMMAND_NOT_SUPPORTED((byte) 0X07),
    ADDRESS_TYPE_NOT_SUPPORTED((byte) 0X08),
    UNASSIGNED((byte) 0X09);

    public static CmdExecutionStatus getCmdExecutionStatus(byte value) {
        for(CmdExecutionStatus status: values()) {
            if(status.value == value) {
                return status;
            }
        }
        return UNASSIGNED;
    }

    public final byte value;

    CmdExecutionStatus(byte value) {
        this.value = value;
    }
}
