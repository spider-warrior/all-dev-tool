package cn.t.tool.netproxytool.handler;

import cn.t.tool.netproxytool.event.ProxyConnectionBuildResultListener;
import cn.t.tool.netproxytool.http.constants.ProxyBuildExecutionStatus;
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

    private final ProxyConnectionBuildResultListener proxyConnectionBuildResultListener;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        proxyConnectionBuildResultListener.handle(ProxyBuildExecutionStatus.SUCCEEDED.value, ctx);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                //连接失败处理
                log.info("[{}]: 连接失败, 回调监听器", ctx.channel().remoteAddress());
                proxyConnectionBuildResultListener.handle(ProxyBuildExecutionStatus.FAILED.value, ctx);
            }
        }));
    }

    public FetchMessageHandler(ChannelHandlerContext remoteChannelHandlerContext, ProxyConnectionBuildResultListener proxyConnectionBuildResultListener) {
        super(remoteChannelHandlerContext);
        this.proxyConnectionBuildResultListener = proxyConnectionBuildResultListener;
    }
}
