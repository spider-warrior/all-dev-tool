package cn.t.tool.nettytool.server.listener;

import cn.t.tool.nettytool.server.DaemonServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyTcpListener implements DaemonListener {

    private static final Logger logger = LoggerFactory.getLogger(NettyTcpListener.class);

    @Override
    public void startup(DaemonServer server) {
        logger.info(server.getClass() + " start....");
    }

    @Override
    public void close(DaemonServer server) {
        logger.info(server.getClass() + " stop....");
    }
}
