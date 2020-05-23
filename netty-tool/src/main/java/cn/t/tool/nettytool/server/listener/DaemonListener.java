package cn.t.tool.nettytool.server.listener;


import cn.t.tool.nettytool.server.DaemonServer;

public interface DaemonListener {
    void startup(DaemonServer server);
    void close(DaemonServer server);
}
