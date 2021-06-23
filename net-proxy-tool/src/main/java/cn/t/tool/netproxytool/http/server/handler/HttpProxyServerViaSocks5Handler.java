package cn.t.tool.netproxytool.http.server.handler;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.http.config.Socks5ClientConfig;
import cn.t.tool.netproxytool.http.constants.ProxyBuildExecutionStatus;
import cn.t.tool.netproxytool.http.server.listener.ProxyServerViaSocks5ClientBuildResultListener;
import cn.t.tool.netproxytool.util.InitializerBuilder;
import cn.t.tool.netproxytool.util.ThreadUtil;
import cn.t.tool.nettytool.daemon.client.NettyTcpClient;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_GATEWAY;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * http请求处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-24 11:54
 **/
@Slf4j
public class HttpProxyServerViaSocks5Handler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Socks5ClientConfig socks5ClientConfig;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        HttpMethod httpMethod = request.method();
        String host = request.headers().get(HttpHeaderNames.HOST);
        String[] elements = host.split(":");
        String targetHost = elements[0];
        short targetPort;
        if(elements.length == 1) {
            targetPort = 80;
        } else {
            targetPort= Short.parseShort(elements[1]);
        }
        HttpVersion httpVersion = request.protocolVersion();
        if(httpMethod == HttpMethod.CONNECT) {
            buildHttpsProxy(ctx, targetHost, targetPort, httpVersion);
        } else {
            buildHttpProxy(ctx, targetHost, targetPort, httpVersion, request);
        }
    }

    private void buildHttpsProxy(ChannelHandlerContext ctx, String targetHost, short targetPort, HttpVersion httpVersion) {
        InetSocketAddress clientAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientName = clientAddress.getHostString() + ":" + clientAddress.getPort() + " -> " + targetHost + ":" + targetPort;
        ProxyBuildResultListener proxyBuildResultListener = (status, remoteChannelHandlerContext) -> {
            if(ProxyBuildExecutionStatus.SUCCEEDED.value == status) {
                log.info("[{}:{}]: 代理创建成功, remote: {}:{}", clientAddress.getHostString(), clientAddress.getPort(), targetHost, targetPort);
                ChannelPromise promise = ctx.newPromise();
                promise.addListener(new ProxyServerViaSocks5ClientBuildResultListener(ctx, remoteChannelHandlerContext, clientName));
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, OK), promise);
            } else {
                log.error("[{}]: 代理客户端失败, remote: {}:{}", clientAddress, targetHost, targetPort);
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, BAD_GATEWAY));
                ctx.close();
            }
        };
        //连接socks5服务器
        NettyChannelInitializer channelInitializer = InitializerBuilder.buildProxyServerViaSocks5ClientChannelInitializer(ctx, proxyBuildResultListener, targetHost, targetPort, socks5ClientConfig);
        NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, socks5ClientConfig.getSocks5ServerHost(), socks5ClientConfig.getSocks5ServerPort(), channelInitializer);
        ThreadUtil.submitProxyTask(nettyTcpClient::start);
    }

    private void buildHttpProxy(ChannelHandlerContext ctx, String targetHost, short targetPort, HttpVersion httpVersion, FullHttpRequest request) {
        InetSocketAddress clientAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientName = clientAddress.getHostString() + ":" + clientAddress.getPort() + " -> " + targetHost + ":" + targetPort;
        FullHttpRequest proxiedRequest = request.retainedDuplicate();
        ProxyBuildResultListener proxyBuildResultListener = (status, remoteChannelHandlerContext) -> {
            if(ProxyBuildExecutionStatus.SUCCEEDED.value == status) {
                log.info("[{}:{}]: 代理创建成功, remote: {}:{}", clientAddress.getHostString(), clientAddress.getPort(), targetHost, targetPort);
                ChannelPromise promise = remoteChannelHandlerContext.newPromise();
                promise.addListener(new ProxyServerViaSocks5ClientBuildResultListener(ctx, remoteChannelHandlerContext, clientName));
                EmbeddedChannel embeddedChannel = new EmbeddedChannel(new HttpRequestEncoder());
                embeddedChannel.writeOutbound(proxiedRequest);
                ByteBuf byteBuf = embeddedChannel.readOutbound();
                embeddedChannel.close();
                remoteChannelHandlerContext.channel().writeAndFlush(byteBuf, promise);
            } else {
                log.error("[{}]: 代理客户端失败, remote: {}:{}", clientAddress, targetHost, targetPort);
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, BAD_GATEWAY));
                ctx.close();
            }
        };
        NettyChannelInitializer channelInitializer = InitializerBuilder.buildProxyServerViaSocks5ClientChannelInitializer(ctx, proxyBuildResultListener, targetHost, targetPort, socks5ClientConfig);
        NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, socks5ClientConfig.getSocks5ServerHost(), socks5ClientConfig.getSocks5ServerPort(), channelInitializer);
        ThreadUtil.submitProxyTask(nettyTcpClient::start);
    }


    public HttpProxyServerViaSocks5Handler(Socks5ClientConfig socks5ClientConfig) {
        this.socks5ClientConfig = socks5ClientConfig;
    }
}
