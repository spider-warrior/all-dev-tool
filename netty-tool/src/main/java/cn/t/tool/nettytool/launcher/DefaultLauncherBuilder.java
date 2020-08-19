package cn.t.tool.nettytool.launcher;

import cn.t.tool.nettytool.launcher.listener.LauncherListener;
import cn.t.tool.nettytool.daemon.DaemonService;

import java.util.List;

public class DefaultLauncherBuilder {

    private int timeout;

    private boolean autoRestart;

    private List<DaemonService> daemonServiceList;

    private List<LauncherListener> launcherListenerList;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setAutoRestart(boolean autoRestart) {
        this.autoRestart = autoRestart;
    }

    public void setDaemonServiceList(List<DaemonService> daemonServiceList) {
        this.daemonServiceList = daemonServiceList;
    }

    public void setLauncherListenerList(List<LauncherListener> launcherListenerList) {
        this.launcherListenerList = launcherListenerList;
    }

    public Launcher build() {
        DefaultLauncher defaultLauncher = new DefaultLauncher();
        defaultLauncher.setTimeout(timeout);
        defaultLauncher.setAutoRestart(autoRestart);
        defaultLauncher.setDaemonServiceList(daemonServiceList);
        defaultLauncher.setLauncherListenerList(launcherListenerList);
        return defaultLauncher;
    }
}
