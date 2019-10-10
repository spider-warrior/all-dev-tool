package cn.t.tool.nettytool.watersystem.test;

import cn.t.tool.nettytool.launcher.DefaultLauncherBuilder;
import cn.t.tool.nettytool.launcher.Launcher;
import cn.t.tool.nettytool.launcher.listener.DefaultLauncherListener;
import cn.t.tool.nettytool.launcher.listener.LauncherListener;
import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.server.NettyTcpServer;
import cn.t.tool.nettytool.server.listener.DemonListener;
import cn.t.tool.nettytool.server.listener.NettyTcpListener;
import cn.t.tool.nettytool.watersystem.WaterSystemNettyChannelBuilder;

import java.util.ArrayList;
import java.util.List;

public class WaterSystemServerTest {

    public static void main(String[] args) {

        WaterSystemNettyChannelBuilder channelInitializer = new WaterSystemNettyChannelBuilder();
        NettyTcpServer tcpSever = new NettyTcpServer("水压", 7600, channelInitializer.build());

        List<DemonListener> demonListenerList = new ArrayList<>();
        demonListenerList.add(new NettyTcpListener());
        tcpSever.setDemonListenerList(demonListenerList);

        List<DaemonServer> daemonServerList = new ArrayList<>();
        daemonServerList.add(tcpSever);

        List<LauncherListener> launcherListenerList = new ArrayList<>();
        launcherListenerList.add(new DefaultLauncherListener());

        DefaultLauncherBuilder defaultLauncherBuilder = new DefaultLauncherBuilder();
        defaultLauncherBuilder.setTimeout(10000);
        defaultLauncherBuilder.setAutoRestart(true);
        defaultLauncherBuilder.setDaemonServerList(daemonServerList);
        defaultLauncherBuilder.setLauncherListenerList(launcherListenerList);
        Launcher launch = defaultLauncherBuilder.build();
        launch.startup();
    }
}
