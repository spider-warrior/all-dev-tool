package cn.t.tool.netproxytool.http.server.handler;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.http.constants.HttpProxyBuildExecutionStatus;
import cn.t.tool.netproxytool.http.server.listener.HttpProxyServerViaSocks5HttpsClientBuildResultListener;
import cn.t.tool.netproxytool.socks5.client.initializer.HttpProxyServerViaSocks5ClientChannelInitializerBuilder;
import cn.t.tool.netproxytool.util.ThreadUtil;
import cn.t.tool.nettytool.client.NettyTcpClient;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class HttpProxyServerViaSocks5Handler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private AtomicInteger count = new AtomicInteger(0);

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
        ProxyBuildResultListener proxyBuildResultListener = (status, sender) -> {
            if(HttpProxyBuildExecutionStatus.SUCCEEDED.value == status) {
                log.info("[{}:{}]: 代理创建成功, remote: {}:{}", clientAddress.getHostString(), clientAddress.getPort(), targetHost, targetPort);
                ChannelPromise promise = ctx.newPromise();
                promise.addListener(new HttpProxyServerViaSocks5HttpsClientBuildResultListener(ctx, sender, targetHost, targetPort));
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, OK), promise);
            } else {
                log.error("[{}]: 代理客户端失败, remote: {}:{}", clientAddress, targetHost, targetPort);
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, BAD_GATEWAY));
                ctx.close();
            }
        };
        //连接socks5服务器
        String host = "127.0.0.1";
        short port = 10086;
        String clientName = clientAddress.getHostString() + ":" + clientAddress.getPort() + " -> " + targetHost + ":" + targetPort;
        NettyChannelInitializer channelInitializer = new HttpProxyServerViaSocks5ClientChannelInitializerBuilder(ctx, proxyBuildResultListener, targetHost, targetPort).build();
        NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, host, port, channelInitializer);
        ThreadUtil.submitProxyTask(() -> nettyTcpClient.start(null));
    }

    private void buildHttpProxy(ChannelHandlerContext ctx, String targetHost, short targetPort, HttpVersion httpVersion, FullHttpRequest request) {
        InetSocketAddress clientAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        int index = count.incrementAndGet();
        String clientName = clientAddress.getHostString() + ":" + clientAddress.getPort() + " -> " + targetHost + ":" + targetPort + "("+ index +")";
        FullHttpRequest proxiedRequest = request.retainedDuplicate();
        ProxyBuildResultListener proxyBuildResultListener = (status, sender) -> {
            if(HttpProxyBuildExecutionStatus.SUCCEEDED.value == status) {
                log.info("[{}:{}]: 代理创建成功, remote: {}:{}", clientAddress.getHostString(), clientAddress.getPort(), targetHost, targetPort);
                ChannelPromise promise = sender.newPromise();
                promise.addListener(new HttpProxyServerViaSocks5HttpsClientBuildResultListener(ctx, sender, targetHost, targetPort));
                sender.writeAndFlush(proxiedRequest, promise);
            } else {
                log.error("[{}]: 代理客户端失败, remote: {}:{}", clientAddress, targetHost, targetPort);
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, BAD_GATEWAY));
                ctx.close();
            }
        };
        String host = "127.0.0.1";
        short port = 10086;
        NettyChannelInitializer channelInitializer = new HttpProxyServerViaSocks5ClientChannelInitializerBuilder(ctx, proxyBuildResultListener, targetHost, targetPort).build();
        NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, host, port, channelInitializer);
        ThreadUtil.submitProxyTask(() -> nettyTcpClient.start(null));
    }

}
