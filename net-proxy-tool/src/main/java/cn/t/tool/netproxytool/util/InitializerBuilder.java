package cn.t.tool.netproxytool.util;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.handler.FetchMessageHandler;
import cn.t.tool.netproxytool.http.config.Socks5ClientConfig;
import cn.t.tool.netproxytool.http.constants.HttpProxyServerClientConfig;
import cn.t.tool.netproxytool.http.constants.HttpProxyServerConfig;
import cn.t.tool.netproxytool.http.server.handler.HttpProxyServerHandler;
import cn.t.tool.netproxytool.http.server.handler.HttpProxyServerViaSocks5Handler;
import cn.t.tool.netproxytool.socks5.client.analyse.NegotiateResponseAnalyse;
import cn.t.tool.netproxytool.socks5.client.encoder.CmdRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.MethodRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.UsernamePasswordAuthenticationRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.handler.AuthenticationResponseHandler;
import cn.t.tool.netproxytool.socks5.client.handler.CmdResponseHandler;
import cn.t.tool.netproxytool.socks5.client.handler.NegotiateResponseHandler;
import cn.t.tool.netproxytool.socks5.constants.Socks5ClientDaemonConfig;
import cn.t.tool.netproxytool.socks5.constants.Socks5ProxyConfig;
import cn.t.tool.netproxytool.socks5.constants.Socks5ServerDaemonConfig;
import cn.t.tool.netproxytool.socks5.server.analyse.NegotiateRequestAnalyse;
import cn.t.tool.netproxytool.socks5.server.encoder.CmdResponseEncoder;
import cn.t.tool.netproxytool.socks5.server.encoder.NegotiateResponseEncoder;
import cn.t.tool.netproxytool.socks5.server.encoder.UsernamePasswordAuthenticationResponseEncoder;
import cn.t.tool.netproxytool.socks5.server.handler.CmdRequestHandler;
import cn.t.tool.netproxytool.socks5.server.handler.NegotiateRequestHandler;
import cn.t.tool.netproxytool.socks5.server.handler.UsernamePasswordAuthenticationRequestHandler;
import cn.t.tool.nettytool.daemon.DaemonConfig;
import cn.t.tool.nettytool.initializer.DaemonConfigBuilder;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class InitializerBuilder {

    public static NettyChannelInitializer buildHttpProxyServerViaSocks5ChannelInitializer(Socks5ClientConfig socks5ClientConfig) {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        daemonConfigBuilder.configLogLevel(HttpProxyServerConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        daemonConfigBuilder.configIdleHandler(HttpProxyServerConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpProxyServerConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpProxyServerConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        List<Supplier<ChannelHandler>> supplierList = new ArrayList<>();
        supplierList.add(HttpResponseEncoder::new);
        supplierList.add(HttpRequestDecoder::new);
        supplierList.add(() -> new HttpObjectAggregator(1024 * 1024));
        supplierList.add(() -> new HttpProxyServerViaSocks5Handler(socks5ClientConfig));
        daemonConfigBuilder.configHandler(supplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

    public static NettyChannelInitializer buildHttpProxyServerClientChannelInitializer(ChannelHandlerContext remoteChannelHandlerContext, ProxyBuildResultListener proxyBuildResultListener) {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        daemonConfigBuilder.configLogLevel(HttpProxyServerClientConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        daemonConfigBuilder.configIdleHandler(HttpProxyServerClientConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpProxyServerClientConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpProxyServerClientConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        List<Supplier<ChannelHandler>> supplierList = new ArrayList<>();
        supplierList.add(() -> new FetchMessageHandler(remoteChannelHandlerContext, proxyBuildResultListener));
        daemonConfigBuilder.configHandler(supplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

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

    public static NettyChannelInitializer buildProxyServerViaSocks5ClientChannelInitializer(ChannelHandlerContext remoteChannelHandlerContext, ProxyBuildResultListener proxyBuildResultListener, String targetHost, short targetPort, Socks5ClientConfig socks5ClientConfig) {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        daemonConfigBuilder.configLogLevel(Socks5ClientDaemonConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        daemonConfigBuilder.configIdleHandler(Socks5ClientDaemonConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ClientDaemonConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ClientDaemonConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        daemonConfigBuilder.configByteBufAnalyser(NegotiateResponseAnalyse::new);
        List<Supplier<MessageToByteEncoder<?>>> encoderSupplierList = new ArrayList<>();
        encoderSupplierList.add(MethodRequestEncoder::new);
        encoderSupplierList.add(UsernamePasswordAuthenticationRequestEncoder::new);
        encoderSupplierList.add(CmdRequestEncoder::new);
        daemonConfigBuilder.configEncoder(encoderSupplierList);
        List<Supplier<ChannelHandler>> handlerSupplierList = new ArrayList<>();
        handlerSupplierList.add(HttpRequestEncoder::new);
        handlerSupplierList.add(() -> new NegotiateResponseHandler(targetHost, targetPort, socks5ClientConfig));
        handlerSupplierList.add(AuthenticationResponseHandler::new);
        handlerSupplierList.add(() -> new CmdResponseHandler(proxyBuildResultListener, remoteChannelHandlerContext, socks5ClientConfig));
        daemonConfigBuilder.configHandler(handlerSupplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

    public static NettyChannelInitializer buildLocalToProxyChannelInitializer() {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        daemonConfigBuilder.configLogLevel(Socks5ServerDaemonConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        daemonConfigBuilder.configIdleHandler(Socks5ServerDaemonConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ServerDaemonConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ServerDaemonConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        daemonConfigBuilder.configByteBufAnalyser(NegotiateRequestAnalyse::new);
        List<Supplier<MessageToByteEncoder<?>>> encoderSupplierList = new ArrayList<>();
        encoderSupplierList.add(NegotiateResponseEncoder::new);
        encoderSupplierList.add(UsernamePasswordAuthenticationResponseEncoder::new);
        encoderSupplierList.add(CmdResponseEncoder::new);
        daemonConfigBuilder.configEncoder(encoderSupplierList);
        List<Supplier<ChannelHandler>> handlerSupplierList = new ArrayList<>();
        handlerSupplierList.add(NegotiateRequestHandler::new);
        handlerSupplierList.add(UsernamePasswordAuthenticationRequestHandler::new);
        handlerSupplierList.add(CmdRequestHandler::new);
        daemonConfigBuilder.configHandler(handlerSupplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

    public static NettyChannelInitializer buildProxyToRemoteChannelInitializer(ChannelHandlerContext remoteChannelHandlerContext, ProxyBuildResultListener proxyBuildResultListener) {
        DaemonConfigBuilder daemonConfigBuilder = DaemonConfigBuilder.newInstance();
        daemonConfigBuilder.configLogLevel(Socks5ProxyConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        daemonConfigBuilder.configIdleHandler(Socks5ProxyConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        List<Supplier<ChannelHandler>> handlerSupplierList = new ArrayList<>();
        handlerSupplierList.add(() -> new FetchMessageHandler(remoteChannelHandlerContext, proxyBuildResultListener));
        daemonConfigBuilder.configHandler(handlerSupplierList);
        DaemonConfig daemonConfig = daemonConfigBuilder.build();
        return new NettyChannelInitializer(daemonConfig);
    }

}
