package cn.t.tool.nettytool.watersystem;

import cn.t.tool.nettytool.watersystem.constant.FunctionCode;
import cn.t.tool.nettytool.watersystem.entity.ReadRegisterCommand;
import cn.t.tool.nettytool.watersystem.entity.ReadRegisterCommandResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaterSystemReadRegisterCommandResponseHandler extends SimpleChannelInboundHandler<ReadRegisterCommandResponse> {

    private static final Logger logger = LoggerFactory.getLogger(WaterSystemReadRegisterCommandResponseHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ReadRegisterCommandResponse msg) {
        logger.info("read a msg: {}", msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("连接建立成功: {}", ctx.channel().remoteAddress());

        ReadRegisterCommand readRegisterCommand = new ReadRegisterCommand();
        readRegisterCommand.setAddress((byte)0x1);
        readRegisterCommand.setFunc(FunctionCode.READ_REGISTER_CONTENT);
        readRegisterCommand.setRegisterStartAddress((short)0);
        readRegisterCommand.setRegisterCount((short)17);

        ctx.writeAndFlush(readRegisterCommand);
    }

}
