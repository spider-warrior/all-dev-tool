package cn.t.tool.netproxytool.promise;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * channel context message sender
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-23 15:14
 **/
@Slf4j
public class ChannelContextMessageSender implements MessageSender {

    private ChannelHandlerContext channelHandlerContext;

    @Override
    public void send(Object object) {
        channelHandlerContext.writeAndFlush(object);
    }

    @Override
    public void close() {
        channelHandlerContext.close();
    }

    public ChannelContextMessageSender(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }
}
