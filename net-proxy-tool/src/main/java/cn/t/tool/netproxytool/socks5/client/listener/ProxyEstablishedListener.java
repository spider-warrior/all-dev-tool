package cn.t.tool.netproxytool.socks5.client.listener;

import cn.t.tool.netproxytool.socks5.client.handler.AuthenticationResponseHandler;
import cn.t.tool.netproxytool.socks5.client.handler.CmdResponseHandler;
import cn.t.tool.netproxytool.socks5.client.handler.NegotiateResponseHandler;
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
public class ProxyEstablishedListener implements ChannelFutureListener {

    private ChannelHandlerContext localChannelHandlerContext;

    @Override
    public void operationComplete(ChannelFuture future) {
        if(future.isSuccess()) {
            log.info("代理通道建立成功成功, 目的地址: [{}]", localChannelHandlerContext.channel().remoteAddress());
            //已经通知客户端代理成功, 切换handler
            ChannelPipeline channelPipeline = localChannelHandlerContext.channel().pipeline();
            channelPipeline.remove(NettyTcpDecoder.class);
            channelPipeline.remove(NettyTcpEncoder.class);
            channelPipeline.remove(NegotiateResponseHandler.class);
            channelPipeline.remove(AuthenticationResponseHandler.class);
            channelPipeline.remove(CmdResponseHandler.class);
        } else {
            log.error("代理通道建立成功失败, 目的地址: [{}], 原因: {}", localChannelHandlerContext.channel().remoteAddress(), future.cause().getMessage());
            localChannelHandlerContext.close();
        }
    }

    public ProxyEstablishedListener(ChannelHandlerContext localChannelHandlerContext) {
        this.localChannelHandlerContext = localChannelHandlerContext;
    }
}
