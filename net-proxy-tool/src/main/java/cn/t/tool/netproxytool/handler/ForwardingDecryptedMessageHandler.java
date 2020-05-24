package cn.t.tool.netproxytool.handler;

import cn.t.util.security.message.AlgorithmUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * 转发消息处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:46
 **/
public class ForwardingDecryptedMessageHandler extends ForwardingMessageHandler {

    private final Key key;
    private final Cipher cipher;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof ByteBuf) {
            //进行消息解密
            ByteBuf buf = (ByteBuf)msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            bytes = AlgorithmUtil.decrypt(cipher, key, bytes);
            buf.clear();
            buf.writeBytes(bytes);
        }
        super.channelRead(ctx, msg);
    }

    public ForwardingDecryptedMessageHandler(ChannelHandlerContext remoteChannelHandlerContext, byte[] keyBytes) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super(remoteChannelHandlerContext);
        this.key = new SecretKeySpec(keyBytes, "AES");
        this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    }
}
