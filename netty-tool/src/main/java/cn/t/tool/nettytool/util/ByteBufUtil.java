package cn.t.tool.nettytool.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBufUtil {

    public static ByteBuf createByteBuf(byte[] data) {
        return Unpooled.copiedBuffer(data);
    }
}
