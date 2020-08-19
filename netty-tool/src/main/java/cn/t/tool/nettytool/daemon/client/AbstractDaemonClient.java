package cn.t.tool.nettytool.daemon.client;

import cn.t.tool.nettytool.daemon.ListenableDaemonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDaemonClient extends ListenableDaemonService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDaemonClient.class);

    protected String name;
    protected String host;
    protected int port;

    @Override
    public void start() {
        logger.debug("client: {} is going to start", name);
        doStart();
        logger.debug("client: {} is started", name);
    }

    @Override
    public void close() {
        logger.debug("client: {} is going to stop", name);
        doClose();
        logger.debug("client: {} is stopped", name);
    }

    /**
     * 服务器会阻塞在这里
     * */
    public abstract void doStart();

    /**
     *
     * */
    public abstract void doClose();

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
