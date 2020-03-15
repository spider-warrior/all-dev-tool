package cn.t.tool.netproxytool.socks5.client.initializer;

import cn.t.tool.netproxytool.socks5.client.analyse.NegotiateResponseAnalyse;
import cn.t.tool.netproxytool.socks5.client.encoder.CmdRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.MethodRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.UsernamePasswordAuthenticationRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.handler.AuthenticationResponseHandler;
import cn.t.tool.netproxytool.socks5.client.handler.NegotiateResponseHandler;
import cn.t.tool.netproxytool.socks5.constants.Socks5ClientConfig;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;

/**
 * 客户端代理Initializer构建器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-03-14 18:45
 **/
public class ClientChannelInitializerBuilder extends NettyChannelInitializerBuilder {
    public ClientChannelInitializerBuilder(String host, short port) {
        setLoggingHandlerLogLevel(Socks5ClientConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        setIdleState(Socks5ClientConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ClientConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ClientConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        setByteBufAnalyserSupplier(NegotiateResponseAnalyse::new);
        addEncoderListsSupplier(MethodRequestEncoder::new);
        addEncoderListsSupplier(UsernamePasswordAuthenticationRequestEncoder::new);
        addEncoderListsSupplier(CmdRequestEncoder::new);
        addChannelHandlerSupplier(() -> new NegotiateResponseHandler(host, port));
        addChannelHandlerSupplier(AuthenticationResponseHandler::new);
    }
}
