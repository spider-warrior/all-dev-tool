package cn.t.tool.nettytool.server;

import cn.t.tool.nettytool.launcher.Launcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDaemonServer implements DaemonServer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDaemonServer.class);

    protected String name;
    protected int port;

    @Override
    public void start(Launcher launcher) {
        doStart(launcher);
    }

    @Override
    public void close(Launcher launcher) {
        doClose(launcher);
    }

    /**
     * 服务器会阻塞在这里
     * */
    public abstract void doStart(Launcher launcher);

    /**
     *
     * */
    public abstract void doClose(Launcher launcher);

    public AbstractDaemonServer () {}

    public AbstractDaemonServer(int port) {
        this.port = port;
    }

    public AbstractDaemonServer(String name, int port) {
        this.name = name;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
