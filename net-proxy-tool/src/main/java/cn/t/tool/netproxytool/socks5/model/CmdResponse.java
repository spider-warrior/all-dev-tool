package cn.t.tool.netproxytool.socks5.model;

import lombok.Data;

/**
 * 命令响应
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-21 00:35
 **/
@Data
public class CmdResponse {
    private byte version;
    private byte executionStatus;
    private byte rsv;
    private byte socks5AddressType;
    private byte[] targetAddress;
    private short targetPort;
}
