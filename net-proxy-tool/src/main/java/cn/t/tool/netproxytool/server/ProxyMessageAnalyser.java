package cn.t.tool.netproxytool.server;

import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yj
 * @since 2020-01-12 16:26
 **/
public class ProxyMessageAnalyser extends ByteBufAnalyser {

    @Override
    public Object analyse(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        return null;
    }
}
