package cn.t.tool.nettytool.launcher;

import cn.t.tool.nettytool.launcher.listener.LauncherListener;
import cn.t.tool.nettytool.server.DaemonServer;

import java.util.List;

public class DefaultLauncherBuilder {

    private int timeout;

    private boolean autoRestart;

    private List<DaemonServer> daemonServerList;

    private List<LauncherListener> launcherListenerList;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setAutoRestart(boolean autoRestart) {
        this.autoRestart = autoRestart;
    }

    public void setDaemonServerList(List<DaemonServer> daemonServerList) {
        this.daemonServerList = daemonServerList;
    }

    public void setLauncherListenerList(List<LauncherListener> launcherListenerList) {
        this.launcherListenerList = launcherListenerList;
    }

    public Launcher build() {
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setTimeout(timeout);
        defaultLauncher.setAutoRestart(autoRestart);
        defaultLauncher.setDaemonServerList(daemonServerList);
        defaultLauncher.setLauncherListenerList(launcherListenerList);
        return defaultLauncher;
    }
}
