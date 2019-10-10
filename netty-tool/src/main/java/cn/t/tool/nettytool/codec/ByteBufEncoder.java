package cn.t.tool.nettytool.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.TypeParameterMatcher;

public abstract class ByteBufEncoder<Msg> implements MessageEncoder<ByteBuf, Msg, ChannelHandlerContext> {

    private final TypeParameterMatcher matcher;

    @Override
    public final boolean support(Object msg) {
        return matcher.match(msg);
    }

    public ByteBufEncoder() {
        matcher = TypeParameterMatcher.find(this, ByteBufEncoder.class, "Msg");
    }
}
