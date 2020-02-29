package cn.t.tool.netproxytool.socks5.server.promise;

import cn.t.tool.netproxytool.component.MessageSender;
import cn.t.tool.netproxytool.socks5.server.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.socks5.server.handler.Socks5MessageHandler;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import lombok.extern.slf4j.Slf4j;

/**
 * http代理结果监听器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-27 15:42
 **/
@Slf4j
public class Socks5ProxyForwardingResultListener implements ChannelFutureListener {

    private ChannelHandlerContext channelHandlerContext;
    private MessageSender messageSender;
    private String targetHost;
    private int targetPort;

    @Override
    public void operationComplete(ChannelFuture future) {
        if(future.isSuccess()) {
            log.info("[{}]: 发送代理结果成功, 目的地址: [{}:{}]", channelHandlerContext.channel().remoteAddress(), targetHost, targetPort);
            //已经通知客户端代理成功, 切换handler
            ChannelPipeline channelPipeline = channelHandlerContext.channel().pipeline();
            channelPipeline.remove(NettyTcpDecoder.class);
            channelPipeline.remove(NettyTcpEncoder.class);
            channelPipeline.remove(Socks5MessageHandler.class);
            channelPipeline.addLast("proxy-fording-handler", new ForwardingMessageHandler(messageSender));
        } else {
            log.error("[{}]: 发送代理结果失败, 目的地址: [{}:{}], 原因: {}", channelHandlerContext.channel().remoteAddress(), targetHost, targetPort, future.cause());
            channelHandlerContext.close();
        }
    }

    public Socks5ProxyForwardingResultListener(ChannelHandlerContext channelHandlerContext, MessageSender messageSender, String targetHost, int targetPort) {
        this.channelHandlerContext = channelHandlerContext;
        this.messageSender = messageSender;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }
}
