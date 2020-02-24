package cn.t.tool.netproxytool.socks5.server.analyse.authenticationanalyse;

import io.netty.buffer.ByteBuf;

public interface AuthenticationAnalyse {
    Object analyse(ByteBuf byteBuf);
}
