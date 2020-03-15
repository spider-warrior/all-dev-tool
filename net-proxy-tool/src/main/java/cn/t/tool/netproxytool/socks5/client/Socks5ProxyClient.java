package cn.t.tool.netproxytool.socks5.client;

import cn.t.tool.netproxytool.socks5.client.initializer.ClientChannelInitializerBuilder;
import cn.t.tool.nettytool.client.NettyTcpClient;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;

/**
 * 代理客户端
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-03-14 18:42
 **/
public class Socks5ProxyClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        short port = 10086;
        String clientName =  "client -> " + host + ":" + port;
        NettyChannelInitializer channelInitializer = new ClientChannelInitializerBuilder(host, port).build();
        NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, host, port, channelInitializer);
        new Thread(() -> nettyTcpClient.start(null)).start();
    }
}
