package cn.t.tool.netproxytool.socks5.server.encoder;

import cn.t.tool.netproxytool.socks5.model.NegotiateResponse;
import cn.t.tool.nettytool.encoer.NettyM2bEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yj
 * @since 2020-01-12 16:27
 **/
public class NegotiateResponseEncoder extends NettyM2bEncoder<NegotiateResponse> {
    @Override
    protected void doEncode(ChannelHandlerContext ctx, NegotiateResponse negotiateResponse, ByteBuf out) {
        out.writeByte(negotiateResponse.getVersion());
        out.writeByte(negotiateResponse.getSocks5Method());
    }
}
