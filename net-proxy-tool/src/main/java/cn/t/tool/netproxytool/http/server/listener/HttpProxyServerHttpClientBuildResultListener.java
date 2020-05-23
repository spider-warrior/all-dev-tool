package cn.t.tool.netproxytool.http.server.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
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
    private final String targetHost;
    private final int targetPort;
    private final String clientName;


    @Override
    public void operationComplete(ChannelFuture future) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)localChannelHandlerContext.channel().remoteAddress();
        if(future.isSuccess()) {
            log.info("{}([{}:{}] -> [{}:{}]): 代理请求发送成功", clientName, inetSocketAddress.getHostString(), inetSocketAddress.getPort(), targetHost, targetPort);
            ChannelPipeline channelPipeline = localChannelHandlerContext.channel().pipeline();
            HttpResponseEncoder instance = channelPipeline.get(HttpResponseEncoder.class);
            if(instance != null) {
                synchronized (localChannelHandlerContext) {
                    instance = channelPipeline.get(HttpResponseEncoder.class);
                    if(instance != null) {
                        channelPipeline.remove(HttpResponseEncoder.class);
                    }
                }
            }
        } else {
            log.error("{}([{}:{}] -> [{}:{}]): 代理请求发送失败, 即将关闭连接, 失败原因: {}", clientName, inetSocketAddress.getHostString(), inetSocketAddress.getPort(), targetHost, targetPort, future.cause());
            localChannelHandlerContext.close();
        }
    }

    public HttpProxyServerHttpClientBuildResultListener(ChannelHandlerContext localChannelHandlerContext, String targetHost, int targetPort, String clientName) {
        this.localChannelHandlerContext = localChannelHandlerContext;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.clientName = clientName;
    }
}
