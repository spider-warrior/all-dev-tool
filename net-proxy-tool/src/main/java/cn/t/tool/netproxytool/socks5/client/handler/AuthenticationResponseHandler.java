package cn.t.tool.netproxytool.socks5.client.handler;

import cn.t.tool.netproxytool.socks5.util.Socks5MessageUtil;
import cn.t.tool.netproxytool.socks5.client.listener.CmdRequestWriteListener;
import cn.t.tool.netproxytool.socks5.constants.Socks5ClientConfig;
import cn.t.tool.netproxytool.socks5.constants.Socks5CmdExecutionStatus;
import cn.t.tool.netproxytool.socks5.model.AuthenticationResponse;
import cn.t.tool.netproxytool.socks5.model.CmdRequest;
import cn.t.tool.nettytool.aware.NettyTcpDecoderAware;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 鉴权响应处理器处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class AuthenticationResponseHandler extends SimpleChannelInboundHandler<AuthenticationResponse> implements NettyTcpDecoderAware {

    private NettyTcpDecoder nettyTcpDecoder;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AuthenticationResponse response) {
        log.info("鉴权结果: version: {}, status: {}", response.getVersion(), response.getStatus());
        if(Socks5CmdExecutionStatus.SUCCEEDED.value == response.getStatus()) {
            String targetHost = ctx.channel().attr(Socks5ClientConfig.TARGET_HOST_KEY).get();
            Short targetPort = ctx.channel().attr(Socks5ClientConfig.TARGET_PORT_KEY).get();
            CmdRequest cmdRequest = Socks5MessageUtil.buildCmdRequest(targetHost.getBytes(), targetPort);
            ChannelPromise promise = ctx.newPromise();
            promise.addListener(new CmdRequestWriteListener(nettyTcpDecoder));
            ctx.writeAndFlush(cmdRequest, promise);
        }
    }

    @Override
    public void setNettyTcpDecoder(NettyTcpDecoder nettyTcpDecoder) {
        this.nettyTcpDecoder = nettyTcpDecoder;
    }

}
