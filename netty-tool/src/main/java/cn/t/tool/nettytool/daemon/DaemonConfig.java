package cn.t.tool.nettytool.daemon;

import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.util.List;
import java.util.function.Supplier;

public class DaemonConfig {
    private LogLevel loggingHandlerLogLevel = LogLevel.DEBUG;
    private InternalLoggerFactory internalLoggerFactory = Slf4JLoggerFactory.INSTANCE;
    private Supplier<IdleStateHandler> idleStateHandlerSupplier;
    private Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier;
    private Supplier<List<MessageToByteEncoder<?>>> nettyTcpEncoderListSupplier;
    private Supplier<List<ChannelHandler>> channelHandlerListSupplier;

    public LogLevel getLoggingHandlerLogLevel() {
        return loggingHandlerLogLevel;
    }

    public void setLoggingHandlerLogLevel(LogLevel loggingHandlerLogLevel) {
        this.loggingHandlerLogLevel = loggingHandlerLogLevel;
    }

    public InternalLoggerFactory getInternalLoggerFactory() {
        return internalLoggerFactory;
    }

    public void setInternalLoggerFactory(InternalLoggerFactory internalLoggerFactory) {
        this.internalLoggerFactory = internalLoggerFactory;
    }

    public Supplier<IdleStateHandler> getIdleStateHandlerSupplier() {
        return idleStateHandlerSupplier;
    }

    public void setIdleStateHandlerSupplier(Supplier<IdleStateHandler> idleStateHandlerSupplier) {
        this.idleStateHandlerSupplier = idleStateHandlerSupplier;
    }

    public Supplier<NettyTcpDecoder> getNettyTcpDecoderSupplier() {
        return nettyTcpDecoderSupplier;
    }

    public void setNettyTcpDecoderSupplier(Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier) {
        this.nettyTcpDecoderSupplier = nettyTcpDecoderSupplier;
    }

    public Supplier<List<MessageToByteEncoder<?>>> getNettyTcpEncoderListSupplier() {
        return nettyTcpEncoderListSupplier;
    }

    public void setNettyTcpEncoderListSupplier(Supplier<List<MessageToByteEncoder<?>>> nettyTcpEncoderListSupplier) {
        this.nettyTcpEncoderListSupplier = nettyTcpEncoderListSupplier;
    }

    public Supplier<List<ChannelHandler>> getChannelHandlerListSupplier() {
        return channelHandlerListSupplier;
    }

    public void setChannelHandlerListSupplier(Supplier<List<ChannelHandler>> channelHandlerListSupplier) {
        this.channelHandlerListSupplier = channelHandlerListSupplier;
    }


}
