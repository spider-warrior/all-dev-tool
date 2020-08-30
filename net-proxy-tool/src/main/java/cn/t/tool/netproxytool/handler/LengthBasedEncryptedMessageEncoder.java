package cn.t.tool.netproxytool.handler;

import cn.t.util.security.message.AlgorithmUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class LengthBasedEncryptedMessageEncoder extends MessageToByteEncoder<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(LengthBasedEncryptedMessageEncoder.class);

    private final Key key;
    private final Cipher cipher;

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        bytes = AlgorithmUtil.encrypt(cipher, key, bytes);
        //length
        out.writeShort(bytes.length);
        //body
        out.writeBytes(bytes);
        logger.info("encrypt bytes: {} B", bytes.length);
    }

    public LengthBasedEncryptedMessageEncoder(byte[] keyBytes) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.key = new SecretKeySpec(keyBytes, "AES");
        this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    }
}
