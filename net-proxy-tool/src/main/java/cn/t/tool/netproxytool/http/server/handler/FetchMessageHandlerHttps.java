package cn.t.tool.netproxytool.http.server.handler;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.http.constants.HttpProxyBuildExecutionStatus;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
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
public class FetchMessageHandlerHttps extends HttpsForwardingMessageHandler implements ChannelOutboundHandler {

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

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) {
        ctx.bind(localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.disconnect(promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.close(promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.deregister(promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) {
        ctx.read();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    public FetchMessageHandlerHttps(ChannelHandlerContext remoteChannelHandlerContext, ProxyBuildResultListener proxyBuildResultListener) {
        super(remoteChannelHandlerContext);
        this.proxyBuildResultListener = proxyBuildResultListener;
    }
}
