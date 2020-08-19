package cn.t.tool.nettytool.daemon;

import cn.t.tool.nettytool.daemon.listener.DaemonListener;

import java.util.ArrayList;
import java.util.List;

public abstract class ListenableDaemonService implements DaemonService {
    protected List<DaemonListener> daemonListenerList = new ArrayList<>();
    public void addListener(DaemonListener daemonListener) {
        if(daemonListener != null) {
            daemonListenerList.add(daemonListener);
        }
    }
}
