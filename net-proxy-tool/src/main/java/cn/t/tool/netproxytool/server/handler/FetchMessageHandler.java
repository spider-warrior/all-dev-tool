package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.constants.CmdExecutionStatus;
import cn.t.tool.netproxytool.constants.ServerConfig;
import cn.t.tool.netproxytool.promise.ChannelContextMessageSender;
import cn.t.tool.netproxytool.promise.ConnectionResultListener;
import cn.t.tool.netproxytool.promise.MessageSender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.TimeoutException;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;

/**
 * 抓取消息处理器处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 20:54
 **/
@Slf4j
public class FetchMessageHandler extends ForwardingMessageHandler {

    private ConnectionResultListener connectionResultListener;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        connectionResultListener.handle(CmdExecutionStatus.SUCCEEDED, new ChannelContextMessageSender(ctx));
    }

    /**
     * 覆盖父类方法，不关闭被代理的客户端连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("远程连接关闭:[{}:{}] <--->[{}]", ServerConfig.SERVER_HOST, ServerConfig.SERVER_PORT, ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if(cause instanceof UnknownHostException) {
            connectionResultListener.handle(CmdExecutionStatus.HOST_UNREACHABLE, new ChannelContextMessageSender(ctx));
        } else if(cause instanceof TimeoutException) {
            connectionResultListener.handle(CmdExecutionStatus.CONNECTION_REFUSED, new ChannelContextMessageSender(ctx));
        } else {
            connectionResultListener.handle(CmdExecutionStatus.GENERAL_SOCKS_SERVER_FAILURE, new ChannelContextMessageSender(ctx));
        }
    }

    public FetchMessageHandler(MessageSender messageSender, ConnectionResultListener connectionResultListener) {
        setMessageSender((messageSender));
        this.connectionResultListener = connectionResultListener;
    }
}
