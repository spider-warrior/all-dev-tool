package cn.t.tool.netproxytool.server.encoder;

import cn.t.tool.netproxytool.model.NegotiateResponse;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yj
 * @since 2020-01-12 16:27
 **/
public class ServerNegotiateResponseEncoder extends NettyTcpEncoder<NegotiateResponse> {
    @Override
    protected void doEncode(ChannelHandlerContext ctx, NegotiateResponse negotiateResponse, ByteBuf out) {
        out.writeByte(negotiateResponse.getVersion());
        out.writeByte(negotiateResponse.getMethod().rangeStart);
    }
}
