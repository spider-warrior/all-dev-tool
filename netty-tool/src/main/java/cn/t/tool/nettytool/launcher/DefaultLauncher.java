package cn.t.tool.nettytool.launcher;

import cn.t.tool.nettytool.server.DaemonServer;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DefaultLauncher extends AbstractLauncher {

    private static final Logger logger = LoggerFactory.getLogger(DefaultLauncher.class);
    private int timeout = 10000;
    private boolean autoRestart = true;

    public void doStart() {
        //启动所有服务器
        if (getDaemonServerList() != null && !getDaemonServerList().isEmpty()) {
            logger.info(String.format("server list size: %d", getDaemonServerList().size()));
            long before = System.currentTimeMillis();
            boolean notTimeout = true;
            for (final DaemonServer server: getDaemonServerList()) {
                startServer(server);
            }
            //等待直到超时
            while (serverSuccessCount.get() != getDaemonServerList().size() && (notTimeout = System.currentTimeMillis() - before < timeout)) {
                try { Thread.sleep(500); } catch (InterruptedException e) {logger.error("", e);}
            }
            if (!notTimeout) {
                logger.error("Launcher starts timeout!");
            }
        }

        //故障服务器检查
        if (autoRestart && !stop) {
            logger.info("launcher config server restart: true");
            final HashedWheelTimer timer = new HashedWheelTimer();
            final int period = 5;
            timer.newTimeout(new TimerTask() {
                public void run(Timeout timeout) {
                    logger.info("monitor down server....");
                    if (downDaemonServer.size() > 0) {
                        logger.info(stop + ", find down server, size: " + downDaemonServer.size());
                        while (downDaemonServer.size() > 0) {
                            DaemonServer daemonServer = downDaemonServer.get(0);
                            logger.info("server restart: " + daemonServer);
                            if (!stop) {
                                startServer(daemonServer);
                                downDaemonServer.remove(0);
                            }
                            else {
                                break;
                            }
                        }
                    }
                    if (!stop) {
                        timer.newTimeout(this, period, TimeUnit.SECONDS);
                    }
                    else {
                        logger.info("server health check monitor stop....");
                    }
                }
            }, period, TimeUnit.SECONDS);
            timer.start();
        }

    }

    public void doClose() {
        //停止所有服务器
        if (getDaemonServerList() != null && !getDaemonServerList().isEmpty()) {
            logger.info(getDaemonServerList().size() + " servers to stop");
            for (DaemonServer server: getDaemonServerList()) {
                server.close(this);
            }
            while (serverSuccessCount.get() != 0) {
                try {
                    Thread.sleep(500);
                    logger.info("alive alive remain: " + serverSuccessCount.get());
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }
        }
    }

    void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    void setAutoRestart(boolean autoRestart) {
        this.autoRestart = autoRestart;
    }
}
