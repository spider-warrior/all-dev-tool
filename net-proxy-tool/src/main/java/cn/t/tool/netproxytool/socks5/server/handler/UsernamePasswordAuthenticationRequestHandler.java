package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.socks5.constants.AuthenticationStatus;
import cn.t.tool.netproxytool.socks5.constants.Socks5ProtocolConstants;
import cn.t.tool.netproxytool.socks5.model.UsernamePasswordAuthenticationRequest;
import cn.t.tool.netproxytool.socks5.model.AuthenticationResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 认证请求处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class UsernamePasswordAuthenticationRequestHandler extends SimpleChannelInboundHandler<UsernamePasswordAuthenticationRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, UsernamePasswordAuthenticationRequest usernamePasswordAuthenticationRequest) throws Exception {
        String username = new String(usernamePasswordAuthenticationRequest.getUsername());
        String password = new String(usernamePasswordAuthenticationRequest.getPassword());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setVersion(Socks5ProtocolConstants.VERSION);
        if("admin".equals(username) && "admin".equals(password)) {
            log.info("用户名密码验证通过, username: {}", username);
            authenticationResponse.setStatus(AuthenticationStatus.SUCCESS.value);
        } else {
            log.info("用户名密码验证失败, password: {}", password);
            authenticationResponse.setStatus(AuthenticationStatus.FAILED.value);
        }
        channelHandlerContext.writeAndFlush(authenticationResponse);
    }
}
