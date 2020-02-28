package cn.t.tool.nettytool.initializer;

import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import cn.t.tool.nettytool.handler.EventLoggingHandler;
import cn.t.tool.nettytool.handler.NettyExceptionHandler;
import cn.t.util.common.CollectionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.util.List;
import java.util.function.Supplier;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private LogLevel loggingHandlerLogLevel = LogLevel.DEBUG;
    private InternalLoggerFactory internalLoggerFactory = Slf4JLoggerFactory.INSTANCE;
    private Supplier<IdleStateHandler> idleStateHandlerSupplier;
    private Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier;
    private Supplier<List<NettyTcpEncoder<?>>> NettyTcpEncoderListSupplier;
    private Supplier<List<ChannelHandler>> channelHandlerListSupplier;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline channelPipeline = ch.pipeline();
        //切换日志实现，防止有人篡改LoggerFactory，强制使用Slf4JLoggerFactory
        InternalLoggerFactory originalInternalLoggerFactory = InternalLoggerFactory.getDefaultFactory();
        InternalLoggerFactory.setDefaultFactory(internalLoggerFactory);
        try {
            channelPipeline.addLast("logging-handler",new EventLoggingHandler(loggingHandlerLogLevel));
            if(idleStateHandlerSupplier != null) {
                channelPipeline.addLast("idle-handler", idleStateHandlerSupplier.get());
            }
            if(nettyTcpDecoderSupplier != null) {
                channelPipeline.addLast("msg-decoder", nettyTcpDecoderSupplier.get());
            }
            if(NettyTcpEncoderListSupplier != null) {
                List<NettyTcpEncoder<?>>nettyTcpEncoderList = NettyTcpEncoderListSupplier.get();
                if(!CollectionUtil.isEmpty(nettyTcpEncoderList)) {
                    nettyTcpEncoderList.forEach(encoder -> channelPipeline.addLast("encoder#" + encoder.getClass().getName(), encoder));
                }
            }
            if(channelHandlerListSupplier != null) {
                List<ChannelHandler> channelHandlerList = channelHandlerListSupplier.get();
                if(!CollectionUtil.isEmpty(channelHandlerList)) {
                    channelHandlerList.forEach(handler -> channelPipeline.addLast("handler#" + handler.getClass().getName(), handler));
                }
            }
            channelPipeline.addLast("exception-handler", new NettyExceptionHandler());
        } finally {
            InternalLoggerFactory.setDefaultFactory(originalInternalLoggerFactory);
        }
    }

    public NettyChannelInitializer(LogLevel loggingHandlerLogLevel, InternalLoggerFactory internalLoggerFactory, Supplier<IdleStateHandler> idleStateHandlerSupplier, Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier, Supplier<List<NettyTcpEncoder<?>>> nettyTcpEncoderListSupplier, Supplier<List<ChannelHandler>> channelHandlerListSupplier) {
        if(loggingHandlerLogLevel != null) {
            this.loggingHandlerLogLevel = loggingHandlerLogLevel;
        }
        if(internalLoggerFactory != null) {
            this.internalLoggerFactory = internalLoggerFactory;
        }
        this.idleStateHandlerSupplier = idleStateHandlerSupplier;
        this.nettyTcpDecoderSupplier = nettyTcpDecoderSupplier;
        NettyTcpEncoderListSupplier = nettyTcpEncoderListSupplier;
        this.channelHandlerListSupplier = channelHandlerListSupplier;
    }
}
