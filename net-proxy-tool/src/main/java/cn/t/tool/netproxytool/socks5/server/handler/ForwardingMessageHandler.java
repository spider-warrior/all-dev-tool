package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.component.MessageSender;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 转发消息处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:46
 **/
public class ForwardingMessageHandler extends ChannelDuplexHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected MessageSender messageSender;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof ByteBuf) {
            log.info("[{}]: 转发消息: {} B", ctx.channel().remoteAddress(), ((ByteBuf)msg).readableBytes());
            messageSender.send(msg);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if(messageSender != null) {
            if(this.getClass().equals(ForwardingMessageHandler.class)) {
                log.info("[{}]: 断开连接, 释放代理资源", ctx.channel().remoteAddress());
            } else {
                log.info("[{}]: 断开连接, 关闭客户端", ctx.channel().remoteAddress());
            }
            messageSender.close();
        } else {
            log.warn("[{}]: 断开连接，没有获取代理句柄无法释放代理资源", ctx.channel().remoteAddress());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //消息读取失败不能实现消息转发，断开客户端代理
        ctx.close();
    }

    public ForwardingMessageHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
