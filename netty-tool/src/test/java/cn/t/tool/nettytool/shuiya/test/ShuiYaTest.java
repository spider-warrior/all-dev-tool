package cn.t.tool.nettytool.shuiya.test;

import cn.t.tool.nettytool.launcher.DefaultLauncher;
import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.server.NettyTcpSever;
import cn.t.tool.nettytool.shuiya.ShuiYaNettyChannelInitializer;

import java.util.ArrayList;
import java.util.List;

public class ShuiYaTest {

    public static void main(String[] args) {
        ShuiYaNettyChannelInitializer channelInitializer = new ShuiYaNettyChannelInitializer();
        NettyTcpSever tcpSever = new NettyTcpSever("水压", 7600, channelInitializer);
        List<DaemonServer> daemonServerList = new ArrayList<>();
        daemonServerList.add(tcpSever);
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setDaemonServerList(daemonServerList);
        defaultLauncher.doStart();
    }
}
