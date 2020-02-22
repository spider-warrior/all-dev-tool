package cn.t.tool.netproxytool.server;

import cn.t.tool.netproxytool.constants.ServerConfig;
import cn.t.tool.netproxytool.server.initializer.LocalToProxyChannelInitializerBuilder;
import cn.t.tool.nettytool.launcher.DefaultLauncher;
import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.server.NettyTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yj
 * @since 2020-01-12 13:41
 **/
public class Socks5ProxyServer {
    public static void main(String[] args) {
        List<DaemonServer> daemonServerList = new ArrayList<>();
        NettyTcpServer proxyServer = new NettyTcpServer("socks5-proxy", ServerConfig.PORT, new LocalToProxyChannelInitializerBuilder().build());
        daemonServerList.add(proxyServer);
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setDaemonServerList(daemonServerList);
        defaultLauncher.startup();
    }
}
