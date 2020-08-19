package cn.t.tool.nettytool.daemon;

/**
 * 守护服务
 * */
public interface DaemonService {

    /**
     * 启动
     * */
    void start();

    /**
     * 结束
     * */
    void close();

}
