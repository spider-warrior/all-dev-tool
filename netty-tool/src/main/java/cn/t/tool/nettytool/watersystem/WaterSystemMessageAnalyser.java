package cn.t.tool.nettytool.watersystem;

import cn.t.tool.nettytool.codec.ByteBufAnalyser;
import cn.t.tool.nettytool.util.NullMessage;
import cn.t.tool.nettytool.watersystem.entity.ReadRegisterCommandResponse;
import cn.t.tool.nettytool.watersystem.util.WaterSystemUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaterSystemMessageAnalyser extends ByteBufAnalyser {

    private static final Logger logger = LoggerFactory.getLogger(WaterSystemMessageAnalyser.class);

    @Override
    public Object analyse(ChannelHandlerContext ctx, ByteBuf in) {
        in.markReaderIndex();
        byte address = in.readByte();
        byte func = in.readByte();
        byte dataByteLength = in.readByte();
        if(dataByteLength + 2 > in.readableBytes()) {
            logger.info("bytes not enough, expect: {}, actually: {}", (dataByteLength + 2 + 3), (in.readableBytes() + 3));
            return null;
        }
        ReadRegisterCommandResponse readRegisterCommandResponse = new ReadRegisterCommandResponse();
        readRegisterCommandResponse.setAddress(address);
        readRegisterCommandResponse.setFunc(func);
        readRegisterCommandResponse.setDataByteLength(dataByteLength);
        if(dataByteLength > 1) {
            for(int i=0; i<dataByteLength/2; i++) {
                readRegisterCommandResponse.getRegisterDataList().add(in.readShort());
            }
        } else {
            logger.info("no register data available, data byte length: {}", dataByteLength);
        }
        short crc16 = in.readShortLE();
        readRegisterCommandResponse.setCrc16(crc16);
        in.resetReaderIndex();
        byte[] data = new byte[in.readableBytes() - 2];
        in.readBytes(data);
        in.skipBytes(2);
        short calculateCrc = WaterSystemUtil.calculateCrc(data);
        if(crc16 != calculateCrc) {
            logger.error("crc校验错误");
            return NullMessage.getNullMessage();
        } else {
            return readRegisterCommandResponse;
        }
    }
}
