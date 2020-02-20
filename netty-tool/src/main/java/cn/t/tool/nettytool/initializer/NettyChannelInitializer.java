package cn.t.tool.nettytool.initializer;

import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import cn.t.tool.nettytool.handler.NettyExceptionHandler;
import cn.t.util.common.CollectionUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.util.List;
import java.util.function.Supplier;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private LogLevel logLevel = LogLevel.DEBUG;
    private InternalLoggerFactory internalLoggerFactory = Slf4JLoggerFactory.INSTANCE;
    private Supplier<IdleStateHandler> idleStateHandlerSupplier;
    private Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier;
    private Supplier<List<NettyTcpEncoder<?>>> NettyTcpEncoderListSupplier;
    private Supplier<List<SimpleChannelInboundHandler<?>>> channelInboundHandlerListSupplier;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline channelPipeline = ch.pipeline();
        InternalLoggerFactory originalInternalLoggerFactory = InternalLoggerFactory.getDefaultFactory();
        InternalLoggerFactory.setDefaultFactory(internalLoggerFactory);
        try {
            channelPipeline.addLast("logging",new LoggingHandler(logLevel));
            if(idleStateHandlerSupplier != null) {
                channelPipeline.addLast(idleStateHandlerSupplier.get());
            }
            if(nettyTcpDecoderSupplier != null) {
                channelPipeline.addLast(nettyTcpDecoderSupplier.get());
            }
            if(NettyTcpEncoderListSupplier != null) {
                List<NettyTcpEncoder<?>>nettyTcpEncoderList = NettyTcpEncoderListSupplier.get();
                if(!CollectionUtil.isEmpty(nettyTcpEncoderList)) {
                    nettyTcpEncoderList.forEach(channelPipeline::addLast);
                }
            }
            if(channelInboundHandlerListSupplier != null) {
                List<SimpleChannelInboundHandler<?>> channelInboundHandlerList = channelInboundHandlerListSupplier.get();
                if(!CollectionUtil.isEmpty(channelInboundHandlerList)) {
                    channelInboundHandlerList.forEach(channelPipeline::addLast);
                }
            }
            channelPipeline.addLast(new NettyExceptionHandler());
        } finally {
            InternalLoggerFactory.setDefaultFactory(originalInternalLoggerFactory);
        }
    }

    public NettyChannelInitializer(LogLevel logLevel, InternalLoggerFactory internalLoggerFactory, Supplier<IdleStateHandler> idleStateHandlerSupplier, Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier, Supplier<List<NettyTcpEncoder<?>>> nettyTcpEncoderListSupplier, Supplier<List<SimpleChannelInboundHandler<?>>> channelInboundHandlerListSupplier) {
        if(logLevel != null) {
            this.logLevel = logLevel;
        }
        if(internalLoggerFactory != null) {
            this.internalLoggerFactory = internalLoggerFactory;
        }
        this.idleStateHandlerSupplier = idleStateHandlerSupplier;
        this.nettyTcpDecoderSupplier = nettyTcpDecoderSupplier;
        NettyTcpEncoderListSupplier = nettyTcpEncoderListSupplier;
        this.channelInboundHandlerListSupplier = channelInboundHandlerListSupplier;
    }
}
