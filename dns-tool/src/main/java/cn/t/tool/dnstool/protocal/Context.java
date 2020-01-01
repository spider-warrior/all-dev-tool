package cn.t.tool.dnstool.protocal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author yj
 * @since 2020-01-01 11:00
 **/
public class Context {

    private DatagramSocket socket;
    private InetAddress inetAddress;
    private int port;

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void write(byte[] data) throws IOException {
        if(data != null && data.length > 0) {
            DatagramPacket packet = new DatagramPacket(data, data.length);
            packet.setAddress(inetAddress);
            packet.setPort(port);
            socket.send(packet);
        }
    }
}
