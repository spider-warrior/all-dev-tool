package cn.t.tool.netproxytool.socks5.client.handler;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.http.constants.HttpProxyBuildExecutionStatus;
import cn.t.tool.netproxytool.socks5.client.encoder.CmdRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.MethodRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.UsernamePasswordAuthenticationRequestEncoder;
import cn.t.tool.netproxytool.socks5.constants.Socks5CmdExecutionStatus;
import cn.t.tool.netproxytool.socks5.model.CmdResponse;
import cn.t.tool.netproxytool.socks5.server.handler.ForwardingMessageHandler;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequestEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * cmd响应处理器处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class CmdResponseHandler extends SimpleChannelInboundHandler<CmdResponse>  {

    private ProxyBuildResultListener proxyBuildResultListener;
    private ChannelHandlerContext remoteChannelHandlerContext;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CmdResponse response) {
        byte status = response.getExecutionStatus();
        if(Socks5CmdExecutionStatus.SUCCEEDED.value == status) {
            log.info("[{}]: 连接成功, 回调监听器", ctx.channel().remoteAddress());
            ChannelPipeline channelPipeline = ctx.channel().pipeline();
            channelPipeline.remove(NettyTcpDecoder.class);

            channelPipeline.remove(MethodRequestEncoder.class);
            channelPipeline.remove(UsernamePasswordAuthenticationRequestEncoder.class);
            channelPipeline.remove(CmdRequestEncoder.class);
            channelPipeline.remove(HttpRequestEncoder.class);

            channelPipeline.remove(AuthenticationResponseHandler.class);
            channelPipeline.remove(CmdResponseHandler.class);

            channelPipeline.addLast("http-via-socks5-proxy-fording-handler", new ForwardingMessageHandler(remoteChannelHandlerContext));
            proxyBuildResultListener.handle(HttpProxyBuildExecutionStatus.SUCCEEDED.value, ctx);
        } else {
            log.warn("连接代理服务器失败, status: {}", status);
        }
    }

    public CmdResponseHandler(ProxyBuildResultListener proxyBuildResultListener, ChannelHandlerContext remoteChannelHandlerContext) {
        this.proxyBuildResultListener = proxyBuildResultListener;
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
    }
}
