package cn.t.tool.nettytool.watersystem.test;

import cn.t.tool.nettytool.launcher.DefaultLauncher;
import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.server.NettyTcpSever;
import cn.t.tool.nettytool.watersystem.WaterSystemNettyChannelInitializer;

import java.util.ArrayList;
import java.util.List;

public class WaterSystemServerTest {

    public static void main(String[] args) {
        WaterSystemNettyChannelInitializer channelInitializer = new WaterSystemNettyChannelInitializer();
        NettyTcpSever tcpSever = new NettyTcpSever("水压", 7600, channelInitializer);
        List<DaemonServer> daemonServerList = new ArrayList<>();
        daemonServerList.add(tcpSever);
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setDaemonServerList(daemonServerList);
        defaultLauncher.doStart();
    }
}
