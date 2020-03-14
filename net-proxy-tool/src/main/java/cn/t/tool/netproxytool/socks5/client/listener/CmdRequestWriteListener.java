package cn.t.tool.netproxytool.socks5.client.listener;

import cn.t.tool.netproxytool.socks5.client.analyse.CmdResponseAnalyse;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
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
public class CmdRequestWriteListener implements ChannelFutureListener {

    private NettyTcpDecoder nettyTcpDecoder;

    @Override
    public void operationComplete(ChannelFuture future) {
        if(future.isSuccess()) {
            nettyTcpDecoder.setByteBufAnalyser(new CmdResponseAnalyse());
        } else {
            log.error("命令请求失败", future.cause());
        }
    }

    public CmdRequestWriteListener(NettyTcpDecoder nettyTcpDecoder) {
        this.nettyTcpDecoder = nettyTcpDecoder;
    }
}
