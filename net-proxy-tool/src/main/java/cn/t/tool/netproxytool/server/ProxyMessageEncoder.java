package cn.t.tool.netproxytool.server;

import cn.t.tool.netproxytool.model.TcpMessage;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yj
 * @since 2020-01-12 16:27
 **/
public class ProxyMessageEncoder extends NettyTcpEncoder<TcpMessage> {
    @Override
    protected void doEncode(ChannelHandlerContext ctx, TcpMessage tcpMessage, ByteBuf out) {

    }
}
