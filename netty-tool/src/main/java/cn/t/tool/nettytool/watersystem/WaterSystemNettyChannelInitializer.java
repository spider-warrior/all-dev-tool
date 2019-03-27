package cn.t.tool.nettytool.watersystem;

import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class WaterSystemNettyChannelInitializer extends NettyChannelInitializer {

    @Override
    protected void addSimpleChannelInboundHandlers(ChannelPipeline channelPipeline) {
        channelPipeline.addLast(new WaterSystemMessageEncoder());
        channelPipeline.addLast(new WaterSystemMessageDecoder());
        channelPipeline.addLast(new WaterSystemReadRegisterCommandResponseHandler());
    }

    public WaterSystemNettyChannelInitializer() {

        super(180, 180, 180, null, null);
    }
}
