package cn.t.tool.netproxytool.handler;

import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-05-24 22:57
 **/
public class EncryptMessageAnalyser extends ByteBufAnalyser {

    private static final Logger logger = LoggerFactory.getLogger(EncryptMessageAnalyser.class);

    @Override
    public Object analyse(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        short size = byteBuf.readShort();
        if(byteBuf.readableBytes() < size) {
            return null;
        }
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(size);
        buf.writeBytes(byteBuf, size);
        ByteBuf dup = buf.duplicate();
        byte[] bytes = new byte[dup.readableBytes()];
        dup.readBytes(bytes);
        logger.info("加密数据长度: {}, content: {}", bytes.length, Arrays.toString(bytes));
        return buf;
    }
}
