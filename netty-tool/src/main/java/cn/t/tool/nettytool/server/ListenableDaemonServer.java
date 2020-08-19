package cn.t.tool.nettytool.server;

import cn.t.tool.nettytool.server.listener.DaemonListener;

import java.util.ArrayList;
import java.util.List;

public abstract class ListenableDaemonServer implements DaemonServer {
    protected List<DaemonListener> daemonListenerList = new ArrayList<>();
    public void addListener(DaemonListener daemonListener) {
        if(daemonListener != null) {
            daemonListenerList.add(daemonListener);
        }
    }
}
