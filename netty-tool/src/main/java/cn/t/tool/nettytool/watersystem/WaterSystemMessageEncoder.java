package cn.t.tool.nettytool.watersystem;

import cn.t.tool.nettytool.watersystem.entity.ReadRegisterCommand;
import cn.t.tool.nettytool.watersystem.util.WaterSystemUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class WaterSystemMessageEncoder extends MessageToByteEncoder<ReadRegisterCommand> {

    private static final Logger logger = LoggerFactory.getLogger(WaterSystemMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ReadRegisterCommand msg, ByteBuf out) {

        ByteBuf controlMsg = PooledByteBufAllocator.DEFAULT.buffer();
        //地址码
        controlMsg.writeByte(msg.getAddress());
        //功能码
        controlMsg.writeByte(msg.getFunc());
        //起始地址
        controlMsg.writeShort(msg.getRegisterStartAddress());
        //读取点数
        controlMsg.writeShort(msg.getRegisterCount());
        controlMsg.markReaderIndex();
        byte[] data = new byte[controlMsg.readableBytes()];
        controlMsg.readBytes(data);
        controlMsg.resetReaderIndex();
        //CRC
        controlMsg.writeShortLE(WaterSystemUtil.calculateCrc(data));
        //copy data to log
        data = new byte[controlMsg.readableBytes()];
        controlMsg.readBytes(data);
        controlMsg.resetReaderIndex();

        ctx.writeAndFlush(controlMsg);
        logger.info("write a msg: {}", Arrays.toString(data));
    }

}
