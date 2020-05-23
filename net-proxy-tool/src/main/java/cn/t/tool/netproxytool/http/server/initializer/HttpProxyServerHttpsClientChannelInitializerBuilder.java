package cn.t.tool.netproxytool.http.server.initializer;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.http.constants.HttpProxyServerClientConfig;
import cn.t.tool.netproxytool.http.server.handler.FetchMessageHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class HttpProxyServerHttpsClientChannelInitializerBuilder extends NettyChannelInitializerBuilder {
    public HttpProxyServerHttpsClientChannelInitializerBuilder(ChannelHandlerContext remoteChannelHandlerContext, ProxyBuildResultListener proxyBuildResultListener) {
        setLoggingHandlerLogLevel(HttpProxyServerClientConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        setIdleState(HttpProxyServerClientConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpProxyServerClientConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpProxyServerClientConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        addChannelHandlerSupplier(() -> new FetchMessageHandler(remoteChannelHandlerContext, proxyBuildResultListener));
    }
}
