package cn.t.tool.netproxytool.socks5.server.initializer;

import cn.t.tool.netproxytool.socks5.constants.Socks5ProxyConfig;
import cn.t.tool.netproxytool.socks5.promise.ConnectionResultListener;
import cn.t.tool.netproxytool.socks5.promise.MessageSender;
import cn.t.tool.netproxytool.socks5.server.handler.FetchMessageHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.handler.logging.LogLevel;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class ProxyToRemoteChannelInitializerBuilder extends NettyChannelInitializerBuilder {

    public ProxyToRemoteChannelInitializerBuilder(MessageSender messageSender, ConnectionResultListener connectionResultListener) {
        //设置logging handler的输出级别
        setLogLevel(LogLevel.DEBUG);
        setIdleState(Socks5ProxyConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        addChannelInboundHandlerSupplier(() -> new FetchMessageHandler(messageSender, connectionResultListener));
    }
}
