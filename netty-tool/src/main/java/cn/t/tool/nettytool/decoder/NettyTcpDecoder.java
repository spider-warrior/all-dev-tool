package cn.t.tool.nettytool.decoder;

import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import cn.t.tool.nettytool.util.NullMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NettyTcpDecoder extends ByteToMessageDecoder {

    private static Logger logger = LoggerFactory.getLogger(NettyTcpDecoder.class);

    private ByteBufAnalyser byteBufAnalyser;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if(in.isReadable()) {
            int readerIndex = in.readerIndex();
            Object msg = byteBufAnalyser.analyse(ctx, in);
            if(msg == null) {
                logger.info("message is incomplete，reader index reset");
                in.readerIndex(readerIndex);
            } else {
                if(NullMessage.getNullMessage() != msg) {
                    logger.info("produce message success，type: {}", msg.getClass().getSimpleName());
                    out.add(msg);
                } else {
                    logger.info("read a null message，reader index will not reset");
                }
            }
        }
    }

    public NettyTcpDecoder(ByteBufAnalyser byteBufAnalyser) {
        this.byteBufAnalyser = byteBufAnalyser;
    }
}
