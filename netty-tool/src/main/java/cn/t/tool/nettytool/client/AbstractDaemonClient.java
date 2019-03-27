package cn.t.tool.nettytool.client;

import cn.t.tool.nettytool.launcher.Launcher;
import cn.t.tool.nettytool.server.DaemonServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDaemonClient implements DaemonServer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDaemonClient.class);

    protected String name;
    protected String host;
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

    public AbstractDaemonClient () {}

    public AbstractDaemonClient(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
