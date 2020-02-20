package cn.t.tool.netproxytool.server.analyse.authenticationanalyse;

import io.netty.buffer.ByteBuf;

public interface AuthenticationAnalyse {
    Object analyse(ByteBuf byteBuf);
}
