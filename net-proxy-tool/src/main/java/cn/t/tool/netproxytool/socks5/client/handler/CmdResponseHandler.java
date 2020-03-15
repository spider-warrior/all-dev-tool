package cn.t.tool.netproxytool.socks5.client.handler;

import cn.t.tool.netproxytool.socks5.client.listener.ProxyEstablishedListener;
import cn.t.tool.netproxytool.socks5.constants.Socks5CmdExecutionStatus;
import cn.t.tool.netproxytool.socks5.model.CmdResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

/**
 * cmd响应处理器处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class CmdResponseHandler extends SimpleChannelInboundHandler<CmdResponse>  {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CmdResponse response) {
        byte status = response.getExecutionStatus();
        if(Socks5CmdExecutionStatus.SUCCEEDED.value == status) {
            log.info("连接代理服务器成功, 即将发送数据");
            HttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
            ChannelPromise promise = ctx.newPromise();
            promise.addListener(new ProxyEstablishedListener(ctx));
            ctx.writeAndFlush(httpRequest, promise);
        } else {
            log.warn("连接代理服务器失败, status: {}", status);
        }
    }

}
