package cn.t.tool.dnstool.protocal;

import cn.t.tool.dnstool.model.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

/**
 * @author yj
 * @since 2020-01-01 10:54
 **/
public class MessageEncoder {
    public void encode(Context context, Message message) throws IOException {
        if(message != null) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte[] output = buffer.array();
            DatagramPacket packet = new DatagramPacket(output, output.length);
            context.write(packet);
        }
    }
}
