package cn.t.tool.netproxytool.socks5.server.listener;

import cn.t.tool.netproxytool.socks5.server.handler.CmdRequestHandler;
import cn.t.tool.netproxytool.socks5.server.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.socks5.server.handler.NegotiateRequestHandler;
import cn.t.tool.netproxytool.socks5.server.handler.UsernamePasswordAuthenticationRequestHandler;
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

    private ChannelHandlerContext localChannelHandlerContext;
    private ChannelHandlerContext remoteChannelHandlerContext;
    private String targetHost;
    private int targetPort;

    @Override
    public void operationComplete(ChannelFuture future) {
        if(future.isSuccess()) {
            log.info("[{}]: 发送代理结果成功, 目的地址: [{}:{}]", localChannelHandlerContext.channel().remoteAddress(), targetHost, targetPort);
            //已经通知客户端代理成功, 切换handler
            ChannelPipeline channelPipeline = localChannelHandlerContext.channel().pipeline();
            channelPipeline.remove(NettyTcpDecoder.class);
            channelPipeline.remove(NettyTcpEncoder.class);
            channelPipeline.remove(NegotiateRequestHandler.class);
            channelPipeline.remove(UsernamePasswordAuthenticationRequestHandler.class);
            channelPipeline.remove(CmdRequestHandler.class);
            channelPipeline.addLast("proxy-fording-handler", new ForwardingMessageHandler(remoteChannelHandlerContext));
        } else {
            log.error("[{}]: 发送代理结果失败, 目的地址: [{}:{}], 原因: {}", localChannelHandlerContext.channel().remoteAddress(), targetHost, targetPort, future.cause());
            localChannelHandlerContext.close();
        }
    }

    public Socks5ProxyForwardingResultListener(ChannelHandlerContext localChannelHandlerContext, ChannelHandlerContext remoteChannelHandlerContext, String targetHost, int targetPort) {
        this.localChannelHandlerContext = localChannelHandlerContext;
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }
}
