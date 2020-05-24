package cn.t.tool.netproxytool.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

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
public class ForwardingEncryptedMessageHandler extends ForwardingMessageHandler {

    private final Cipher cipher;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof ByteBuf) {
            //进行消息加密
            ByteBuf buf = (ByteBuf)msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            bytes = cipher.doFinal(bytes);
            buf.clear();
            buf.writeBytes(bytes);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //消息读取失败不能实现消息转发，断开客户端代理
        ctx.close();
    }

    public ForwardingEncryptedMessageHandler(ChannelHandlerContext remoteChannelHandlerContext, String keyStr) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        super(remoteChannelHandlerContext);
        Key key = new SecretKeySpec(keyStr.getBytes(), "AES");
        this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
}
