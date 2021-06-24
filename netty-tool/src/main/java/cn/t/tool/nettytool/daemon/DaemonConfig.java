package cn.t.tool.nettytool.daemon;

import cn.t.tool.nettytool.decoder.NettyB2mDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
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
    private Supplier<NettyB2mDecoder> nettyB2mDecoderSupplier;
    private Supplier<List<MessageToMessageEncoder<?>>> nettyM2mEncoderListSupplier;
    private Supplier<List<MessageToByteEncoder<?>>> nettyM2bEncoderListSupplier;
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

    public Supplier<NettyB2mDecoder> getNettyB2mDecoderSupplier() {
        return nettyB2mDecoderSupplier;
    }

    public void setNettyB2mDecoderSupplier(Supplier<NettyB2mDecoder> nettyB2mDecoderSupplier) {
        this.nettyB2mDecoderSupplier = nettyB2mDecoderSupplier;
    }

    public Supplier<List<MessageToMessageEncoder<?>>> getNettyM2mEncoderListSupplier() {
        return nettyM2mEncoderListSupplier;
    }

    public void setNettyM2mEncoderListSupplier(Supplier<List<MessageToMessageEncoder<?>>> nettyM2mEncoderListSupplier) {
        this.nettyM2mEncoderListSupplier = nettyM2mEncoderListSupplier;
    }

    public Supplier<List<MessageToByteEncoder<?>>> getNettyM2bEncoderListSupplier() {
        return nettyM2bEncoderListSupplier;
    }

    public void setNettyM2bEncoderListSupplier(Supplier<List<MessageToByteEncoder<?>>> nettyM2bEncoderListSupplier) {
        this.nettyM2bEncoderListSupplier = nettyM2bEncoderListSupplier;
    }

    public Supplier<List<ChannelHandler>> getChannelHandlerListSupplier() {
        return channelHandlerListSupplier;
    }

    public void setChannelHandlerListSupplier(Supplier<List<ChannelHandler>> channelHandlerListSupplier) {
        this.channelHandlerListSupplier = channelHandlerListSupplier;
    }


}
