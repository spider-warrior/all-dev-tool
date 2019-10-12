package cn.t.tool.nettytool.encoer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NettyTcpEncoder<Msg> extends MessageToByteEncoder<Msg> {

    private static final Logger logger = LoggerFactory.getLogger(NettyTcpEncoder.class);

    @Override
    protected final void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) {
        if(msg != null) {
            doEncode(ctx, msg, out);
        } else {
            logger.warn("sent msg is null, ignored!");
        }
    }

    protected abstract void doEncode(ChannelHandlerContext ctx, Msg msg, ByteBuf out);
}
