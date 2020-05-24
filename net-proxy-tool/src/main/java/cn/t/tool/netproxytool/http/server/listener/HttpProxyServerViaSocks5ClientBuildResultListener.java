package cn.t.tool.netproxytool.http.server.listener;

import cn.t.tool.netproxytool.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.http.server.handler.HttpProxyServerViaSocks5Handler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * https代理结果监听器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-27 15:42
 **/
@Slf4j
public class HttpProxyServerViaSocks5ClientBuildResultListener implements ChannelFutureListener {

    private ChannelHandlerContext localChannelHandlerContext;
    private ChannelHandlerContext remoteChannelHandlerContext;
    private String clientName;

    @Override
    public void operationComplete(ChannelFuture future) {
        if(future.isSuccess()) {
            log.info("{}: 通知客户端代理已就位成功", clientName);
            //已经通知客户端代理成功, 切换handler
            ChannelPipeline channelPipeline = localChannelHandlerContext.channel().pipeline();
            channelPipeline.remove(HttpRequestDecoder.class);
            channelPipeline.remove(HttpResponseEncoder.class);
            channelPipeline.remove(HttpObjectAggregator.class);
            channelPipeline.remove(HttpProxyServerViaSocks5Handler.class);
            channelPipeline.addLast("http-proxy-server-via-socks5-forwarding-handler", new ForwardingMessageHandler(remoteChannelHandlerContext));
        } else {
            log.error("{}: 通知客户端代理已就位失败, 即将关闭连接, 失败原因: {}", clientName, future.cause().getMessage());
            localChannelHandlerContext.close();
        }
    }

    public HttpProxyServerViaSocks5ClientBuildResultListener(ChannelHandlerContext localChannelHandlerContext, ChannelHandlerContext remoteChannelHandlerContext, String clientName) {
        this.localChannelHandlerContext = localChannelHandlerContext;
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
        this.clientName = clientName;
    }
}
