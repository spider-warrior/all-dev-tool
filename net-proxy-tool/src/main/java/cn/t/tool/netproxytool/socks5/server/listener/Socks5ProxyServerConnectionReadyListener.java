package cn.t.tool.netproxytool.socks5.server.listener;

import cn.t.tool.netproxytool.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.handler.LengthBasedDecryptedMessageDecoder;
import cn.t.tool.netproxytool.handler.LengthBasedEncryptedMessageEncoder;
import cn.t.tool.netproxytool.socks5.server.handler.CmdRequestHandler;
import cn.t.tool.netproxytool.socks5.server.handler.NegotiateRequestHandler;
import cn.t.tool.netproxytool.socks5.server.handler.UsernamePasswordAuthenticationRequestHandler;
import cn.t.tool.nettytool.decoder.NettyB2mDecoder;
import cn.t.tool.nettytool.encoer.NettyM2bEncoder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

/**
 * http代理结果监听器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-27 15:42
 **/
@Slf4j
public class Socks5ProxyServerConnectionReadyListener implements ChannelFutureListener {

    private final ChannelHandlerContext localChannelHandlerContext;
    private final ChannelHandlerContext remoteChannelHandlerContext;
    private final String clientName;
    private final byte[] security;

    @Override
    public void operationComplete(ChannelFuture future) throws NoSuchAlgorithmException, NoSuchPaddingException {
        if(future.isSuccess()) {
            log.info("{}: 代理连接已就位", clientName);
            //已经通知客户端代理成功, 切换handler
            ChannelPipeline channelPipeline = localChannelHandlerContext.channel().pipeline();
            //remove decoders
            channelPipeline.remove(NettyB2mDecoder.class);
            //remove encoders
            channelPipeline.remove(NettyM2bEncoder.class);
            //remove handlers
            channelPipeline.remove(NegotiateRequestHandler.class);
            channelPipeline.remove(UsernamePasswordAuthenticationRequestHandler.class);
            channelPipeline.remove(CmdRequestHandler.class);
            if(security != null) {
                //默认使用基于length-body结构解密数据
                channelPipeline.addFirst(new LengthBasedDecryptedMessageDecoder(security));
                //默认使用基于length-body结构加密数据
                channelPipeline.addFirst(new LengthBasedEncryptedMessageEncoder(security));
            }
            channelPipeline.addLast("http-via-socks5-proxy-forwarding-handler", new ForwardingMessageHandler(remoteChannelHandlerContext));
        } else {
            log.error("{}: 代理请求发送失败, 即将关闭连接, 失败原因: {}", clientName, future.cause().getMessage());
            localChannelHandlerContext.close();
        }
    }

    public Socks5ProxyServerConnectionReadyListener(ChannelHandlerContext localChannelHandlerContext, ChannelHandlerContext remoteChannelHandlerContext, String clientName, byte[] security) {
        this.localChannelHandlerContext = localChannelHandlerContext;
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
        this.clientName = clientName;
        this.security = security;
    }
}
