package cn.t.tool.nettytool.watersystem;

import cn.t.tool.nettytool.decoder.NettyTcpDecoder;
import cn.t.tool.nettytool.encoer.NettyTcpEncoder;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class WaterSystemNettyChannelBuilder extends NettyChannelInitializerBuilder {

    public WaterSystemNettyChannelBuilder() {
        setIdleState(180, 180, 180);
        setNettyTcpDecoder(() -> new NettyTcpDecoder(new WaterSystemMessageAnalyser()));
        setNettyTcpEncoder(() -> new NettyTcpEncoder(new WaterSystemMessageEncoder()));
        addChannelInboundHandler(() -> {
            List<SimpleChannelInboundHandler> waterSystemReadRegisterCommandResponseHandlerList = new ArrayList<>();
            waterSystemReadRegisterCommandResponseHandlerList.add(new WaterSystemReadRegisterCommandResponseHandler());
            return waterSystemReadRegisterCommandResponseHandlerList;
        });
    }

}
