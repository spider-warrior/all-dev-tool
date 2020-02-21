package cn.t.tool.netproxytool.server.encoder;

import cn.t.tool.netproxytool.constants.AddressType;
import cn.t.tool.netproxytool.exception.ConnectionException;
import cn.t.tool.netproxytool.model.CmdResponse;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yj
 * @since 2020-01-12 16:27
 **/
public class ServerCmdResponseEncoder extends NettyTcpEncoder<CmdResponse> {
    @Override
    protected void doEncode(ChannelHandlerContext ctx, CmdResponse cmdResponse, ByteBuf out) {
        out.writeByte(cmdResponse.getVersion());
        out.writeByte(cmdResponse.getExecutionStatus().value);
        out.writeByte(cmdResponse.getRsv());
        out.writeByte(cmdResponse.getAddressType().value);
        if(cmdResponse.getAddressType() == AddressType.IPV4) {
            out.writeBytes(cmdResponse.getTargetAddress());
        } else if(cmdResponse.getAddressType() == AddressType.IPV6) {
            out.writeBytes(cmdResponse.getTargetAddress());
        } else if(cmdResponse.getAddressType() == AddressType.DOMAIN) {
            out.writeByte(cmdResponse.getTargetAddress().length);
            out.writeBytes(cmdResponse.getTargetAddress());
        } else {
            throw new ConnectionException(String.format("不支持的地址类型: %s", cmdResponse.getAddressType()));
        }
        out.writeShort(cmdResponse.getTargetPort());
    }
}
