package cn.t.tool.netproxytool.http;

import cn.t.tool.netproxytool.http.config.Socks5ClientConfig;
import cn.t.tool.netproxytool.http.constants.HttpProxyServerConfig;
import cn.t.tool.netproxytool.http.server.initializer.HttpProxyServerViaSocks5ChannelInitializerBuilder;
import cn.t.tool.netproxytool.http.util.Socks5ClientConfigUtil;
import cn.t.tool.nettytool.launcher.DefaultLauncher;
import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.server.NettyTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * http代理服务器作socks5客户端
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-24 11:05
 **/
public class HttpProxyServerViaSocks5 {
    public static void main(String[] args) {
        Socks5ClientConfig socks5ClientConfig = new Socks5ClientConfig();
        if(args.length < 2) {
            System.out.println("参数格式: host:port username:password security");
            System.exit(1);
        }
        Socks5ClientConfigUtil.analyseAndConfigSocks5Server(socks5ClientConfig, args[0]);
        Socks5ClientConfigUtil.analyseAndConfigUser(socks5ClientConfig, args[1]);
        if(args.length > 2) {
            Socks5ClientConfigUtil.analyseAndConfigSecurity(socks5ClientConfig, args[2]);
        }
        List<DaemonServer> daemonServerList = new ArrayList<>();
        NettyTcpServer proxyServer = new NettyTcpServer("http-proxy-server-via-socks5", HttpProxyServerConfig.SERVER_PORT, new HttpProxyServerViaSocks5ChannelInitializerBuilder(socks5ClientConfig).build());
        daemonServerList.add(proxyServer);
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setDaemonServerList(daemonServerList);
        defaultLauncher.startup();
    }
}
