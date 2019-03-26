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
        controlMsg.writeByte(0x1);
        //功能码
        controlMsg.writeByte(0x3);
        //起始地址
        controlMsg.writeShort(0x10);
        //读取点数
        controlMsg.writeShort(0x2);
        int readerIndex = controlMsg.readerIndex();
        byte[] data = new byte[controlMsg.readableBytes()];
        controlMsg.readBytes(data);
        controlMsg.readerIndex(readerIndex);
        //CRC
        controlMsg.writeShortLE(calculateCrc(data));
        ctx.writeAndFlush(controlMsg);
    }

    private static int calculateCrc(byte[] bytes) {
        int value = 0xFFFF;
        for(byte b: bytes) {
            for(int i=0; i<8; i++) {
                if((((b>>i) ^ value) & 1) == 1) {
                    value = ((value>>1) ^ 0xA001);
                } else {
                    value>>=1;
                }
            }
        }
        return (short)value;
    }

}
