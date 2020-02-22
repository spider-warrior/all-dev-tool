package cn.t.tool.nettytool.initializer;

import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import cn.t.util.common.CollectionUtil;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class NettyChannelInitializerBuilder {

    private LogLevel logLevel;
    private InternalLoggerFactory internalLoggerFactory;
    private IdleStateConfig idleStateConfig;
    private Supplier<ByteBufAnalyser> byteBufAnalyserSupplier;
    private List<Supplier<NettyTcpEncoder<?>>> nettyTcpEncoderSupplierList = new ArrayList<>();
    private List<Supplier<ChannelInboundHandler>> channelInboundHandlerSupplierList = new ArrayList<>();

    public NettyChannelInitializer build() {
        return new NettyChannelInitializer(
            logLevel,
            internalLoggerFactory,
            () -> idleStateConfig == null ? null : new IdleStateHandler(idleStateConfig.readerIdleTime, idleStateConfig.writerIdleTime, idleStateConfig.allIdleTime, TimeUnit.SECONDS),
            byteBufAnalyserSupplier == null ? null : () -> new NettyTcpDecoder(byteBufAnalyserSupplier.get()),
            CollectionUtil.isEmpty(nettyTcpEncoderSupplierList) ? null : () -> {
                List<NettyTcpEncoder<?>> nettyTcpEncoderList = new ArrayList<>();
                if(!CollectionUtil.isEmpty(nettyTcpEncoderSupplierList)) {
                    nettyTcpEncoderSupplierList.forEach(supplier -> nettyTcpEncoderList.add(supplier.get()));
                }
                return nettyTcpEncoderList;
            },
            channelInboundHandlerSupplierList == null ? null : () -> {
                List<ChannelInboundHandler> channelInboundHandlerList = new ArrayList<>();
                if(!CollectionUtil.isEmpty(channelInboundHandlerSupplierList)) {
                    channelInboundHandlerSupplierList.forEach(supplier -> channelInboundHandlerList.add(supplier.get()));
                }
                return channelInboundHandlerList;
            }
        );
    }

    public void setIdleState(long readerIdleTime, long writerIdleTime, long allIdleTime) {
        this.idleStateConfig = new IdleStateConfig(readerIdleTime, writerIdleTime, allIdleTime);
    }

    public void setByteBufAnalyserSupplier(Supplier<ByteBufAnalyser> byteBufAnalyserSupplier) {
        this.byteBufAnalyserSupplier = byteBufAnalyserSupplier;
    }

    public void addEncoderListsSupplier(List<Supplier<NettyTcpEncoder<?>>> nettyTcpEncoderSupplierList) {
        if(!CollectionUtil.isEmpty(nettyTcpEncoderSupplierList)) {
            this.nettyTcpEncoderSupplierList.addAll(nettyTcpEncoderSupplierList);
        }
    }

    public void addEncoderListsSupplier(Supplier<NettyTcpEncoder<?>> nettyTcpEncoderSupplier) {
        if(nettyTcpEncoderSupplier != null) {
            this.nettyTcpEncoderSupplierList.add(nettyTcpEncoderSupplier);
        }
    }

    public void addChannelInboundHandlerListSupplier(List<Supplier<ChannelInboundHandler>> channelInboundHandlerSupplierList) {
        if(!CollectionUtil.isEmpty(channelInboundHandlerSupplierList)) {
            this.channelInboundHandlerSupplierList.addAll(channelInboundHandlerSupplierList);
        }
    }

    public void addChannelInboundHandlerSupplier(Supplier<ChannelInboundHandler> channelInboundHandlerSupplier) {
        if(channelInboundHandlerSupplier != null) {
            this.channelInboundHandlerSupplierList.add(channelInboundHandlerSupplier);
        }
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
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
