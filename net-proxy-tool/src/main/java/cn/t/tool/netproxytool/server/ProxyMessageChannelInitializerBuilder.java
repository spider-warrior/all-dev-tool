package cn.t.tool.netproxytool.server;

import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.handler.logging.LogLevel;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class ProxyMessageChannelInitializerBuilder extends NettyChannelInitializerBuilder {

    public ProxyMessageChannelInitializerBuilder() {
        setLogLevel(LogLevel.INFO);
        setIdleState(180, 180, 180);
        setByteBufAnalyserSupplier(ProxyMessageAnalyser::new);
        addEncoderListsSupplier(ProxyMessageEncoder::new);
        addSimpleChannelInboundHandlerListSupplier(ProxyMessageHandler::new);
    }
}
