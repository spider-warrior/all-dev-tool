package cn.t.tool.netproxytool.http.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * http请求处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-24 11:54
 **/
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        HttpMethod httpMethod = msg.method();
        if(httpMethod == HttpMethod.CONNECT) {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
            ctx.writeAndFlush(response);
        } else {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer("this is response msg".getBytes(StandardCharsets.UTF_8)));
            ctx.writeAndFlush(response);
        }
    }
}
