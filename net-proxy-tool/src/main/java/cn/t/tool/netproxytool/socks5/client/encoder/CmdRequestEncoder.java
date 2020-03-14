package cn.t.tool.netproxytool.socks5.client.encoder;

import cn.t.tool.netproxytool.socks5.constants.Socks5AddressType;
import cn.t.tool.netproxytool.socks5.model.CmdRequest;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * CmdRequest编码器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-03-14 19:21
 **/
public class CmdRequestEncoder extends NettyTcpEncoder<CmdRequest> {

    @Override
    protected void doEncode(ChannelHandlerContext ctx, CmdRequest cmdRequest, ByteBuf out) {
        //version
        out.writeByte(cmdRequest.getVersion());
        //cmd
        out.writeByte(cmdRequest.getRequestSocks5Cmd().value);
        //rsv
        out.writeByte(cmdRequest.getRsv());
        //address type
        out.writeByte(cmdRequest.getSocks5AddressType().value);
        //dst addr
        if(Socks5AddressType.DOMAIN == cmdRequest.getSocks5AddressType()) {
            out.writeByte(cmdRequest.getTargetAddress().length);
            out.writeBytes(cmdRequest.getTargetAddress());
        } else {
            out.writeBytes(cmdRequest.getTargetAddress());
        }
        //dst port
        out.writeShort(cmdRequest.getTargetPort());
    }
}
