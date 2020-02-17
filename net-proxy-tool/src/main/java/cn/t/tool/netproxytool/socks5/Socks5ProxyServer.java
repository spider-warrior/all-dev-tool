package cn.t.tool.netproxytool.socks5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * socks5代理服务器简单实现
 * <a>https://www.ietf.org/rfc/rfc1928.txt</a>
 *
 * 使用socks5代理的坑，域名在本地解析还是在代理服务器端解析，有些比如google.com就必须在代理服务器端解析
 * <a>https://blog.emacsos.com/use-socks5-proxy-in-curl.html</a>
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-17 22:34
 **/
public class Socks5ProxyServer implements Runnable {

    // 服务监听在哪个端口上
    private final int serviceListenerPort;

    // 能够允许的最大客户端数量
    private final int maxClientNum;

    //ip地址
    private final String serverIpAddress;

    // 用于统计客户端的数量
    private final AtomicInteger clientNumCount = new AtomicInteger();


    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serviceListenerPort);
            while (true) {
                Socket socket = serverSocket.accept();
                if (clientNumCount.get() >= maxClientNum) {
                    Socks5LogUtil.log("client num run out.");
                    continue;
                }
                Socks5LogUtil.log("new client, ip=%s:%d, current client count=%s", socket.getInetAddress(), socket.getPort(), clientNumCount.get());
                clientNumCount.incrementAndGet();
                new Thread(new ClientHandler(socket, this), "client-handler-" + UUID.randomUUID().toString()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int decrementAndGet() {
        return clientNumCount.decrementAndGet();
    }

    public int incrementAndGet() {
        return clientNumCount.incrementAndGet();
    }

    public Socks5ProxyServer(int serviceListenerPort, int maxClientNum, String serverIpAddress) {
        this.serviceListenerPort = serviceListenerPort;
        this.maxClientNum = maxClientNum;
        this.serverIpAddress = serverIpAddress;
    }

    public int getServiceListenerPort() {
        return serviceListenerPort;
    }

    public int getMaxClientNum() {
        return maxClientNum;
    }

    public String getServerIpAddress() {
        return serverIpAddress;
    }
}
