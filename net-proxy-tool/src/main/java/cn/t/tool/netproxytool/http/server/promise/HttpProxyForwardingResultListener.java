package cn.t.tool.netproxytool.http.server.promise;

import cn.t.tool.netproxytool.http.server.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.http.server.handler.HttpRequestHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * http代理结果监听器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-27 15:42
 **/
@Slf4j
public class HttpProxyForwardingResultListener implements ChannelFutureListener {

    private ChannelHandlerContext localChannelHandlerContext;
    private ChannelHandlerContext remoteChannelHandlerContext;
    private String targetHost;
    private int targetPort;

    @Override
    public void operationComplete(ChannelFuture future) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)localChannelHandlerContext.channel().remoteAddress();
        if(future.isSuccess()) {
            log.info("[{}:{}] -> [{}:{}]: 通知客户端代理已就位成功, 目的地址:", inetSocketAddress.getHostString(), inetSocketAddress.getPort(), targetHost, targetPort);
            //已经通知客户端代理成功, 切换handler
            ChannelPipeline channelPipeline = localChannelHandlerContext.channel().pipeline();
            channelPipeline.remove(HttpServerCodec.class);
            channelPipeline.remove(HttpObjectAggregator.class);
            channelPipeline.remove(HttpRequestHandler.class);
            channelPipeline.addLast("proxy-fording-handler", new ForwardingMessageHandler(remoteChannelHandlerContext));
        } else {
            log.error("[{}:{}] -> [{}:{}]: 通知客户端代理已就位失败, 即将关闭连接, 失败原因: {}", inetSocketAddress.getHostString(), inetSocketAddress.getPort(), targetHost, targetPort, future.cause());
            localChannelHandlerContext.close();
        }
    }

    public HttpProxyForwardingResultListener(ChannelHandlerContext localChannelHandlerContext, ChannelHandlerContext remoteChannelHandlerContext, String targetHost, int targetPort) {
        this.localChannelHandlerContext = localChannelHandlerContext;
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }
}
