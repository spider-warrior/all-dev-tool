package cn.t.tool.netproxytool.socks5.server.initializer;

import cn.t.tool.netproxytool.component.MessageSender;
import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.socks5.constants.Socks5ProxyConfig;
import cn.t.tool.netproxytool.socks5.server.handler.FetchMessageHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class ProxyToRemoteChannelInitializerBuilder extends NettyChannelInitializerBuilder {

    public ProxyToRemoteChannelInitializerBuilder(MessageSender messageSender, ProxyBuildResultListener proxyBuildResultListener) {
        //设置logging handler的输出级别
        setLoggingHandlerLogLevel(Socks5ProxyConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        setIdleState(Socks5ProxyConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        addChannelHandlerSupplier(() -> new FetchMessageHandler(messageSender, proxyBuildResultListener));
    }
}
