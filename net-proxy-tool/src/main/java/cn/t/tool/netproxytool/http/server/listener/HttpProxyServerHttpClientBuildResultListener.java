package cn.t.tool.netproxytool.http.server.listener;

import cn.t.tool.netproxytool.http.server.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.http.server.handler.HttpProxyServerHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * http代理结果监听器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-27 15:42
 **/
@Slf4j
public class HttpProxyServerHttpClientBuildResultListener implements ChannelFutureListener {

    private final ChannelHandlerContext localChannelHandlerContext;
    private final ChannelHandlerContext remoteChannelHandlerContext;
    private final String targetHost;
    private final int targetPort;
    private final String clientName;


    @Override
    public void operationComplete(ChannelFuture future) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)localChannelHandlerContext.channel().remoteAddress();
        if(future.isSuccess()) {
            log.info("{}([{}:{}] -> [{}:{}]): 代理请求发送成功", clientName, inetSocketAddress.getHostString(), inetSocketAddress.getPort(), targetHost, targetPort);
            ChannelPipeline channelPipeline = localChannelHandlerContext.channel().pipeline();
            channelPipeline.remove(HttpResponseEncoder.class);
            channelPipeline.remove(HttpRequestDecoder.class);
            channelPipeline.remove(HttpObjectAggregator.class);
            channelPipeline.remove(HttpProxyServerHandler.class);
            channelPipeline.addLast("proxy-fording-handler", new ForwardingMessageHandler(remoteChannelHandlerContext));
        } else {
            log.error("{}([{}:{}] -> [{}:{}]): 代理请求发送失败, 即将关闭连接, 失败原因: {}", clientName, inetSocketAddress.getHostString(), inetSocketAddress.getPort(), targetHost, targetPort, future.cause().getMessage());
            localChannelHandlerContext.close();
        }
    }

    public HttpProxyServerHttpClientBuildResultListener(ChannelHandlerContext localChannelHandlerContext, ChannelHandlerContext remoteChannelHandlerContext, String targetHost, int targetPort, String clientName) {
        this.localChannelHandlerContext = localChannelHandlerContext;
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.clientName = clientName;
    }
}
