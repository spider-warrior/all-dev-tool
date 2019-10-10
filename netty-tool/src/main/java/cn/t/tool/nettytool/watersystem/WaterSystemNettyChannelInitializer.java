package cn.t.tool.nettytool.watersystem;

import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class WaterSystemNettyChannelInitializer extends NettyChannelInitializer {

    @Override
    protected void addSimpleChannelInboundHandlers(ChannelPipeline channelPipeline) {
        channelPipeline.addLast(new WaterSystemMessageEncoder());
        channelPipeline.addLast(new NettyTcpDecoder(new WaterSystemMessageAnalyser()));
        channelPipeline.addLast(new WaterSystemReadRegisterCommandResponseHandler());
    }

    public WaterSystemNettyChannelInitializer() {
        super(180, 180, 180, null, null);
    }

}
