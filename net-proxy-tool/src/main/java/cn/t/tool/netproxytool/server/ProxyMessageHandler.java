package cn.t.tool.netproxytool.server;

import cn.t.tool.netproxytool.model.TcpMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yj
 * @since 2020-01-12 16:28
 **/
public class ProxyMessageHandler extends SimpleChannelInboundHandler<TcpMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TcpMessage tcpMessage) {

    }
}
