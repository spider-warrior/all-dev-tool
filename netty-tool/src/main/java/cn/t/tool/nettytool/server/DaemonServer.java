package cn.t.tool.nettytool.server;

import cn.t.tool.nettytool.launcher.Launcher;

/**
 * 守护服务
 * */
public interface DaemonServer {

    /**
     * 启动
     * */
    void start(Launcher launcher);

    /**
     * 结束
     * */
    void close(Launcher launcher);

}
