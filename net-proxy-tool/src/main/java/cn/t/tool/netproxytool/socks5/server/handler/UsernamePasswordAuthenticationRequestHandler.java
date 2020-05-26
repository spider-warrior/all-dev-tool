package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.socks5.constants.AuthenticationStatus;
import cn.t.tool.netproxytool.socks5.constants.Socks5ProtocolConstants;
import cn.t.tool.netproxytool.socks5.constants.Socks5ServerDaemonConfig;
import cn.t.tool.netproxytool.socks5.model.AuthenticationResponse;
import cn.t.tool.netproxytool.socks5.model.UsernamePasswordAuthenticationRequest;
import cn.t.tool.netproxytool.socks5.server.UserRepository;
import cn.t.tool.netproxytool.socks5.server.listener.AuthenticationResponseWriteListener;
import cn.t.tool.nettytool.aware.NettyTcpDecoderAware;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 认证请求处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class UsernamePasswordAuthenticationRequestHandler extends SimpleChannelInboundHandler<UsernamePasswordAuthenticationRequest>  implements NettyTcpDecoderAware {

    private NettyTcpDecoder nettyTcpDecoder;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UsernamePasswordAuthenticationRequest usernamePasswordAuthenticationRequest) {
        String username = new String(usernamePasswordAuthenticationRequest.getUsername());
        String password = new String(usernamePasswordAuthenticationRequest.getPassword());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setVersion(Socks5ProtocolConstants.VERSION);
        String passwordInConfig = UserRepository.getPassword(username);
        if(passwordInConfig != null && passwordInConfig.equals(password)) {
            log.info("用户名密码验证通过, username: {}", username);
            ctx.channel().attr(Socks5ServerDaemonConfig.CHANNEL_USERNAME).set(username);
            authenticationResponse.setStatus(AuthenticationStatus.SUCCESS.value);
            ChannelPromise promise = ctx.newPromise();
            promise.addListener(new AuthenticationResponseWriteListener(nettyTcpDecoder));
            ctx.writeAndFlush(authenticationResponse, promise);
        } else {
            log.info("用户名密码验证失败, password: {}", password);
            authenticationResponse.setStatus(AuthenticationStatus.FAILED.value);
            ctx.writeAndFlush(authenticationResponse);
        }
    }
    @Override
    public void setNettyTcpDecoder(NettyTcpDecoder nettyTcpDecoder) {
        this.nettyTcpDecoder = nettyTcpDecoder;
    }
}
