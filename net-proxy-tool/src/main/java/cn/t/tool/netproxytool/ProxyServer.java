package cn.t.tool.netproxytool;

import cn.t.tool.netproxytool.server.initializer.LocalToProxyChannelInitializerBuilder;
import cn.t.tool.nettytool.launcher.DefaultLauncher;
import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.server.NettyTcpServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yj
 * @since 2020-01-12 16:34
 **/
public class ProxyServer {
    public static void main(String[] args) throws IOException {
        try(
            InputStream is = null;
        ) {

        }
        List<DaemonServer> daemonServerList = new ArrayList<>();
        daemonServerList.add(new NettyTcpServer("tcp-proxy-server", 1080, new LocalToProxyChannelInitializerBuilder().build()));
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setDaemonServerList(daemonServerList);
        defaultLauncher.startup();
    }
}
