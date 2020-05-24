package cn.t.tool.netproxytool.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * 转发消息处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:46
 **/
public class ForwardingEncryptedMessageHandler extends ChannelDuplexHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ChannelHandlerContext remoteChannelHandlerContext;
    private final Cipher cipher;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf)msg;
            log.info("[{} : {}]: 转发消息: {} B", remoteChannelHandlerContext.channel().remoteAddress(), ctx.channel().remoteAddress(), ((ByteBuf)msg).readableBytes());
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            bytes = cipher.doFinal(bytes);
            buf.clear();
            buf.writeBytes(bytes);
            remoteChannelHandlerContext.writeAndFlush(msg);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if(remoteChannelHandlerContext != null) {
            if(this.getClass().equals(ForwardingEncryptedMessageHandler.class)) {
                log.info("[{}]: 断开连接, 释放代理资源", ctx.channel().remoteAddress());
            } else {
                log.info("[{}]: 断开连接, 关闭客户端", ctx.channel().remoteAddress());
            }
            remoteChannelHandlerContext.close();
        } else {
            log.warn("[{}]: 断开连接，没有获取代理句柄无法释放代理资源", ctx.channel().remoteAddress());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //消息读取失败不能实现消息转发，断开客户端代理
        ctx.close();
    }

    public ForwardingEncryptedMessageHandler(ChannelHandlerContext remoteChannelHandlerContext, String keyStr) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Key key = new SecretKeySpec(keyStr.getBytes(), "AES");
        this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        this.remoteChannelHandlerContext = remoteChannelHandlerContext;
    }
}
