package cn.t.tool.netproxytool.socks5.server;

import cn.t.tool.netproxytool.socks5.config.ServerConfig;
import cn.t.tool.netproxytool.socks5.constants.Socks5ServerDaemonConfig;
import cn.t.tool.netproxytool.socks5.server.initializer.LocalToProxyChannelInitializerBuilder;
import cn.t.tool.netproxytool.socks5.util.ServerConfigUtil;
import cn.t.tool.nettytool.launcher.DefaultLauncher;
import cn.t.tool.nettytool.daemon.Daemon;
import cn.t.tool.nettytool.daemon.NettyTcp;
import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代理服务器
 * <a>https://www.cnblogs.com/cc11001100/p/9949729.html</a>
 * <a>https://www.ietf.org/rfc/rfc1928.txt</a>
 *
 * 使用socks5代理的坑，域名在本地解析还是在代理服务器端解析，有些比如google.com就必须在代理服务器端解析
 * <a>https://blog.emacsos.com/use-socks5-proxy-in-curl.html</a>
 *
 * @author yj
 * @since 2020-01-12 13:41
 **/
public class Socks5ProxyServer {
    public static void main(String[] args) {
        Map<AttributeKey<?>, Object> childAttrs = new HashMap<>();
        ServerConfig serverConfig = ServerConfigUtil.loadServerConfig();
        childAttrs.put(Socks5ServerDaemonConfig.SERVER_CONFIG_KEY, serverConfig);
        List<Daemon> daemonServerList = new ArrayList<>();
        NettyTcp proxyServer = new NettyTcp("socks5-proxy-server", Socks5ServerDaemonConfig.SERVER_PORT, new LocalToProxyChannelInitializerBuilder().build(), childAttrs);
        daemonServerList.add(proxyServer);
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setDaemonServiceList(daemonServerList);
        defaultLauncher.startup();
    }
}
