package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.promise.MessageSender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 转发消息处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:46
 **/
@Slf4j
public class ForwardingMessageHandler extends ChannelInboundHandlerAdapter {

    protected MessageSender messageSender;
    private boolean closed = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        messageSender.send(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("客户端与代理断开连接，即将释放远程连接资源");
        if(messageSender != null) {
            messageSender.close();
        }
        closed = true;
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
        if(closed) {
            messageSender.close();
        }
    }
}
