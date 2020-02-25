package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.socks5.promise.MessageSender;
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
    private volatile boolean closed = false;

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
    //selector线程会执行该逻辑messageSender为共享资源
    public synchronized void channelInactive(ChannelHandlerContext ctx) {
        if(messageSender != null) {
            log.info("[{}]: 断开连接", ctx.channel().remoteAddress());
            messageSender.close();
        } else {
            log.warn("[{}]: 断开连接，尚未获取对端句柄无法进行关闭", ctx.channel().remoteAddress());
        }
        closed = true;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //消息读取失败不能实现消息转发，断开客户端代理
        ctx.close();
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    //远程连接线程成功后会回调执行该逻辑
    public synchronized void setMessageSender(MessageSender messageSender) {
        if(closed) {
            messageSender.close();
        } else {
            this.messageSender = messageSender;
        }
    }
}
