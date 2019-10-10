package cn.t.tool.nettytool.analyser;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class ByteBufAnalyser<Out> implements MessageAnalyser<ByteBuf, Out, ChannelHandlerContext> {
}
