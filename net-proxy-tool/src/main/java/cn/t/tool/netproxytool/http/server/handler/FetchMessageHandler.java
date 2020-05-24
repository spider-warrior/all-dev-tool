package cn.t.tool.netproxytool.http.server.handler;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.http.constants.HttpProxyBuildExecutionStatus;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

/**
 * 抓取消息处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 20:54
 **/
@Slf4j
public class FetchMessageHandler extends ForwardingMessageHandler {

    private ProxyBuildResultListener proxyBuildResultListener;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("[{}]: 连接成功, 回调监听器", ctx.channel().remoteAddress());
        proxyBuildResultListener.handle(HttpProxyBuildExecutionStatus.SUCCEEDED.value, ctx);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                log.info("[{}]: 连接失败, 回调监听器", ctx.channel().remoteAddress());
                proxyBuildResultListener.handle(HttpProxyBuildExecutionStatus.FAILED.value, ctx);
            }
        }));
    }

    public FetchMessageHandler(ChannelHandlerContext remoteChannelHandlerContext, ProxyBuildResultListener proxyBuildResultListener) {
        super(remoteChannelHandlerContext);
        this.proxyBuildResultListener = proxyBuildResultListener;
    }
}
