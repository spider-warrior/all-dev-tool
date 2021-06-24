package cn.t.tool.netproxytool.socks5.client.handler;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.handler.LengthBasedDecryptedMessageDecoder;
import cn.t.tool.netproxytool.handler.LengthBasedEncryptedMessageEncoder;
import cn.t.tool.netproxytool.http.config.Socks5ClientConfig;
import cn.t.tool.netproxytool.http.constants.ProxyBuildExecutionStatus;
import cn.t.tool.netproxytool.socks5.client.encoder.CmdRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.MethodRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.UsernamePasswordAuthenticationRequestEncoder;
import cn.t.tool.netproxytool.socks5.constants.Socks5CmdExecutionStatus;
import cn.t.tool.netproxytool.socks5.model.CmdResponse;
import cn.t.tool.nettytool.decoder.NettyB2mDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequestEncoder;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

/**
 * cmd响应处理器处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class CmdResponseHandler extends SimpleChannelInboundHandler<CmdResponse> {

    private final ProxyBuildResultListener proxyBuildResultListener;
    private final ChannelHandlerContext remoteChannelHandlerContext;
    private final Socks5ClientConfig socks5ClientConfig;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CmdResponse response) throws NoSuchAlgorithmException, NoSuchPaddingException {
        byte status = response.getExecutionStatus();
        if(Socks5CmdExecutionStatus.SUCCEEDED.value == status) {
            log.info("[{} : {}]: 连接成功, 回调监听器", remoteChannelHandlerContext.channel().remoteAddress(), ctx.channel().remoteAddress());
            ChannelPipeline channelPipeline = ctx.channel().pipeline();
            channelPipeline.remove(NettyB2mDecoder.class);
            channelPipeline.remove(MethodRequestEncoder.class);
            channelPipeline.remove(UsernamePasswordAuthenticationRequestEncoder.class);
            channelPipeline.remove(CmdRequestEncoder.class);
            channelPipeline.remove(HttpRequestEncoder.class);
            channelPipeline.remove(AuthenticationResponseHandler.class);
            channelPipeline.remove(CmdResponseHandler.class);
            if(socks5ClientConfig != null) {
                //读取的数据要解密
                channelPipeline.addFirst(new LengthBasedDecryptedMessageDecoder(socks5ClientConfig.getSecurity()));
                //写出去的数据要加密
                channelPipeline.addFirst(new LengthBasedEncryptedMessageEncoder(socks5ClientConfig.getSecurity()));
            }
            channelPipeline.addLast("http-via-socks5-proxy-forwarding-handler", new ForwardingMessageHandler(remoteChannelHandlerContext));
            proxyBuildResultListener.handle(ProxyBuildExecutionStatus.SUCCEEDED.value, ctx);
        } else {
            log.warn("连接代理服务器失败, status: {}", status);
        }
    }

    public CmdResponseHandler(ProxyBuildResultListener proxyBuildResultListener, ChannelHandlerContext remoteChannelHandlerContext, Socks5ClientConfig socks5ClientConfig) {
        this.proxyBuildResultListener = proxyBuildResultListener;
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
        this.socks5ClientConfig = socks5ClientConfig;
    }
}
