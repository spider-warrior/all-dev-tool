package cn.t.tool.nettytool.launcher;

import cn.t.tool.nettytool.daemon.DaemonService;
import cn.t.tool.nettytool.daemon.ListenableDaemonService;
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
        if (getDaemonServiceList() != null && !getDaemonServiceList().isEmpty()) {
            logger.info(String.format("server list size: %d", getDaemonServiceList().size()));
            long before = System.currentTimeMillis();
            for (final DaemonService service: getDaemonServiceList()) {
                if(service instanceof ListenableDaemonService) {
                    ((ListenableDaemonService)service).addListener(this);
                }
                startServer(service);
            }
            boolean notTimeout;
            //等待直到超时
            while ((notTimeout = System.currentTimeMillis() - before < timeout) && serverSuccessCount.get() != getDaemonServiceList().size()) {
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
                    if (downDaemonService.size() > 0) {
                        logger.info(stop + ", find down server, size: " + downDaemonService.size());
                        while (downDaemonService.size() > 0) {
                            DaemonService daemonService = downDaemonService.get(0);
                            logger.info("server restart: " + daemonService);
                            if (!stop) {
                                startServer(daemonService);
                                downDaemonService.remove(0);
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
        if (getDaemonServiceList() != null && !getDaemonServiceList().isEmpty()) {
            logger.info(getDaemonServiceList().size() + " servers to stop");
            for (DaemonService server: getDaemonServiceList()) {
                server.close();
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

    public DefaultLauncher() {}
}
