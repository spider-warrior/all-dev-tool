package cn.t.tool.dnstool;

import org.xbill.DNS.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author yj
 * @since 2019-12-29 14:37
 **/
public class DnsServer {

    public static void main(String[] args) {
        DnsServer server = new DnsServer();
        server.start();
    }

    private static DatagramSocket socket;
    public DnsServer() {
        //设置socket，监听端口53
        try {
            this.socket = new DatagramSocket(53);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Starting。。。。。。\n");
        while (true) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);
                System.out.println("==========================================================================================");
                //输出客户端的dns请求数据
                InetAddress sourceIpAddr = request.getAddress();
                int sourcePort = request.getPort();
                System.out.println("\nsourceIpAddr = " + sourceIpAddr.toString() + "\nsourcePort = " + sourcePort);
                //分析dns数据包格式
                Message indata = new Message(request.getData());
                System.out.println("\nindata = " + indata.toString());
                Record question = indata.getQuestion();
                System.out.println("question = " + question);
                String domain = indata.getQuestion().getName().toString();
                System.out.println("domain = " + domain);
                InetAddress answerIpAddr;
                Record answer;
                if("sensors.ishansong.com.".equals(domain)) {
                    answerIpAddr = InetAddress.getByAddress(domain, new byte[]{47, 95 ,48, (byte)199});
                } else if("sapi-bstable.bingex.com.".equals(domain)) {
                    answerIpAddr = InetAddress.getByAddress(domain, new byte[]{(byte)172, 23, 6, (byte)196});
                } else {
                    //解析域名
                    answerIpAddr = Address.getByName(domain);
                }
                //由于接收到的请求为A类型，因此应答也为ARecord。查看Record类的继承，发现还有AAAARecord(ipv6)，CNAMERecord等
                answer = new ARecord(question.getName(), question.getDClass(), 64, answerIpAddr);
                Message outdata = (Message)indata.clone();
                outdata.addRecord(answer, Section.ANSWER);
                //发送消息给客户端
                byte[] buf = outdata.toWire();
                DatagramPacket response = new DatagramPacket(buf, buf.length, sourceIpAddr, sourcePort);
                socket.send(response);
            } catch (SocketException e) {
                System.out.println("SocketException:");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IOException:");
                e.printStackTrace();
            }
        }
    }

}
