package cn.t.tool.netproxytool.socks5.server.initializer;

import cn.t.tool.netproxytool.socks5.constants.Socks5ServerConfig;
import cn.t.tool.netproxytool.socks5.server.analyse.ProxyMessageAnalyser;
import cn.t.tool.netproxytool.socks5.server.encoder.ServerCmdResponseEncoder;
import cn.t.tool.netproxytool.socks5.server.encoder.ServerNegotiateResponseEncoder;
import cn.t.tool.netproxytool.socks5.server.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.socks5.server.handler.ProxyMessageHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.handler.logging.LogLevel;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class LocalToProxyChannelInitializerBuilder extends NettyChannelInitializerBuilder {

    public LocalToProxyChannelInitializerBuilder() {
        setLoggingHandlerLogLevel(Socks5ServerConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        setIdleState(Socks5ServerConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ServerConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ServerConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        setByteBufAnalyserSupplier(ProxyMessageAnalyser::new);
        addEncoderListsSupplier(ServerNegotiateResponseEncoder::new);
        addEncoderListsSupplier(ServerCmdResponseEncoder::new);
        addChannelHandlerSupplier(ProxyMessageHandler::new);
        addChannelHandlerSupplier(ForwardingMessageHandler::new);
    }
}
