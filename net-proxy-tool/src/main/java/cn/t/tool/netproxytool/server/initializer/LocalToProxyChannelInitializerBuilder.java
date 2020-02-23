package cn.t.tool.netproxytool.server.initializer;

import cn.t.tool.netproxytool.server.analyse.ProxyMessageAnalyser;
import cn.t.tool.netproxytool.server.encoder.ServerCmdResponseEncoder;
import cn.t.tool.netproxytool.server.encoder.ServerNegotiateResponseEncoder;
import cn.t.tool.netproxytool.server.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.server.handler.ProxyMessageHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.handler.logging.LogLevel;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class LocalToProxyChannelInitializerBuilder extends NettyChannelInitializerBuilder {

    public LocalToProxyChannelInitializerBuilder() {
        //设置logging handler的输出级别
        setLogLevel(LogLevel.DEBUG);
        setIdleState(30, 30, 30);
        setByteBufAnalyserSupplier(ProxyMessageAnalyser::new);
        addEncoderListsSupplier(ServerNegotiateResponseEncoder::new);
        addEncoderListsSupplier(ServerCmdResponseEncoder::new);
        addChannelInboundHandlerSupplier(ProxyMessageHandler::new);
        addChannelInboundHandlerSupplier(ForwardingMessageHandler::new);
    }
}
