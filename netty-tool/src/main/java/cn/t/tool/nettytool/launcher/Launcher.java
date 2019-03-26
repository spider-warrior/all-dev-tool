package cn.t.tool.nettytool.launcher;

import cn.t.tool.nettytool.server.DaemonServer;

/**
 * 启动器
 * */
public interface Launcher {

    /**
     * 启动
     * */
    void startup();

    /**
     * 停止
     * */
    void close();

    /**
     * 启动器启动回调
     * */
    void serverStartSuccess(DaemonServer server);

    /**
     * 启动器停止回调
     * */
    void serverShutdownSuccess(DaemonServer server);
}
