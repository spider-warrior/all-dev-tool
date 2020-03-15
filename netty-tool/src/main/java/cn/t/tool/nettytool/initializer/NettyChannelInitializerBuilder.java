package cn.t.tool.nettytool.initializer;

import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import cn.t.tool.nettytool.aware.NettyTcpDecoderAware;
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

    private LogLevel loggingHandlerLogLevel;
    private InternalLoggerFactory internalLoggerFactory;
    private IdleStateConfig idleStateConfig;
    private Supplier<ByteBufAnalyser> byteBufAnalyserSupplier;
    private List<Supplier<MessageToByteEncoder<?>>> nettyTcpEncoderSupplierList = new ArrayList<>();
    private List<Supplier<ChannelHandler>> channelHandlerSupplierList = new ArrayList<>();

    public NettyChannelInitializer build() {
        Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier;
        if(byteBufAnalyserSupplier != null) {
            nettyTcpDecoderSupplier = () -> {
                ByteBufAnalyser byteBufAnalyser = byteBufAnalyserSupplier.get();
                NettyTcpDecoder nettyTcpDecoder = new NettyTcpDecoder(byteBufAnalyser);
                if(byteBufAnalyser instanceof NettyTcpDecoderAware) {
                    ((NettyTcpDecoderAware)byteBufAnalyser).setNettyTcpDecoder(nettyTcpDecoder);
                }
                return nettyTcpDecoder;
            };
        } else {
            nettyTcpDecoderSupplier = null;
        }
        return new NettyChannelInitializer(
            loggingHandlerLogLevel,
            internalLoggerFactory,
            () -> idleStateConfig == null ? null : new IdleStateHandler(idleStateConfig.readerIdleTime, idleStateConfig.writerIdleTime, idleStateConfig.allIdleTime, TimeUnit.SECONDS),
            nettyTcpDecoderSupplier,
            CollectionUtil.isEmpty(nettyTcpEncoderSupplierList) ? null : () -> {
                List<MessageToByteEncoder<?>> nettyTcpEncoderList = new ArrayList<>();
                if(!CollectionUtil.isEmpty(nettyTcpEncoderSupplierList)) {
                    nettyTcpEncoderSupplierList.forEach(supplier -> nettyTcpEncoderList.add(supplier.get()));
                }
                return nettyTcpEncoderList;
            },
            channelHandlerSupplierList == null ? null : () -> {
                List<ChannelHandler> channelHandlerList = new ArrayList<>();
                if(!CollectionUtil.isEmpty(channelHandlerSupplierList)) {
                    channelHandlerSupplierList.forEach(supplier -> channelHandlerList.add(supplier.get()));
                }
                return channelHandlerList;
            }
        );
    }

    public void setIdleState(long readerIdleTime, long writerIdleTime, long allIdleTime) {
        this.idleStateConfig = new IdleStateConfig(readerIdleTime, writerIdleTime, allIdleTime);
    }

    public void setByteBufAnalyserSupplier(Supplier<ByteBufAnalyser> byteBufAnalyserSupplier) {
        this.byteBufAnalyserSupplier = byteBufAnalyserSupplier;
    }

    public void addEncoderListsSupplier(List<Supplier<MessageToByteEncoder<?>>> nettyTcpEncoderSupplierList) {
        if(!CollectionUtil.isEmpty(nettyTcpEncoderSupplierList)) {
            this.nettyTcpEncoderSupplierList.addAll(nettyTcpEncoderSupplierList);
        }
    }

    public void addEncoderListsSupplier(Supplier<MessageToByteEncoder<?>> nettyTcpEncoderSupplier) {
        if(nettyTcpEncoderSupplier != null) {
            this.nettyTcpEncoderSupplierList.add(nettyTcpEncoderSupplier);
        }
    }

    public void addChannelHandlerListSupplier(List<Supplier<ChannelHandler>> channelHandlerSupplierList) {
        if(!CollectionUtil.isEmpty(channelHandlerSupplierList)) {
            this.channelHandlerSupplierList.addAll(channelHandlerSupplierList);
        }
    }

    public void addChannelHandlerSupplier(Supplier<ChannelHandler> channelHandlerSupplier) {
        if(channelHandlerSupplier != null) {
            this.channelHandlerSupplierList.add(channelHandlerSupplier);
        }
    }

    public void setLoggingHandlerLogLevel(LogLevel loggingHandlerLogLevel) {
        this.loggingHandlerLogLevel = loggingHandlerLogLevel;
    }

    public void setInternalLoggerFactory(InternalLoggerFactory internalLoggerFactory) {
        this.internalLoggerFactory = internalLoggerFactory;
    }

    private static class IdleStateConfig {
        private Long readerIdleTime;
        private Long writerIdleTime;
        private Long allIdleTime;

        public IdleStateConfig(Long readerIdleTime, Long writerIdleTime, Long allIdleTime) {
            this.readerIdleTime = readerIdleTime;
            this.writerIdleTime = writerIdleTime;
            this.allIdleTime = allIdleTime;
        }
    }
}
