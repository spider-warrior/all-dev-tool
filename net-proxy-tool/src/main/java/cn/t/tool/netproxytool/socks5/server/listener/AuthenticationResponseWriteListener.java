package cn.t.tool.netproxytool.socks5.server.listener;

import cn.t.tool.netproxytool.socks5.server.analyse.CmdRequestAnalyse;
import cn.t.tool.nettytool.decoder.NettyB2mDecoder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * 协商成功响应客户端监听器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-03-07 16:31
 **/
@Slf4j
public class AuthenticationResponseWriteListener implements ChannelFutureListener {

    private NettyB2mDecoder nettyB2mDecoder;

    @Override
    public void operationComplete(ChannelFuture future) {
        if(future.isSuccess()) {
            nettyB2mDecoder.setByteBufAnalyser(new CmdRequestAnalyse());
        } else {
            log.error("鉴权请求失败", future.cause());
        }
    }

    public AuthenticationResponseWriteListener(NettyB2mDecoder nettyB2mDecoder) {
        this.nettyB2mDecoder = nettyB2mDecoder;
    }
}
