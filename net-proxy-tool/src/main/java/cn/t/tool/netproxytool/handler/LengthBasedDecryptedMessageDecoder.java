package cn.t.tool.netproxytool.handler;

import cn.t.tool.nettytool.util.ByteBufUtil;
import cn.t.util.security.message.AlgorithmUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
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
import java.util.List;

public class LengthBasedDecryptedMessageDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(LengthBasedDecryptedMessageDecoder.class);

    private final Key key;
    private final Cipher cipher;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        if(in.readableBytes() < 4) {
            logger.info("readable bytes length less than 2, return immediately");
            return;
        }
        in.markReaderIndex();
        int size = in.readInt();
        if(in.readableBytes() < size) {
            in.resetReaderIndex();
            logger.info("readable bytes length less than required: {}, actually: {} return immediately", size, in.readableBytes());
            return;
        }
        byte[] bytes = new byte[size];
        in.readBytes(bytes);
        bytes = AlgorithmUtil.decrypt(cipher, key, bytes);
        logger.info("解密消息: {} B", size);
        out.add(ByteBufUtil.createByteBuf(bytes));
    }

    public LengthBasedDecryptedMessageDecoder(byte[] keyBytes) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.key = new SecretKeySpec(keyBytes, "AES");
        this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    }
}
