package cn.t.tool.nettytool.initializer;

import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import cn.t.tool.nettytool.aware.NettyB2mDecoderAware;
import cn.t.tool.nettytool.daemon.DaemonConfig;
import cn.t.tool.nettytool.decoder.NettyB2mDecoder;
import cn.t.util.common.CollectionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class DaemonConfigBuilder {

    private final DaemonConfig daemonConfig = new DaemonConfig();

    public DaemonConfigBuilder configByteBufAnalyser(Supplier<ByteBufAnalyser> byteBufAnalyserSupplier) {
        if(byteBufAnalyserSupplier != null) {
            daemonConfig.setNettyB2mDecoderSupplier(() -> {
                ByteBufAnalyser byteBufAnalyser = byteBufAnalyserSupplier.get();
                NettyB2mDecoder nettyB2mDecoder = new NettyB2mDecoder(byteBufAnalyser);
                if(byteBufAnalyser instanceof NettyB2mDecoderAware) {
                    ((NettyB2mDecoderAware)byteBufAnalyser).setNettyB2mDecoder(nettyB2mDecoder);
                }
                return nettyB2mDecoder;
            });
        }
        return this;
    }

    public DaemonConfigBuilder configLogLevel(LogLevel logLevel) {
        if(logLevel != null) {
            daemonConfig.setLoggingHandlerLogLevel(logLevel);
        }
        return this;
    }

    public DaemonConfigBuilder configLogFactory(InternalLoggerFactory loggerFactory) {
        if(loggerFactory != null) {
            daemonConfig.setInternalLoggerFactory(loggerFactory);
        }
        return this;
    }

    public DaemonConfigBuilder configIdleHandler(long readerIdleTime, long writerIdleTime, long allIdleTime) {
        daemonConfig.setIdleStateHandlerSupplier(() -> new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.SECONDS));
        return this;
    }

    public DaemonConfigBuilder configM2mEncoder(List<Supplier<MessageToMessageEncoder<?>>> supplierList) {
        if(!CollectionUtil.isEmpty(supplierList)) {
            daemonConfig.setNettyM2mEncoderListSupplier(() -> {
                List<MessageToMessageEncoder<?>> nettyTcpEncoderList = new ArrayList<>();
                supplierList.forEach(supplier -> nettyTcpEncoderList.add(supplier.get()));
                return nettyTcpEncoderList;
            });
        }
        return this;
    }

    public DaemonConfigBuilder configM2bEncoder(List<Supplier<MessageToByteEncoder<?>>> supplierList) {
        if(!CollectionUtil.isEmpty(supplierList)) {
            daemonConfig.setNettyM2bEncoderListSupplier(() -> {
                List<MessageToByteEncoder<?>> nettyTcpEncoderList = new ArrayList<>();
                supplierList.forEach(supplier -> nettyTcpEncoderList.add(supplier.get()));
                return nettyTcpEncoderList;
            });
        }
        return this;
    }

    public DaemonConfigBuilder configHandler(List<Supplier<ChannelHandler>> supplierList) {
        if(!CollectionUtil.isEmpty(supplierList)) {
            daemonConfig.setChannelHandlerListSupplier(() -> {
                List<ChannelHandler> handlerList = new ArrayList<>();
                supplierList.forEach(supplier -> handlerList.add(supplier.get()));
                return handlerList;
            });
        }
        return this;
    }

    public static DaemonConfigBuilder newInstance() {
        return new DaemonConfigBuilder();
    }

    public DaemonConfig build() {
        return daemonConfig;
    }

    private DaemonConfigBuilder() {}

}
