package cn.t.tool.netproxytool.http.server.listener;

import cn.t.tool.netproxytool.http.server.handler.HttpForwardingMessageHandler;
import cn.t.tool.netproxytool.http.server.handler.HttpRequestAsSocket5ClientMsgHandler;
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
public class HttpProxyForwardingResultListener implements ChannelFutureListener {

    private ChannelHandlerContext localChannelHandlerContext;
    private ChannelHandlerContext remoteChannelHandlerContext;
    private String targetHost;
    private int targetPort;
    private int id;

    @Override
    public void operationComplete(ChannelFuture future) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)localChannelHandlerContext.channel().remoteAddress();
        if(future.isSuccess()) {
            log.info("[{}:{}] -> [{}:{}]: 代理请求发送成功", inetSocketAddress.getHostString(), inetSocketAddress.getPort(), targetHost, targetPort);
            //同一个连接首次执行
            if(id == 0) {
                ChannelPipeline channelPipeline = localChannelHandlerContext.channel().pipeline();
                //HttpResponseEncoder会误处理写回来的数据
                channelPipeline.remove(HttpResponseEncoder.class);
                channelPipeline.remove(HttpRequestDecoder.class);
                channelPipeline.remove(HttpObjectAggregator.class);
                channelPipeline.remove(HttpRequestAsSocket5ClientMsgHandler.class);
                channelPipeline.addLast("proxy-fording-handler", new HttpForwardingMessageHandler(remoteChannelHandlerContext));
            }
        } else {
            log.error("[{}:{}] -> [{}:{}]: 代理请求发送失败, 即将关闭连接, 失败原因: {}", inetSocketAddress.getHostString(), inetSocketAddress.getPort(), targetHost, targetPort, future.cause());
            localChannelHandlerContext.close();
        }
    }

    public HttpProxyForwardingResultListener(ChannelHandlerContext localChannelHandlerContext, ChannelHandlerContext remoteChannelHandlerContext, String targetHost, int targetPort, int  id) {
        this.localChannelHandlerContext = localChannelHandlerContext;
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.id = id;
    }
}
