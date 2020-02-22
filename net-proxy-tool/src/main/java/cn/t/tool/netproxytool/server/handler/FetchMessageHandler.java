package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.constants.CmdExecutionStatus;
import cn.t.tool.netproxytool.promise.ConnectionResultListener;
import cn.t.tool.netproxytool.promise.MessageSender;
import io.netty.channel.ChannelHandlerContext;

/**
 * 抓取消息处理器处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 20:54
 **/
public class FetchMessageHandler extends ForwardingMessageHandler {

    private ConnectionResultListener connectionResultListener;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        connectionResultListener.handle(CmdExecutionStatus.SUCCEEDED, ctx::writeAndFlush);
    }

    public FetchMessageHandler(MessageSender messageSender, ConnectionResultListener connectionResultListener) {
        setMessageSender((messageSender));
        this.connectionResultListener = connectionResultListener;
    }
}
