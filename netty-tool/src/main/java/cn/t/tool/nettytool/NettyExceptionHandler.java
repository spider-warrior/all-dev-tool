package cn.t.tool.nettytool;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class NettyExceptionHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(NettyExceptionHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Uncaught exceptions from inbound handlers will propagate up to this handler
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        logger.error(String.format("%s读取消息异常", socketAddress), cause);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                // Handle connect exception here...
                logger.error("cannot connect to: {}", future.channel().remoteAddress());
            }
        }));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                // Handle write exception here...
                logger.error("{}写出消息异常", future.channel().remoteAddress());
            }
        }));
    }

    // ... override more outbound methods to handle their exceptions as well
}
