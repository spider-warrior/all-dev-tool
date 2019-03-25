package cn.t.tool.nettytool.shuiya;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ShuiYaMessageHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(ShuiYaMessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        logger.info("read a msg: {}", Arrays.toString(bytes));

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("连接建立成功: {}", ctx.channel().remoteAddress());
        ByteBuf controlMsg = PooledByteBufAllocator.DEFAULT.buffer();
        //地址码
        controlMsg.writeByte(1);
        //功能码
        controlMsg.writeByte(3);
        //起始地址
        controlMsg.writeShort(0);
        //读取点数
        controlMsg.writeShort(1);
        int writerIndex = controlMsg.writerIndex();
        byte[] data = new byte[controlMsg.readableBytes()];
        controlMsg.readBytes(data);
        controlMsg.writerIndex(writerIndex);
        //CRC
        controlMsg.writeShort(calculateCrc(data));
        ctx.writeAndFlush(controlMsg);
    }

    private static int calculateCrc(byte[] bytes) {
        int crc = 0xFFFF;
        for (int j = 0; j < bytes.length ; j++) {
            crc = ((crc  >>> 8) | (crc  << 8) )& 0xffff;
            crc ^= (bytes[j] & 0xff);//byte to int, trunc sign
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return crc;
    }

    public static void main(String[] args) {
        System.out.println(calculateCrc("123456789".getBytes()));
    }
}
