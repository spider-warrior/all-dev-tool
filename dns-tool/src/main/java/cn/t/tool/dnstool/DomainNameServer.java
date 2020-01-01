package cn.t.tool.dnstool;

import cn.t.tool.dnstool.model.Message;
import cn.t.tool.dnstool.protocal.Context;
import cn.t.tool.dnstool.protocal.DnsMessageDecoder;
import cn.t.tool.dnstool.protocal.MessageEncoder;
import cn.t.tool.dnstool.protocal.MessageHandlerAdapter;
import cn.t.util.common.digital.HexUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author yj
 * @since 2019-12-31 20:48
 **/
@Slf4j
public class DomainNameServer {

    private static final byte[] buffer = new byte[1024];

    private static DnsMessageDecoder messageDecoder = new DnsMessageDecoder();
    private static MessageHandlerAdapter messageHandlerAdapter = new MessageHandlerAdapter();
    private static MessageEncoder messageEncoder = new MessageEncoder();

    public static void main(String[] args) throws IOException {
        SystemPropertiesLoader.loadDefaultProperties();
        DatagramSocket socket = new DatagramSocket(53);
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            log.info("==========================================================================================");
            InetAddress inetAddress = packet.getAddress();
            int port = packet.getPort();
            log.info("message from: sourceIpAddr: {}:{}", inetAddress, port);
            byte[] messageBytes = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), 0, messageBytes, 0, packet.getLength());
            //解析消息
            Message message = messageDecoder.decode(messageBytes);
            if(message != null) {
                Context context = new Context();
                context.setSocket(socket);
                context.setInetAddress(packet.getAddress());
                context.setPort(packet.getPort());
                //处理消息
                Message result = messageHandlerAdapter.handle(context, message);
                if(result != null) {
                    //响应客户端
                    messageEncoder.encode(context, result);
                }
            }
        }
    }
}
