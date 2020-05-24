package cn.t.tool.netproxytool.socks5.server.initializer;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.socks5.constants.Socks5ProxyConfig;
import cn.t.tool.netproxytool.socks5.server.handler.Socks5FetchMessageHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class ProxyToRemoteChannelInitializerBuilder extends NettyChannelInitializerBuilder {

    public ProxyToRemoteChannelInitializerBuilder(ChannelHandlerContext remoteChannelHandlerContext, ProxyBuildResultListener proxyBuildResultListener) {
        setLoggingHandlerLogLevel(Socks5ProxyConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        setIdleState(Socks5ProxyConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        addChannelHandlerSupplier(() -> new Socks5FetchMessageHandler(remoteChannelHandlerContext, proxyBuildResultListener));
    }
}
