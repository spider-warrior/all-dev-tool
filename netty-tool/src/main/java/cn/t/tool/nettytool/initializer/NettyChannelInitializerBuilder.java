package cn.t.tool.nettytool.initializer;

import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import cn.t.tool.nettytool.aware.NettyTcpDecoderAware;
import cn.t.tool.nettytool.daemon.DaemonConfig;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.util.common.CollectionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class NettyChannelInitializerBuilder {

    private final DaemonConfig daemonConfig = new DaemonConfig();

    public NettyChannelInitializerBuilder configByteBufAnalyser(Supplier<ByteBufAnalyser> byteBufAnalyserSupplier) {
        if(byteBufAnalyserSupplier != null) {
            daemonConfig.setNettyTcpDecoderSupplier(() -> {
                ByteBufAnalyser byteBufAnalyser = byteBufAnalyserSupplier.get();
                NettyTcpDecoder nettyTcpDecoder = new NettyTcpDecoder(byteBufAnalyser);
                if(byteBufAnalyser instanceof NettyTcpDecoderAware) {
                    ((NettyTcpDecoderAware)byteBufAnalyser).setNettyTcpDecoder(nettyTcpDecoder);
                }
                return nettyTcpDecoder;
            });
        }
        return this;
    }

    public NettyChannelInitializerBuilder configLogLevel(LogLevel logLevel) {
        if(logLevel != null) {
            daemonConfig.setLoggingHandlerLogLevel(logLevel);
        }
        return this;
    }

    public NettyChannelInitializerBuilder configLogFactory(InternalLoggerFactory loggerFactory) {
        if(loggerFactory != null) {
            daemonConfig.setInternalLoggerFactory(loggerFactory);
        }
        return this;
    }

    public NettyChannelInitializerBuilder configIdleHandler(long readerIdleTime, long writerIdleTime, long allIdleTime) {
        daemonConfig.setIdleStateHandlerSupplier(() -> new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.SECONDS));
        return this;
    }

    public NettyChannelInitializerBuilder configEncoder(List<Supplier<MessageToByteEncoder<?>>> supplierList) {
        if(!CollectionUtil.isEmpty(supplierList)) {
            daemonConfig.setNettyTcpEncoderListSupplier(() -> {
                List<MessageToByteEncoder<?>> nettyTcpEncoderList = new ArrayList<>();
                supplierList.forEach(supplier -> nettyTcpEncoderList.add(supplier.get()));
                return nettyTcpEncoderList;
            });
        }
        return this;
    }

    public NettyChannelInitializerBuilder configHandler(List<Supplier<ChannelHandler>> supplierList) {
        if(!CollectionUtil.isEmpty(supplierList)) {
            daemonConfig.setChannelHandlerListSupplier(() -> {
                List<ChannelHandler> handlerList = new ArrayList<>();
                supplierList.forEach(supplier -> handlerList.add(supplier.get()));
                return handlerList;
            });
        }
        return this;
    }

}
