package cn.t.tool.nettytool;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public abstract class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private long readerIdleTime;
    private long writerIdleTime;
    private long allIdleTime;
    private NettyTcpDecoder nettyTcpDecoder;
    private NettyTcpEncoder<?> nettyTcpEncoder;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.SECONDS));
        if(nettyTcpEncoder != null) {
            channelPipeline.addLast(nettyTcpEncoder);
        }
        if(nettyTcpDecoder != null) {
            channelPipeline.addLast(nettyTcpDecoder);
        }
        addSimpleChannelInboundHandlers(channelPipeline);
        channelPipeline.addLast(new NettyExceptionHandler());
    }

    protected abstract void addSimpleChannelInboundHandlers(ChannelPipeline channelPipeline);

    public NettyChannelInitializer(long readerIdleTime, long writerIdleTime, long allIdleTime, NettyTcpDecoder nettyTcpDecoder, NettyTcpEncoder<?> nettyTcpEncoder) {
        this.readerIdleTime = readerIdleTime;
        this.writerIdleTime = writerIdleTime;
        this.allIdleTime = allIdleTime;
        this.nettyTcpDecoder = nettyTcpDecoder;
        this.nettyTcpEncoder = nettyTcpEncoder;
    }
}
