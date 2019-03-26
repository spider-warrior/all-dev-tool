package cn.t.tool.nettytool.server.listener;


import cn.t.tool.nettytool.server.DaemonServer;

public interface DemonListener {

    void startup(DaemonServer server);

    void close(DaemonServer server);
}
