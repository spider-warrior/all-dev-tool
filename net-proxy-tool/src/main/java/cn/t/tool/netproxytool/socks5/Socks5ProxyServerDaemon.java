package cn.t.tool.netproxytool.socks5;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-17 23:14
 **/
public class Socks5ProxyServerDaemon {
    public static void main(String[] args) {
        Socks5ProxyServer server = new Socks5ProxyServer(10086,100, "106.13.61.214");
        new Thread(server).start();
    }
}
