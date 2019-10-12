package cn.t.tool.nettytool.watersystem;

import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;

public class WaterSystemNettyChannelBuilder extends NettyChannelInitializerBuilder {

    public WaterSystemNettyChannelBuilder() {
        setIdleState(180, 180, 180);
        setByteBufAnalyserSupplier(WaterSystemMessageAnalyser::new);
        addEncoderListsSupplier(WaterSystemMessageEncoder::new);
        addSimpleChannelInboundHandlerListSupplier(WaterSystemReadRegisterCommandResponseHandler::new);
    }

}
