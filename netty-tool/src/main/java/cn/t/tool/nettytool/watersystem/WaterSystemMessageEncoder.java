package cn.t.tool.nettytool.watersystem;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class WaterSystemMessageEncoder extends MessageToByteEncoder<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(WaterSystemMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        byte[] data = new byte[msg.readableBytes()];
        msg.markReaderIndex();
        msg.readBytes(data);
        msg.resetReaderIndex();
        out.writeBytes(msg);
        logger.info("write msg: {}", Arrays.toString(data));
    }
}
