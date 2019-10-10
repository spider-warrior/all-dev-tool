package cn.t.tool.nettytool.encoer;

import cn.t.tool.nettytool.codec.ByteBufEncoder;
import cn.t.util.common.ArrayUtil;
import cn.t.util.common.CollectionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NettyTcpEncoder extends MessageToByteEncoder<Object> {

    private static final Logger logger = LoggerFactory.getLogger(NettyTcpEncoder.class);

    private final List<ByteBufEncoder> byteBufEncoderList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        if(msg != null) {
            ByteBufEncoder byteBufEncoder = selectByteBufEncoder(msg);
            if(byteBufEncoder != null) {
                byteBufEncoder.encode(out, msg, ctx);
            } else {
                logger.error("为找到消息编码器, 消息类型: {}", msg.getClass().getName());
            }
        } else {
            logger.warn("msg is null, ignored!");
        }
    }

    public NettyTcpEncoder(List<ByteBufEncoder> byteBufEncoderList) {
        if(!CollectionUtil.isEmpty(byteBufEncoderList)) {
            this.byteBufEncoderList.addAll(byteBufEncoderList);
        }
    }

    public NettyTcpEncoder(ByteBufEncoder... byteBufEncoders) {
        if(!ArrayUtil.isEmpty(byteBufEncoders)) {
            this.byteBufEncoderList.addAll(Arrays.asList(byteBufEncoders));
        }
    }

    private ByteBufEncoder selectByteBufEncoder(Object msg) {
        for(ByteBufEncoder<?> encoder: byteBufEncoderList) {
            if(encoder.support(msg)) {
                return encoder;
            }
        }
        return null;
    }


}
