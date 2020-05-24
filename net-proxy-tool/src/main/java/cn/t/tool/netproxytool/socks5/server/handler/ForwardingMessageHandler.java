package cn.t.tool.netproxytool.socks5.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
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
public class ForwardingMessageHandler extends ChannelDuplexHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ChannelHandlerContext remoteChannelHandlerContext;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof ByteBuf) {
            log.info("[{} : {}]: 转发消息: {} B", remoteChannelHandlerContext.channel().remoteAddress(), ctx.channel().remoteAddress(), ((ByteBuf)msg).readableBytes());
            remoteChannelHandlerContext.writeAndFlush(msg);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if(this.getClass().equals(ForwardingMessageHandler.class)) {
            log.info("[{}]: 断开连接, 释放代理资源", ctx.channel().remoteAddress());
        } else {
            log.info("[{}]: 断开连接, 关闭客户端", ctx.channel().remoteAddress());
        }
        remoteChannelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //消息读取失败不能实现消息转发，断开客户端代理
        InetSocketAddress inetSocketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        log.error("读取消息异常, 即将关闭连接: [{}:{}], 原因: {}", inetSocketAddress.getHostString(), inetSocketAddress.getPort(), cause.getMessage());
        ctx.close();
    }

    public ForwardingMessageHandler(ChannelHandlerContext remoteChannelHandlerContext) {
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
    }
}
