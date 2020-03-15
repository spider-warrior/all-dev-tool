package cn.t.tool.netproxytool.socks5.client.handler;

import cn.t.tool.netproxytool.socks5.constants.Socks5CmdExecutionStatus;
import cn.t.tool.netproxytool.socks5.model.CmdResponse;
import cn.t.tool.nettytool.aware.NettyTcpDecoderAware;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * cmd响应处理器处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class CmdResponseHandler extends SimpleChannelInboundHandler<CmdResponse> implements NettyTcpDecoderAware {

    private NettyTcpDecoder nettyTcpDecoder;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CmdResponse response) {
        byte status = response.getExecutionStatus();
        if(Socks5CmdExecutionStatus.SUCCEEDED.value == status) {
            log.info("连接代理服务器成功, 即将发送数据");

        } else {
            log.warn("连接代理服务器失败, status: {}", status);
        }
    }

    @Override
    public void setNettyTcpDecoder(NettyTcpDecoder nettyTcpDecoder) {
        this.nettyTcpDecoder = nettyTcpDecoder;
    }

}
