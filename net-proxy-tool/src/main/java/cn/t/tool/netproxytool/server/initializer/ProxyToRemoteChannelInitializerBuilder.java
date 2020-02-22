package cn.t.tool.netproxytool.server.initializer;

import cn.t.tool.netproxytool.promise.ConnectionResultListener;
import cn.t.tool.netproxytool.promise.MessageSender;
import cn.t.tool.netproxytool.server.handler.FetchMessageHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.handler.logging.LogLevel;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class ProxyToRemoteChannelInitializerBuilder extends NettyChannelInitializerBuilder {

    public ProxyToRemoteChannelInitializerBuilder(MessageSender messageSender, ConnectionResultListener connectionResultListener) {
        setLogLevel(LogLevel.INFO);
        setIdleState(30, 30, 30);
        addChannelInboundHandlerSupplier(() -> new FetchMessageHandler(messageSender, connectionResultListener));
    }
}
