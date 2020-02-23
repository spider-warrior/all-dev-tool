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
        //设置logging handler的输出级别
        setLogLevel(LogLevel.DEBUG);
        setIdleState(6, 6, 6);
        addChannelInboundHandlerSupplier(() -> new FetchMessageHandler(messageSender, connectionResultListener));
    }
}
