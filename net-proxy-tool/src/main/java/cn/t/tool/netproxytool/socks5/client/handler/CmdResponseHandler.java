package cn.t.tool.netproxytool.socks5.client.handler;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.handler.DecryptMessageAnalyser;
import cn.t.tool.netproxytool.handler.EncryptedMessageEncoder;
import cn.t.tool.netproxytool.handler.ForwardingDecryptedMessageHandler;
import cn.t.tool.netproxytool.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.http.UserConfig;
import cn.t.tool.netproxytool.http.constants.HttpProxyBuildExecutionStatus;
import cn.t.tool.netproxytool.http.constants.HttpProxyServerClientConfig;
import cn.t.tool.netproxytool.socks5.client.encoder.CmdRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.MethodRequestEncoder;
import cn.t.tool.netproxytool.socks5.client.encoder.UsernamePasswordAuthenticationRequestEncoder;
import cn.t.tool.netproxytool.socks5.constants.Socks5CmdExecutionStatus;
import cn.t.tool.netproxytool.socks5.model.CmdResponse;
import cn.t.tool.nettytool.aware.NettyTcpDecoderAware;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.util.Attribute;
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
public class CmdResponseHandler extends SimpleChannelInboundHandler<CmdResponse> implements NettyTcpDecoderAware {

    private final ProxyBuildResultListener proxyBuildResultListener;
    private final ChannelHandlerContext remoteChannelHandlerContext;
    private NettyTcpDecoder nettyTcpDecoder;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CmdResponse response) throws NoSuchPaddingException, NoSuchAlgorithmException {
        byte status = response.getExecutionStatus();
        if(Socks5CmdExecutionStatus.SUCCEEDED.value == status) {
            log.info("[{} : {}]: 连接成功, 回调监听器", remoteChannelHandlerContext.channel().remoteAddress(), ctx.channel().remoteAddress());
            Attribute<Object> userConfigAttr = ctx.channel().attr(HttpProxyServerClientConfig.USER_CONFIG_KEY);
            ChannelPipeline channelPipeline = ctx.channel().pipeline();
            if(userConfigAttr == null || userConfigAttr.get() == null) {
                channelPipeline.remove(NettyTcpDecoder.class);
            } else {
                nettyTcpDecoder.setByteBufAnalyser(new DecryptMessageAnalyser());
            }
            channelPipeline.remove(MethodRequestEncoder.class);
            channelPipeline.remove(UsernamePasswordAuthenticationRequestEncoder.class);
            channelPipeline.remove(CmdRequestEncoder.class);
            channelPipeline.remove(HttpRequestEncoder.class);

            channelPipeline.remove(AuthenticationResponseHandler.class);
            channelPipeline.remove(CmdResponseHandler.class);
            if(userConfigAttr == null || userConfigAttr.get() == null) {
                channelPipeline.addLast("http-via-socks5-proxy-forwarding-handler", new ForwardingMessageHandler(remoteChannelHandlerContext));
            } else {
                UserConfig userConfig = (UserConfig)userConfigAttr.get();
                //消息加密
                channelPipeline.addFirst("http-via-socks5-proxy-encrypt-forwarding-encoder", new EncryptedMessageEncoder(userConfig.getSecurity()));
                //消息解密转发器
                channelPipeline.addLast("http-via-socks5-proxy-decrypt-forwarding-handler", new ForwardingDecryptedMessageHandler(remoteChannelHandlerContext, userConfig.getSecurity()));
            }
            proxyBuildResultListener.handle(HttpProxyBuildExecutionStatus.SUCCEEDED.value, ctx);
        } else {
            log.warn("连接代理服务器失败, status: {}", status);
        }
    }

    public CmdResponseHandler(ProxyBuildResultListener proxyBuildResultListener, ChannelHandlerContext remoteChannelHandlerContext) {
        this.proxyBuildResultListener = proxyBuildResultListener;
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
    }

    @Override
    public void setNettyTcpDecoder(NettyTcpDecoder nettyTcpDecoder) {
        this.nettyTcpDecoder = nettyTcpDecoder;
    }
}
