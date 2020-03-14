package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.exception.ProxyException;
import cn.t.tool.netproxytool.socks5.constants.Socks5Method;
import cn.t.tool.netproxytool.socks5.constants.Socks5ProtocolConstants;
import cn.t.tool.netproxytool.socks5.model.NegotiateRequest;
import cn.t.tool.netproxytool.socks5.model.NegotiateResponse;
import cn.t.tool.netproxytool.socks5.server.listener.NegotiateSuccessWriteListener;
import cn.t.tool.nettytool.aware.NettyTcpDecoderAware;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.util.common.CollectionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

/**
 * 协商请求处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
public class NegotiateRequestHandler extends SimpleChannelInboundHandler<NegotiateRequest> implements NettyTcpDecoderAware {

    private NettyTcpDecoder nettyTcpDecoder;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NegotiateRequest negotiateRequest) {
        byte version = negotiateRequest.getVersion();
        if(version != Socks5ProtocolConstants.VERSION) {
            throw new ProxyException(String.format("不支持的协议版本: %d", version));
        }
        List<Socks5Method> socks5MethodList =  negotiateRequest.getSupportSocks5MethodList();
        if(CollectionUtil.isEmpty(socks5MethodList)) {
            throw new ProxyException("客户端未提供支持的认证方法");
        } else {
            Socks5Method selectedSocks5Method = negotiateMethod(socks5MethodList);
            if(selectedSocks5Method == null) {
                throw new ProxyException(String.format("未协商到合适的认证方法, 客户端支持的内容为: %s", socks5MethodList));
            }
            NegotiateResponse negotiateResponse = new NegotiateResponse();
            negotiateResponse.setVersion(version);
            negotiateResponse.setSocks5Method(selectedSocks5Method.rangeStart);

            ChannelPromise channelPromise = ctx.newPromise();
            channelPromise.addListener(new NegotiateSuccessWriteListener(nettyTcpDecoder));
            ctx.writeAndFlush(negotiateResponse, channelPromise);

        }
    }

    private Socks5Method negotiateMethod(List<Socks5Method> socks5MethodList) {
        if(socks5MethodList.contains(Socks5Method.USERNAME_PASSWORD)) {
            return Socks5Method.USERNAME_PASSWORD;
        } else if (socks5MethodList.contains(Socks5Method.NO_AUTHENTICATION_REQUIRED)) {
            return Socks5Method.NO_AUTHENTICATION_REQUIRED;
        } else {
            return null;
        }
    }

    @Override
    public void setNettyTcpDecoder(NettyTcpDecoder nettyTcpDecoder) {
        this.nettyTcpDecoder = nettyTcpDecoder;
    }
}
