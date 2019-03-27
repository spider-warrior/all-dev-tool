package cn.t.tool.nettytool.watersystem.test;

import cn.t.tool.nettytool.client.NettyTcpClient;
import cn.t.tool.nettytool.launcher.DefaultLauncher;
import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.watersystem.WaterSystemNettyChannelInitializer;

import java.util.ArrayList;
import java.util.List;

public class WaterSystemClientTest {

    public static void main(String[] args) {
        WaterSystemNettyChannelInitializer channelInitializer = new WaterSystemNettyChannelInitializer();
        NettyTcpClient tcpClient = new NettyTcpClient("水压", "www.baidu.com", 80, channelInitializer);
        List<DaemonServer> daemonServerList = new ArrayList<>();
        daemonServerList.add(tcpClient);
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setDaemonServerList(daemonServerList);
        defaultLauncher.doStart();
    }
}
