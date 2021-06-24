package cn.t.tool.nettytool.initializer;

import cn.t.tool.nettytool.aware.NettyTcpDecoderAware;
import cn.t.tool.nettytool.daemon.DaemonConfig;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.tool.nettytool.handler.EventLoggingHandler;
import cn.t.tool.nettytool.handler.NettyExceptionHandler;
import cn.t.util.common.CollectionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.List;

import static cn.t.tool.nettytool.constants.HandlerNames.*;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final DaemonConfig daemonConfig;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline channelPipeline = ch.pipeline();
        //切换日志实现，防止有人篡改LoggerFactory，强制使用Slf4JLoggerFactory
        InternalLoggerFactory originalInternalLoggerFactory = InternalLoggerFactory.getDefaultFactory();
        InternalLoggerFactory.setDefaultFactory(daemonConfig.getInternalLoggerFactory());
        try {
            //logging
            channelPipeline.addLast(LOGGING_HANDLER, new EventLoggingHandler(daemonConfig.getLoggingHandlerLogLevel()));
            if(daemonConfig.getIdleStateHandlerSupplier() != null) {
                channelPipeline.addLast(IDLE_HANDLER, daemonConfig.getIdleStateHandlerSupplier().get());
            }
            // decoder
            if(daemonConfig.getNettyTcpDecoderSupplier() != null) {
                channelPipeline.addLast(MSG_DECODER, daemonConfig.getNettyTcpDecoderSupplier().get());
            }
            // m2m encoder
            if(daemonConfig.getNettyM2mEncoderListSupplier() != null) {
                List<MessageToMessageEncoder<?>>nettyTcpEncoderList = daemonConfig.getNettyM2mEncoderListSupplier().get();
                if(!CollectionUtil.isEmpty(nettyTcpEncoderList)) {
                    nettyTcpEncoderList.forEach(encoder -> channelPipeline.addLast(ENCODER_PREFIX + encoder.getClass().getName(), encoder));
                }
            }
            // m2b encoder
            if(daemonConfig.getNettyM2bEncoderListSupplier() != null) {
                List<MessageToByteEncoder<?>>nettyTcpEncoderList = daemonConfig.getNettyM2bEncoderListSupplier().get();
                if(!CollectionUtil.isEmpty(nettyTcpEncoderList)) {
                    nettyTcpEncoderList.forEach(encoder -> channelPipeline.addLast(ENCODER_PREFIX + encoder.getClass().getName(), encoder));
                }
            }
            // handler
            if(daemonConfig.getChannelHandlerListSupplier() != null) {
                List<ChannelHandler> channelHandlerList = daemonConfig.getChannelHandlerListSupplier().get();
                if(!CollectionUtil.isEmpty(channelHandlerList)) {
                    NettyTcpDecoder nettyTcpDecoder = (NettyTcpDecoder)channelPipeline.get(MSG_DECODER);
                    channelHandlerList.forEach(handler -> {
                        if(handler instanceof NettyTcpDecoderAware) {
                            ((NettyTcpDecoderAware)handler).setNettyTcpDecoder(nettyTcpDecoder);
                        }
                        channelPipeline.addLast(HANDLER_PREFIX + handler.getClass().getName(), handler);
                    });
                }
            }
            channelPipeline.addLast(EXCEPTION_HANDLER, new NettyExceptionHandler());
        } finally {
            InternalLoggerFactory.setDefaultFactory(originalInternalLoggerFactory);
        }
    }

    public NettyChannelInitializer(DaemonConfig daemonConfig) {
        this.daemonConfig = daemonConfig;
    }
}
