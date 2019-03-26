package cn.t.tool.nettytool.encoer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public abstract class NettyTcpEncoder<T> extends MessageToByteEncoder<T> {

    @Override
    protected void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) {
        if(msg != null) {
            doEncode(msg, out);
        }
    }

    protected abstract void doEncode(T msg, ByteBuf out);
}
