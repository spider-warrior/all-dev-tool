package cn.t.tool.netproxytool.http.server.handler;

import cn.t.tool.netproxytool.common.promise.ChannelContextMessageSender;
import cn.t.tool.netproxytool.common.promise.ProxyBuildResultListener;
import cn.t.tool.netproxytool.http.constants.HttpProxyBuildExecutionStatus;
import cn.t.tool.netproxytool.http.server.initializer.HttpProxyClientChannelInitializerBuilder;
import cn.t.tool.netproxytool.http.server.promise.HttpProxyForwardingResultListener;
import cn.t.tool.netproxytool.util.ThreadUtil;
import cn.t.tool.nettytool.client.NettyTcpClient;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_GATEWAY;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * http请求处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-24 11:54
 **/
@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        try {
            HttpMethod httpMethod = msg.method();
            if(httpMethod == HttpMethod.CONNECT) {
                String host = msg.headers().get(HttpHeaderNames.HOST);
                String[] elements = host.split(":");
                String targetHost = elements[0];
                int targetPort;
                if(elements.length == 1) {
                    targetPort = 80;
                } else {
                    targetPort= Integer.parseInt(elements[1]);
                }
                HttpVersion httpVersion = msg.protocolVersion();
                buildProxy(ctx, targetHost, targetPort, httpVersion);
            } else {
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED, Unpooled.wrappedBuffer("not support request method".getBytes(StandardCharsets.UTF_8)));
                ctx.writeAndFlush(response);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void buildProxy(ChannelHandlerContext ctx, String targetHost, int targetPort, HttpVersion httpVersion) {
        InetSocketAddress clientAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientName = clientAddress.getHostString() + ":" + clientAddress.getPort() + " -> " + targetHost + ":" + targetPort;
        ProxyBuildResultListener proxyBuildResultListener = (status, sender) -> {
            if(HttpProxyBuildExecutionStatus.SUCCEEDED.value == status) {
                log.info("[{}]: 代理创建成功, remote: {}:{}", clientAddress, targetHost, targetPort);
                ChannelPromise promise = ctx.channel().newPromise();
                promise.addListener(new HttpProxyForwardingResultListener(ctx, sender, targetHost, targetPort));
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, OK), promise);
            } else {
                log.error("[{}]: 代理客户端失败, remote: {}:{}", clientAddress, targetHost, targetPort);
                ctx.writeAndFlush(new DefaultFullHttpResponse(httpVersion, BAD_GATEWAY));
                ctx.close();
            }
        };
        NettyChannelInitializer channelInitializer = new HttpProxyClientChannelInitializerBuilder(new ChannelContextMessageSender(ctx), proxyBuildResultListener).build();
        NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, targetHost, targetPort, channelInitializer);
        ThreadUtil.submitProxyTask(() -> nettyTcpClient.start(null));
    }
}
