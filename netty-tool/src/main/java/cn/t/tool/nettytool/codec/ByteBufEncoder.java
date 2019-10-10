package cn.t.tool.nettytool.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class ByteBufEncoder<Msg> implements MessageEncoder<ByteBuf, Msg, ChannelHandlerContext> {
}
