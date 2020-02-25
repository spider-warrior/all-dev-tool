package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.socks5.constants.CmdExecutionStatus;
import cn.t.tool.netproxytool.socks5.promise.ChannelContextMessageSender;
import cn.t.tool.netproxytool.socks5.promise.ConnectionResultListener;
import cn.t.tool.netproxytool.socks5.promise.MessageSender;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

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
        log.info("[{}]: 连接成功, 回调代客户端听器", ctx.channel().remoteAddress());
        connectionResultListener.handle(CmdExecutionStatus.SUCCEEDED, new ChannelContextMessageSender(ctx));
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) {
        log.info("[{}]: 断开连接, 关闭代理客户端", ctx.channel().remoteAddress());
       messageSender.close();
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                //连接失败处理
                log.info("[{}]: 连接失败, 回调客户端监听器", ctx.channel().remoteAddress());
                connectionResultListener.handle(CmdExecutionStatus.CONNECTION_REFUSED, new ChannelContextMessageSender(ctx));
            }
        }));
    }

    public FetchMessageHandler(MessageSender messageSender, ConnectionResultListener connectionResultListener) {
        setMessageSender((messageSender));
        this.connectionResultListener = connectionResultListener;
    }
}
