package cn.t.tool.netproxytool.socks5.server.analyse;

import cn.t.tool.netproxytool.socks5.constants.AddressType;
import cn.t.tool.netproxytool.socks5.constants.Cmd;
import cn.t.tool.netproxytool.socks5.constants.Socks5Constants;
import cn.t.tool.netproxytool.exception.ConnectionException;
import cn.t.tool.netproxytool.socks5.model.CmdRequest;
import io.netty.buffer.ByteBuf;

/**
 * 客户端命令请求解析器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 20:53
 **/
public class CmdRequestAnalyse {
    public Object analyse(ByteBuf byteBuf) {
        if(byteBuf.readableBytes() < 7) {
            return null;
        }
        byte version = byteBuf.readByte();
        if(version != Socks5Constants.VERSION) {
            throw new ConnectionException(String.format("不支持的协议版本: %d", version));
        }
        byte cmdByte = byteBuf.readByte();
        Cmd cmd = Cmd.getCmd(cmdByte);
        if(cmd == null) {
            throw new ConnectionException(String.format("不支持的命令: %d", cmdByte));
        }
        byte rsv = byteBuf.readByte();
        if(rsv != 0) {
            throw new ConnectionException(String.format("rsv必须为0, 实际传输值为: %d", rsv));
        }
        byte addressTypeByte = byteBuf.readByte();
        AddressType addressType = AddressType.getAddressType(addressTypeByte);
        if(addressType == null) {
            throw new ConnectionException(String.format("不支持的地址类型: %d", addressTypeByte));
        }
        byte[] addressBytes = getAddressBytes(byteBuf, addressType);
        short port = byteBuf.readShort();
        CmdRequest cmdRequest = new CmdRequest();
        cmdRequest.setVersion(version);
        cmdRequest.setRequestCmd(cmd);
        cmdRequest.setRsv(rsv);
        cmdRequest.setAddressType(addressType);
        cmdRequest.setTargetAddress(addressBytes);
        cmdRequest.setTargetPort(port);
        return cmdRequest;
    }

    private byte[] getAddressBytes(ByteBuf byteBuf, AddressType addressType) {
        if(addressType == AddressType.IPV4) {
            byte[] bytes = new byte[4];
            byteBuf.readBytes(bytes);
            return bytes;
        } else if(addressType == AddressType.IPV6) {
            byte[] bytes = new byte[16];
            byteBuf.readBytes(bytes);
            return bytes;
        } else if(addressType == AddressType.DOMAIN) {
            byte[] bytes = new byte[byteBuf.readByte()];
            byteBuf.readBytes(bytes);
            return bytes;
        } else {
            throw new ConnectionException("解析地址失败");
        }
    }
}
