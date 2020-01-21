package cn.t.tool.dhcptool.protocol;

import cn.t.tool.dhcptool.DhcpClient;
import cn.t.tool.dhcptool.constants.OperationType;
import cn.t.tool.dhcptool.model.DhcpMessage;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yj
 * @since 2020-01-20 15:01
 **/
public class DhcpMessageSender {
    private AtomicInteger id = new AtomicInteger(0);
    private static final DhcpMessageEncoder encoder = new DhcpMessageEncoder();
    private DhcpMessageReceiver dhcpMessageReceiver;
    private DatagramSocket datagramSocket = null;
    public DhcpMessageSender(DhcpMessageReceiver dhcpMessageReceiver) throws SocketException {
        datagramSocket = new DatagramSocket();
        this.dhcpMessageReceiver = dhcpMessageReceiver;
    }
    public synchronized void destroy() {
        if(datagramSocket != null && !datagramSocket.isClosed()) {
            datagramSocket.close();
        }
    }
    private void send(byte[] content) throws IOException {
        DatagramPacket dp = new DatagramPacket(content, content.length, InetAddress.getByName("255.255.255.255"), 67);
        datagramSocket.send(dp);
    }

    public void discover(DhcpMessage message, DhcpClient client) throws IOException {
        message.setOperationType(OperationType.DISCOVER);
        Integer txId = id.incrementAndGet();
        message.setTxId(txId);
        dhcpMessageReceiver.bindClientClient(txId, client);
        send(encoder.encode(message));
    }

    public void request(DhcpMessage message, DhcpClient client) throws IOException {
        message.setOperationType(OperationType.REQUEST);
        dhcpMessageReceiver.bindClientClient(message.getTxId(), client);
        send(encoder.encode(message));
    }

}
