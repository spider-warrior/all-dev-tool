package cn.t.tool.netproxytool.http;

import cn.t.tool.netproxytool.http.constants.HttpProxyServerConfig;
import cn.t.tool.netproxytool.http.server.initializer.HttpProxyServerChannelInitializerBuilder;
import cn.t.tool.nettytool.launcher.DefaultLauncher;
import cn.t.tool.nettytool.daemon.Daemon;
import cn.t.tool.nettytool.daemon.NettyTcp;

import java.util.ArrayList;
import java.util.List;

/**
 * http代理
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-24 11:05
 **/
public class HttpProxyServer {
    public static void main(String[] args) {
        List<Daemon> daemonServerList = new ArrayList<>();
        NettyTcp proxyServer = new NettyTcp("http-proxy-server", HttpProxyServerConfig.SERVER_PORT, new HttpProxyServerChannelInitializerBuilder().build());
        daemonServerList.add(proxyServer);
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setDaemonServiceList(daemonServerList);
        defaultLauncher.startup();
    }
}
