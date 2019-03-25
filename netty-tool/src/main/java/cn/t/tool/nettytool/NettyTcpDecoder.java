package cn.t.tool.nettytool;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class NettyTcpDecoder extends ByteToMessageDecoder {

    private static Logger logger = LoggerFactory.getLogger(NettyTcpDecoder.class);

    protected final Object readButNothing = new Object();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if(in.isReadable()) {
            int readerIndex = in.readerIndex();
            Object msg = readMessage(ctx, in);
            if(msg == null) {
                logger.info("消息不完整，重置读取索引");
                in.readerIndex(readerIndex);
            } else {
                if(readButNothing != msg) {
                    logger.info("读取到消息，类型为: {}", msg.getClass().getName());
                    out.add(msg);
                } else {
                    logger.info("未读取到消息，不重置读取索引");
                }
            }
        }
    }


    protected abstract Object readMessage(ChannelHandlerContext ctx, ByteBuf in);

}
