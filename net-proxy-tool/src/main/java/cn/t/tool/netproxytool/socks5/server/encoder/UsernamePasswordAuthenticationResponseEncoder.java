package cn.t.tool.netproxytool.socks5.server.encoder;

import cn.t.tool.netproxytool.socks5.model.AuthenticationResponse;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * UsernamePasswordAuthenticationResponse编码器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-03-14 20:14
 **/
public class UsernamePasswordAuthenticationResponseEncoder extends NettyTcpEncoder<AuthenticationResponse> {

    @Override
    protected void doEncode(ChannelHandlerContext ctx, AuthenticationResponse authenticationResponse, ByteBuf out) {
        out.writeByte(authenticationResponse.getVersion());
        out.writeByte(authenticationResponse.getStatus());
    }
}
