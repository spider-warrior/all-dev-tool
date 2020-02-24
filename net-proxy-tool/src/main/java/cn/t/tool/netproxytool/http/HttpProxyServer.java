package cn.t.tool.netproxytool.http;

import cn.t.tool.netproxytool.http.constants.HttpServerConfig;
import cn.t.tool.netproxytool.http.server.initializer.HttpProxyChannelInitializer;
import cn.t.tool.nettytool.launcher.DefaultLauncher;
import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.server.NettyTcpServer;
import cn.t.util.common.LoggerUtil;
import org.slf4j.event.Level;

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
        //设置logback输出级别
        LoggerUtil.setSlf4jRootLoggerLevel(Level.INFO);
        List<DaemonServer> daemonServerList = new ArrayList<>();
        NettyTcpServer proxyServer = new NettyTcpServer("http-proxy-server", HttpServerConfig.SERVER_PORT, new HttpProxyChannelInitializer());
        daemonServerList.add(proxyServer);
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setDaemonServerList(daemonServerList);
        defaultLauncher.startup();
    }
}
