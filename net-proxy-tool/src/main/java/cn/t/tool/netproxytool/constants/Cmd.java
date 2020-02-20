package cn.t.tool.netproxytool.constants;

/**
 * 命令
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 23:14
 **/
public enum Cmd {

    CONNECT((byte) 0X01),
    BIND((byte) 0X02),
    UDP_ASSOCIATE((byte) 0X03);

    public final byte value;

    Cmd(byte value) {
        this.value = value;
    }

    public static Cmd getCmd(byte value) {
        for(Cmd cmd: values()) {
            if(cmd.value == value) {
                return cmd;
            }
        }
        return null;
    }
}
