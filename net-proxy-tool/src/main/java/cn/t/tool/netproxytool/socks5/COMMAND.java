package cn.t.tool.netproxytool.socks5;

/**
 * 客户端命令
 */
public enum COMMAND {
    CONNECT((byte) 0X01, "CONNECT"),
    BIND((byte) 0X02, "BIND"),
    UDP_ASSOCIATE((byte) 0X03, "UDP ASSOCIATE");

    byte value;
    String description;

    COMMAND(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public static COMMAND convertToCmd(byte value) {
        for (COMMAND cmd : COMMAND.values()) {
            if (cmd.value == value) {
                return cmd;
            }
        }
        return null;
    }
}
