package cn.t.tool.nettytool.shuiya;

import cn.t.tool.nettytool.NettyChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class ShuiYaNettyChannelInitializer extends NettyChannelInitializer {

    @Override
    protected void addSimpleChannelInboundHandlers(ChannelPipeline channelPipeline) {
        channelPipeline.addLast(new ShuiYaMessageHandler());
    }

    public ShuiYaNettyChannelInitializer() {

        super(180, 180, 180, null, null);
    }
}
