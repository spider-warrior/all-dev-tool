package cn.t.tool.nettytool.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class NettyExceptionHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(NettyExceptionHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Uncaught exceptions from inbound handlers will propagate up to this handler
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        logger.error("[{}]: 读取消息异常, 异常类型: {}, 异常消息: {}", socketAddress, cause.getClass(), cause.getMessage());
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                // Handle connect exception here...
                logger.error("[{}]: 连接失败", remoteAddress);
            }
        }));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                // Handle write exception here...
                logger.error("[{}] -> [{}]: 写出消息异常", future.channel().localAddress(), future.channel().remoteAddress());
            }
        }));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        logger.error("[{}]: 读取超时,断开连接", ctx.channel().remoteAddress());
        ctx.close();
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        logger.error("[{}]: 写出超时,断开连接", ctx.channel().remoteAddress());
        ctx.close();
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        logger.error("[{}]: 读取或写出超时,断开连接", ctx.channel().remoteAddress());
        ctx.close();
    }

    // ... override more outbound methods to handle their exceptions as well
}
