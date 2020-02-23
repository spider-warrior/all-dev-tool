package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.promise.MessageSender;
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        messageSender.send(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(messageSender != null) {
            log.info("与远端断开连接，即将释放对端资源");
            messageSender.close();
        } else {
            log.info("客户端与代理断开连接，尚未获取对端句柄无法进行关闭");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //消息读取失败不能实现消息转发，断开客户端代理
        ctx.close();
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
