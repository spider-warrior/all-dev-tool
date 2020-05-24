package cn.t.tool.netproxytool.handler;

import cn.t.util.security.message.AlgorithmUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 转发消息处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:46
 **/
public class EncryptedMessageEncoder extends MessageToByteEncoder<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(EncryptedMessageEncoder.class);

    private final Key key;
    private final Cipher cipher;

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf buf, ByteBuf out) throws Exception {
        //进行消息加密
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        bytes = AlgorithmUtil.encrypt(cipher, key, bytes);
        buf.clear();
        logger.info("加密数据长度: {}, content: {}", bytes.length, Arrays.toString(bytes));
        out.writeShort(bytes.length);
        buf.writeBytes(bytes);
        out.writeBytes(buf);
    }


    public EncryptedMessageEncoder(byte[] keyBytes) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.key = new SecretKeySpec(keyBytes, "AES");
        this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    }
}
