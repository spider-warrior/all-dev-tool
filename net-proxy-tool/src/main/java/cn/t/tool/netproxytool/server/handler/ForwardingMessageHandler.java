package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.promise.MessageSender;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 转发消息处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:46
 **/
@Slf4j
public class ForwardingMessageHandler extends ChannelDuplexHandler {

    protected MessageSender messageSender;
    private boolean closed = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        messageSender.send(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if(messageSender != null) {
            log.info("客户端与代理断开连接，即将释放远程连接资源");
            messageSender.close();
        } else {
            log.info("客户端与代理断开连接，尚未获取远程连接句柄无法进行关闭");
            closed = true;
        }
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
