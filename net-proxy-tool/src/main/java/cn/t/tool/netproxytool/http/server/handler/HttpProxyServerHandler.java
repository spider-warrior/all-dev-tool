package cn.t.tool.netproxytool.http.server.handler;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.http.constants.HttpProxyBuildExecutionStatus;
import cn.t.tool.netproxytool.http.server.initializer.HttpProxyServerHttpClientChannelInitializerBuilder;
import cn.t.tool.netproxytool.http.server.initializer.HttpProxyServerHttpsClientChannelInitializerBuilder;
import cn.t.tool.netproxytool.http.server.listener.HttpProxyServerClientBuildResultListener;
import cn.t.tool.netproxytool.util.ThreadUtil;
import cn.t.tool.nettytool.client.NettyTcpClient;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

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
public class HttpProxyServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        HttpMethod httpMethod = request.method();
        String host = request.headers().get(HttpHeaderNames.HOST);
        String[] elements = host.split(":");
        String targetHost = elements[0];
        int targetPort;
        if(elements.length == 1) {
            targetPort = 80;
        } else {
            targetPort= Integer.parseInt(elements[1]);
        }
        HttpVersion httpVersion = request.protocolVersion();
        if(httpMethod == HttpMethod.CONNECT) {
            buildHttpsProxy(ctx, targetHost, targetPort, httpVersion);
        } else {
            buildHttpProxy(ctx, targetHost, targetPort, httpVersion, request);
        }
    }

    private void buildHttpsProxy(ChannelHandlerContext ctx, String targetHost, int targetPort, HttpVersion httpVersion) {
        InetSocketAddress clientAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientName = clientAddress.getHostString() + ":" + clientAddress.getPort() + " -> " + targetHost + ":" + targetPort + "("+ count.incrementAndGet() +")";
        ProxyBuildResultListener proxyBuildResultListener = (status, sender) -> {
            if(HttpProxyBuildExecutionStatus.SUCCEEDED.value == status) {
                log.info("[{}:{}]: 代理创建成功, remote: {}:{}", clientAddress.getHostString(), clientAddress.getPort(), targetHost, targetPort);
                ChannelPromise promise = ctx.newPromise();
                promise.addListener(new HttpProxyServerClientBuildResultListener(ctx, sender, clientName));
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, OK), promise);
            } else {
                log.error("[{}]: 代理客户端失败, remote: {}:{}", clientAddress, targetHost, targetPort);
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, BAD_GATEWAY));
                ctx.close();
            }
        };
        NettyChannelInitializer channelInitializer = new HttpProxyServerHttpsClientChannelInitializerBuilder(ctx, proxyBuildResultListener).build();
        NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, targetHost, targetPort, channelInitializer);
        ThreadUtil.submitProxyTask(() -> nettyTcpClient.start(null));
    }

    private void buildHttpProxy(ChannelHandlerContext ctx, String targetHost, int targetPort, HttpVersion httpVersion, FullHttpRequest request) {
        InetSocketAddress clientAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientName = clientAddress.getHostString() + ":" + clientAddress.getPort() + " -> " + targetHost + ":" + targetPort + "("+ count.incrementAndGet() +")";
        FullHttpRequest proxiedRequest = request.retainedDuplicate();
        ProxyBuildResultListener proxyBuildResultListener = (status, sender) -> {
            if(HttpProxyBuildExecutionStatus.SUCCEEDED.value == status) {
                log.info("[{}:{}]: 代理创建成功, remote: {}:{}", clientAddress.getHostString(), clientAddress.getPort(), targetHost, targetPort);
                ChannelPromise promise = sender.newPromise();
                promise.addListener(new HttpProxyServerClientBuildResultListener(ctx, sender, clientName));
                EmbeddedChannel embeddedChannel = new EmbeddedChannel(new HttpRequestEncoder());
                embeddedChannel.writeOutbound(proxiedRequest);
                ByteBuf byteBuf = embeddedChannel.readOutbound();
                embeddedChannel.close();
                sender.writeAndFlush(byteBuf, promise);
            } else {
                log.error("[{}]: 代理客户端失败, remote: {}:{}", clientAddress, targetHost, targetPort);
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, BAD_GATEWAY));
                ctx.close();
            }
        };
        NettyChannelInitializer channelInitializer = new HttpProxyServerHttpClientChannelInitializerBuilder(ctx, proxyBuildResultListener).build();
        NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, targetHost, targetPort, channelInitializer);
        ThreadUtil.submitProxyTask(() -> nettyTcpClient.start(null));
    }

}