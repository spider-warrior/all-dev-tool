package cn.t.tool.netproxytool.http.server.initializer;

import cn.t.tool.netproxytool.http.UserConfig;
import cn.t.tool.netproxytool.http.constants.HttpProxyServerConfig;
import cn.t.tool.netproxytool.http.server.handler.HttpProxyServerViaSocks5Handler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class HttpProxyServerViaSocks5ChannelInitializerBuilder extends NettyChannelInitializerBuilder {
    public HttpProxyServerViaSocks5ChannelInitializerBuilder(UserConfig userConfig) {
        setLoggingHandlerLogLevel(HttpProxyServerConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        setIdleState(HttpProxyServerConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpProxyServerConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpProxyServerConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        addChannelHandlerSupplier(HttpResponseEncoder::new);
        addChannelHandlerSupplier(HttpRequestDecoder::new);
        addChannelHandlerSupplier(() -> new HttpObjectAggregator(1024 * 1024));
        addChannelHandlerSupplier(() -> new HttpProxyServerViaSocks5Handler(userConfig));
    }
}
