package cn.t.tool.nettytool.initializer;

import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import cn.t.tool.nettytool.handler.NettyExceptionHandler;
import cn.t.util.common.CollectionUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;
import java.util.function.Supplier;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Supplier<IdleStateHandler> idleStateHandlerSupplier;
    private Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier;
    private Supplier<List<NettyTcpEncoder>> NettyTcpEncoderListSupplier;
    private Supplier<List<SimpleChannelInboundHandler>> channelInboundHandlerListSupplier;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline channelPipeline = ch.pipeline();
        if(idleStateHandlerSupplier != null) {
            channelPipeline.addLast(idleStateHandlerSupplier.get());
        }
        if(nettyTcpDecoderSupplier != null) {
            channelPipeline.addLast(nettyTcpDecoderSupplier.get());
        }
        if(NettyTcpEncoderListSupplier != null) {
            List<NettyTcpEncoder>nettyTcpEncoderList = NettyTcpEncoderListSupplier.get();
            if(!CollectionUtil.isEmpty(nettyTcpEncoderList)) {
                nettyTcpEncoderList.forEach(channelPipeline::addLast);
            }
        }
        if(channelInboundHandlerListSupplier != null) {
            List<SimpleChannelInboundHandler> channelInboundHandlerList = channelInboundHandlerListSupplier.get();
            if(!CollectionUtil.isEmpty(channelInboundHandlerList)) {
                channelInboundHandlerList.forEach(channelPipeline::addLast);
            }
        }
        channelPipeline.addLast(new NettyExceptionHandler());
    }

    public NettyChannelInitializer(Supplier<IdleStateHandler> idleStateHandlerSupplier, Supplier<NettyTcpDecoder> nettyTcpDecoderSupplier, Supplier<List<NettyTcpEncoder>> NettyTcpEncoderListSupplier, Supplier<List<SimpleChannelInboundHandler>> channelInboundHandlerListSupplier) {
        this.idleStateHandlerSupplier = idleStateHandlerSupplier;
        this.nettyTcpDecoderSupplier = nettyTcpDecoderSupplier;
        this.NettyTcpEncoderListSupplier = NettyTcpEncoderListSupplier;
        this.channelInboundHandlerListSupplier = channelInboundHandlerListSupplier;
    }
}
