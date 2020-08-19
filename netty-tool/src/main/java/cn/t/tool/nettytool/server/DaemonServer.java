package cn.t.tool.nettytool.server;

/**
 * 守护服务
 * */
public interface DaemonServer {

    /**
     * 启动
     * */
    void start();

    /**
     * 结束
     * */
    void close();

}
