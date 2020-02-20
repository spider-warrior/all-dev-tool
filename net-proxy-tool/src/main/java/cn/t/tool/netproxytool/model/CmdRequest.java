package cn.t.tool.netproxytool.model;

import cn.t.tool.netproxytool.constants.AddressType;
import cn.t.tool.netproxytool.constants.Cmd;
import cn.t.tool.netproxytool.constants.Method;
import lombok.Data;

import java.util.List;

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
