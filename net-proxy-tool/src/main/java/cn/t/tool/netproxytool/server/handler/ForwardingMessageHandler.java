package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.promise.MessageSender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 转发消息处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:46
 **/
public class ForwardingMessageHandler extends ChannelInboundHandlerAdapter {

    private MessageSender messageSender;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(messageSender != null) {
            messageSender.send(msg);
        }
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
