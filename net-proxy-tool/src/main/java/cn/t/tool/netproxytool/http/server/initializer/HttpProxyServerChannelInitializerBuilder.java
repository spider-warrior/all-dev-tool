package cn.t.tool.netproxytool.http.server.initializer;

import cn.t.tool.netproxytool.http.constants.HttpProxyServerConfig;
import cn.t.tool.netproxytool.http.server.handler.HttpRequestHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class HttpProxyServerChannelInitializerBuilder extends NettyChannelInitializerBuilder {
    public HttpProxyServerChannelInitializerBuilder() {
        setLoggingHandlerLogLevel(HttpProxyServerConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        setIdleState(HttpProxyServerConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpProxyServerConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpProxyServerConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        addChannelHandlerSupplier(HttpRequestDecoder::new);
        addChannelHandlerSupplier(HttpResponseEncoder::new);
        addChannelHandlerSupplier(() -> new HttpObjectAggregator(1024 * 1024));
        addChannelHandlerSupplier(HttpRequestHandler::new);
    }
}
