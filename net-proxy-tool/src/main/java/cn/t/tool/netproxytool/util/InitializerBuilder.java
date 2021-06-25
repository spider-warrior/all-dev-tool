package cn.t.tool.netproxytool.util;

import cn.t.tool.netproxytool.event.ProxyConnectionBuildResultListener;
import cn.t.tool.netproxytool.handler.FetchMessageHandler;
import cn.t.tool.netproxytool.http.config.Socks5ClientConfig;
import cn.t.tool.netproxytool.http.constants.HttpProxyServerClientConfig;
import cn.t.tool.netproxytool.http.constants.HttpProxyServerConfig;
import cn.t.tool.netproxytool.http.server.handler.HttpProxyServerHandler;
import cn.t.tool.netproxytool.http.server.handler.HttpProxyServerViaSocks5Handler;
import cn.t.tool.netproxytool.socks5.client.analyse.MethodResponseAnalyse;
import cn.t.tool.netproxytool.socks5.client.encoder.CmdRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.MethodRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.UsernamePasswordAuthenticationRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.handler.AuthenticationResponseHandler;
import cn.t.tool.netproxytool.socks5.client.handler.CmdResponseHandler;
import cn.t.tool.netproxytool.socks5.client.handler.MethodResponseHandler;
import cn.t.tool.netproxytool.socks5.constants.Socks5ClientDaemonConfig;
import cn.t.tool.netproxytool.socks5.constants.Socks5ProxyConfig;
import cn.t.tool.netproxytool.socks5.constants.Socks5ServerDaemonConfig;
import cn.t.tool.netproxytool.socks5.server.analyse.MethodRequestAnalyse;
import cn.t.tool.netproxytool.socks5.server.encoder.CmdResponseEncoder;
import cn.t.tool.netproxytool.socks5.server.encoder.MethodResponseEncoder;
import cn.t.tool.netproxytool.socks5.server.encoder.UsernamePasswordAuthenticationResponseEncoder;
import cn.t.tool.netproxytool.socks5.server.handler.CmdRequestHandler;
import cn.t.tool.netproxytool.socks5.server.handler.MethodRequestHandler;
import cn.t.tool.netproxytool.socks5.server.handler.UsernamePasswordAuthenticationRequestHandler;
import cn.t.tool.nettytool.daemon.DaemonConfig;
import cn.t.tool.nettytool.initializer.DaemonConfigBuilder;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class InitializerBuilder {

    public static NettyChannelInitializer buildHttpProxyServerChannelInitializer() {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        daemonConfigBuilder.configLogLevel(HttpProxyServerConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        daemonConfigBuilder.configIdleHandler(HttpProxyServerConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpProxyServerConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpProxyServerConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        List<Supplier<ChannelHandler>> supplierList = new ArrayList<>();
        supplierList.add(HttpRequestDecoder::new);
        supplierList.add(HttpResponseEncoder::new);
        supplierList.add(() -> new HttpObjectAggregator(1024 * 1024));
        supplierList.add(HttpProxyServerHandler::new);
        daemonConfigBuilder.configHandler(supplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

    public static NettyChannelInitializer buildHttpProxyServerClientChannelInitializer(ChannelHandlerContext remoteChannelHandlerContext, ProxyConnectionBuildResultListener proxyConnectionBuildResultListener) {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        daemonConfigBuilder.configLogLevel(HttpProxyServerClientConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        daemonConfigBuilder.configIdleHandler(HttpProxyServerClientConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpProxyServerClientConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpProxyServerClientConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        List<Supplier<ChannelHandler>> supplierList = new ArrayList<>();
        supplierList.add(() -> new FetchMessageHandler(remoteChannelHandlerContext, proxyConnectionBuildResultListener));
        daemonConfigBuilder.configHandler(supplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

    public static NettyChannelInitializer buildHttpsProxyServerClientChannelInitializer(ChannelHandlerContext remoteChannelHandlerContext, ProxyConnectionBuildResultListener proxyConnectionBuildResultListener) {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        daemonConfigBuilder.configLogLevel(HttpProxyServerClientConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        daemonConfigBuilder.configIdleHandler(HttpProxyServerClientConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpProxyServerClientConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpProxyServerClientConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        List<Supplier<ChannelHandler>> supplierList = new ArrayList<>();
        supplierList.add(() -> new FetchMessageHandler(remoteChannelHandlerContext, proxyConnectionBuildResultListener));
        daemonConfigBuilder.configHandler(supplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

    public static NettyChannelInitializer buildHttpProxyServerViaSocks5ChannelInitializer(Socks5ClientConfig socks5ClientConfig) {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        //logging
        daemonConfigBuilder.configLogLevel(HttpProxyServerConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        //idle
        daemonConfigBuilder.configIdleHandler(HttpProxyServerConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpProxyServerConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpProxyServerConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        List<Supplier<ChannelHandler>> supplierList = new ArrayList<>();
        //http response encoder
        supplierList.add(HttpResponseEncoder::new);
        //http request decoder
        supplierList.add(HttpRequestDecoder::new);
        supplierList.add(() -> new HttpObjectAggregator(1024 * 1024));
        supplierList.add(() -> new HttpProxyServerViaSocks5Handler(socks5ClientConfig));
        daemonConfigBuilder.configHandler(supplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

    public static NettyChannelInitializer buildHttpProxyServerViaSocks5ClientChannelInitializer(ChannelHandlerContext remoteChannelHandlerContext, ProxyConnectionBuildResultListener proxyConnectionBuildResultListener, String targetHost, short targetPort, Socks5ClientConfig socks5ClientConfig) {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        //logging
        daemonConfigBuilder.configLogLevel(Socks5ClientDaemonConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        //idle
        daemonConfigBuilder.configIdleHandler(Socks5ClientDaemonConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ClientDaemonConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ClientDaemonConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        //negotiate
        daemonConfigBuilder.configByteBufAnalyser(MethodResponseAnalyse::new);
        List<Supplier<MessageToByteEncoder<?>>> encoderSupplierList = new ArrayList<>();
        //socks5 methodRequest encoder
        encoderSupplierList.add(MethodRequestEncoder::new);
        //socks5 usernamePasswordRequest encoder
        encoderSupplierList.add(UsernamePasswordAuthenticationRequestEncoder::new);
        //socks5 cmdRequest encoder
        encoderSupplierList.add(CmdRequestEncoder::new);
        daemonConfigBuilder.configM2bEncoder(encoderSupplierList);
        List<Supplier<ChannelHandler>> handlerSupplierList = new ArrayList<>();
        //negotiate response handler
        handlerSupplierList.add(() -> new MethodResponseHandler(targetHost, targetPort, socks5ClientConfig));
        //authentication response handler
        handlerSupplierList.add(AuthenticationResponseHandler::new);
        //cmd response handler
        handlerSupplierList.add(() -> new CmdResponseHandler(proxyConnectionBuildResultListener, remoteChannelHandlerContext, socks5ClientConfig));
        daemonConfigBuilder.configHandler(handlerSupplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

    public static NettyChannelInitializer buildSocks5ProxyServerChannelInitializer() {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        daemonConfigBuilder.configLogLevel(Socks5ServerDaemonConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        daemonConfigBuilder.configIdleHandler(Socks5ServerDaemonConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ServerDaemonConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ServerDaemonConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        //协商消息解析
        daemonConfigBuilder.configByteBufAnalyser(MethodRequestAnalyse::new);
        List<Supplier<MessageToByteEncoder<?>>> m2bEncoderSupplierList = new ArrayList<>();
        //协商响应消息编码
        m2bEncoderSupplierList.add(MethodResponseEncoder::new);
        //用户名密码鉴权响应编码
        m2bEncoderSupplierList.add(UsernamePasswordAuthenticationResponseEncoder::new);
        //CMD编码响应
        m2bEncoderSupplierList.add(CmdResponseEncoder::new);
        daemonConfigBuilder.configM2bEncoder(m2bEncoderSupplierList);
        List<Supplier<ChannelHandler>> handlerSupplierList = new ArrayList<>();
        //协商消息处理器
        handlerSupplierList.add(MethodRequestHandler::new);
        //用户名密码鉴权处理器
        handlerSupplierList.add(UsernamePasswordAuthenticationRequestHandler::new);
        //CMD处理器
        handlerSupplierList.add(CmdRequestHandler::new);
        daemonConfigBuilder.configHandler(handlerSupplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

    public static NettyChannelInitializer buildSocks5ProxyServerClientChannelInitializer(ChannelHandlerContext remoteChannelHandlerContext, ProxyConnectionBuildResultListener proxyConnectionBuildResultListener) {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        //logging config
        daemonConfigBuilder.configLogLevel(Socks5ProxyConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        //idle config
        daemonConfigBuilder.configIdleHandler(Socks5ProxyConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        List<Supplier<ChannelHandler>> handlerSupplierList = new ArrayList<>();
        //fetchMessageHandler
        handlerSupplierList.add(() -> new FetchMessageHandler(remoteChannelHandlerContext, proxyConnectionBuildResultListener));
        daemonConfigBuilder.configHandler(handlerSupplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

}
