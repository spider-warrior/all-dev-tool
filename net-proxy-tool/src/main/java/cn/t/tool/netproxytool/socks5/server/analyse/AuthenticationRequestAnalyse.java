package cn.t.tool.netproxytool.socks5.server.analyse;

import cn.t.tool.netproxytool.exception.ProxyException;
import cn.t.tool.netproxytool.socks5.constants.Socks5Method;
import cn.t.tool.netproxytool.socks5.server.analyse.authenticationanalyse.AuthenticationAnalyse;
import cn.t.tool.netproxytool.socks5.server.analyse.authenticationanalyse.UsernamePasswordAuthenticationAnalyse;
import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端认证解析器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 20:53
 **/
@Slf4j
public class AuthenticationRequestAnalyse  extends ByteBufAnalyser {
    private final Map<Socks5Method, AuthenticationAnalyse> authenticationAnalyseMap = new HashMap<>();

    @Override
    public Object analyse(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        return null;
    }

    public Object analyse(Socks5Method socks5Method, ByteBuf byteBuf) {
        AuthenticationAnalyse authenticationAnalyse = authenticationAnalyseMap.get(socks5Method);
        if(authenticationAnalyse == null) {
            throw new ProxyException(String.format("未找到认证解析器，认证方法为%s", socks5Method));
        } else {
            return authenticationAnalyse.analyse(byteBuf);
        }
    }
    public AuthenticationRequestAnalyse() {
        authenticationAnalyseMap.put(Socks5Method.USERNAME_PASSWORD, new UsernamePasswordAuthenticationAnalyse());
    }
}
