package cn.t.tool.nettytool.initializer;

import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class NettyChannelInitializerBuilder {

    protected Long readerIdleTime;
    protected Long writerIdleTime;
    protected Long allIdleTime;

    protected Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier;
    protected Supplier<NettyTcpEncoder> nettyTcpEncoderSupplier;
    protected Supplier<List<SimpleChannelInboundHandler>> channelInboundHandlerListSupplier;

    public NettyChannelInitializer build() {
        Supplier<IdleStateHandler> idleStateHandlerSupplier = null;
        if(readerIdleTime != null && writerIdleTime != null && allIdleTime!= null) {
            idleStateHandlerSupplier = () -> {return new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.SECONDS);};
        }
        return new NettyChannelInitializer(idleStateHandlerSupplier, nettyTcpDecoderSupplier, nettyTcpEncoderSupplier, channelInboundHandlerListSupplier);
    }

    public void setIdleState(long readerIdleTime, long writerIdleTime, long allIdleTime) {
        this.readerIdleTime = readerIdleTime;
        this.writerIdleTime = writerIdleTime;
        this.allIdleTime = allIdleTime;
    }

    public void setNettyTcpDecoder(Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier) {
        this.nettyTcpDecoderSupplier = nettyTcpDecoderSupplier;
    }

    public void setNettyTcpEncoder(Supplier<NettyTcpEncoder> nettyTcpEncoderSupplier) {
        this.nettyTcpEncoderSupplier = nettyTcpEncoderSupplier;
    }

    public void addChannelInboundHandler(Supplier<List<SimpleChannelInboundHandler>> channelInboundHandlerListSupplier) {
        this.channelInboundHandlerListSupplier = channelInboundHandlerListSupplier;
    }
}
