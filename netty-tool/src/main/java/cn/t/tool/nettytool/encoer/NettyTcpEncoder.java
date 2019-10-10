package cn.t.tool.nettytool.encoer;

import cn.t.tool.nettytool.codec.ByteBufEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NettyTcpEncoder<T> extends MessageToByteEncoder<T> {

    private static final Logger logger = LoggerFactory.getLogger(NettyTcpEncoder.class);

    private ByteBufEncoder<T> byteBufEncoder;

    @Override
    protected void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) {
        if(msg != null) {
            byteBufEncoder.encode(out, msg, ctx);
        } else {
            logger.warn("msg is null, ignored!");
        }
    }

    public NettyTcpEncoder(ByteBufEncoder<T> byteBufEncoder) {
        this.byteBufEncoder = byteBufEncoder;
    }
}
