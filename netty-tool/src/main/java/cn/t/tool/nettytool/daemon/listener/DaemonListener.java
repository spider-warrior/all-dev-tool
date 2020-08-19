package cn.t.tool.nettytool.daemon.listener;


import cn.t.tool.nettytool.daemon.DaemonService;

public interface DaemonListener {
    void startup(DaemonService server);
    void close(DaemonService server);
}
