package cn.t.tool.nettytool.test.util;

import cn.t.tool.nettytool.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

public class ByteBufUtilTest {

    @Test
    public void createByteBufTest() {
        byte[] bs = {1,4,7};
        ByteBuf buf = ByteBufUtil.createByteBuf(bs);
        System.out.println(buf.readByte());
        System.out.println(buf.readByte());
        System.out.println(buf.readByte());
    }


}
