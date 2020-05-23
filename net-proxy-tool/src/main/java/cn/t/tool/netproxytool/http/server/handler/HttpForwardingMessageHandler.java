package cn.t.tool.netproxytool.http.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 转发消息处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:46
 **/
public class HttpForwardingMessageHandler extends ChannelInboundHandlerAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ChannelHandlerContext remoteChannelHandlerContext;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        if(msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest)msg;
            log.info("[{}:{}]: 转发消息, method: {}, uri: {}, version: {}", inetSocketAddress.getHostString(), inetSocketAddress.getPort(), request.method(), request.uri(), request.protocolVersion());
        } else if(msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf)msg;
            log.info("[{}:{}]: 转发消息, size: {}", inetSocketAddress.getHostString(), inetSocketAddress.getPort(), buf.readableBytes());
        }
        remoteChannelHandlerContext.writeAndFlush(msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        log.info("[{}:{}]: 断开连接", inetSocketAddress.getHostString(), inetSocketAddress.getPort());
        remoteChannelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //消息读取失败不能实现消息转发，断开客户端代理
        InetSocketAddress inetSocketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        log.error("读取消息异常, 即将关闭连接: [{}:{}], 原因: {}", inetSocketAddress.getHostString(), inetSocketAddress.getPort(), cause.getMessage());
        ctx.close();
    }

    public HttpForwardingMessageHandler(ChannelHandlerContext remoteChannelHandlerContext) {
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
    }
}
