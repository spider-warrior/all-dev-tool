package cn.t.tool.netproxytool.socks5.model;

import cn.t.tool.netproxytool.socks5.constants.AddressType;
import cn.t.tool.netproxytool.socks5.constants.Cmd;
import lombok.Data;

/**
 * 命令请求
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 20:13
 **/
@Data
public class CmdRequest {
    private byte version;
    private Cmd requestCmd;
    private byte rsv;
    private AddressType addressType;
    private byte[] targetAddress;
    private short targetPort;
}
