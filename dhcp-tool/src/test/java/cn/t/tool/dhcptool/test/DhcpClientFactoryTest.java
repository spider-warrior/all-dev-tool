package cn.t.tool.dhcptool.test;

import cn.t.tool.dhcptool.DhcpClient;
import cn.t.tool.dhcptool.DhcpClientFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author yj
 * @since 2020-01-20 11:02
 **/
public class DhcpClientFactoryTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        DhcpClientFactory factory = DhcpClientFactory.singleton();
        factory.init();
        InetAddress ia = InetAddress.getLocalHost();
        DhcpClient client = factory.acquireDhcpClient(NetworkInterface.getByInetAddress(ia).getHardwareAddress());
        System.out.println(client.requestClientInfo());
        Thread.sleep(1000000);
    }
}
