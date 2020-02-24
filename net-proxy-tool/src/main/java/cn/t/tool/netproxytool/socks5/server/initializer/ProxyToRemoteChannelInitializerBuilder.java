package cn.t.tool.netproxytool.socks5.server.initializer;

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
        setIdleState(0, 0, 6);
        addChannelInboundHandlerSupplier(() -> new FetchMessageHandler(messageSender, connectionResultListener));
    }
}
