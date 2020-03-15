package cn.t.tool.netproxytool.socks5.client.handler;

import cn.t.tool.netproxytool.exception.ProxyException;
import cn.t.tool.netproxytool.socks5.MessageBuildUtil;
import cn.t.tool.netproxytool.socks5.client.listener.AuthenticationRequestWriteListener;
import cn.t.tool.netproxytool.socks5.client.listener.CmdRequestWriteListener;
import cn.t.tool.netproxytool.socks5.constants.Socks5ClientConfig;
import cn.t.tool.netproxytool.socks5.constants.Socks5Method;
import cn.t.tool.netproxytool.socks5.constants.Socks5ProtocolConstants;
import cn.t.tool.netproxytool.socks5.model.CmdRequest;
import cn.t.tool.netproxytool.socks5.model.MethodRequest;
import cn.t.tool.netproxytool.socks5.model.NegotiateResponse;
import cn.t.tool.netproxytool.socks5.model.UsernamePasswordAuthenticationRequest;
import cn.t.tool.nettytool.aware.NettyTcpDecoderAware;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 协商响应处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
public class NegotiateResponseHandler extends SimpleChannelInboundHandler<NegotiateResponse> implements NettyTcpDecoderAware {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private final String host = "www.baidu.com";
    private final short port = 443;
    private NettyTcpDecoder nettyTcpDecoder;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NegotiateResponse response) {
        byte version = response.getVersion();
        if(version != Socks5ProtocolConstants.VERSION) {
            throw new ProxyException(String.format("不支持的协议版本: %d", version));
        }
        byte socks5MethodValue =  response.getSocks5Method();
        Socks5Method socks5Method = Socks5Method.getSocks5Method(socks5MethodValue);
        if(socks5Method == null || Socks5Method.NO_ACCEPTABLE_METHODS == socks5Method) {
            throw new ProxyException("客户端未提供支持的认证方法");
        } else {
            //不需要认证, 直接构建cmd请求
            if(Socks5Method.NO_AUTHENTICATION_REQUIRED == socks5Method) {
                CmdRequest cmdRequest = MessageBuildUtil.buildCmdRequest(host.getBytes(), port);
                ChannelPromise promise = ctx.newPromise();
                promise.addListener(new CmdRequestWriteListener(nettyTcpDecoder));
                ctx.writeAndFlush(cmdRequest, promise);
                //用户名密码认证
            } else if(Socks5Method.USERNAME_PASSWORD == socks5Method) {
                UsernamePasswordAuthenticationRequest usernamePasswordAuthenticationRequest = MessageBuildUtil.buildUsernamePasswordAuthenticationRequest(USERNAME.getBytes(), PASSWORD.getBytes());
                ChannelPromise promise = ctx.newPromise();
                promise.addListener(new AuthenticationRequestWriteListener(nettyTcpDecoder));
                ctx.writeAndFlush(usernamePasswordAuthenticationRequest, promise);
            } else {
                throw new ProxyException("客户端未实现的方法处理: " + socks5Method);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //init attr
        ctx.channel().attr(Socks5ClientConfig.TARGET_HOST_KEY).set(host);
        ctx.channel().attr(Socks5ClientConfig.TARGET_PORT_KEY).set(port);
        ctx.channel().attr(Socks5ClientConfig.USERNAME_KEY).set(USERNAME);
        ctx.channel().attr(Socks5ClientConfig.PASSWORD_KEY).set(PASSWORD);
        //send negotiate msg
        MethodRequest methodRequest = new MethodRequest();
        methodRequest.setVersion(Socks5ProtocolConstants.VERSION);
        methodRequest.setMethods(Socks5ClientConfig.SUPPORT_METHODS);
        ctx.writeAndFlush(methodRequest);
    }

    @Override
    public void setNettyTcpDecoder(NettyTcpDecoder nettyTcpDecoder) {
        this.nettyTcpDecoder = nettyTcpDecoder;
    }
}
