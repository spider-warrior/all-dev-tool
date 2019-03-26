package cn.t.tool.nettytool.launcher;

import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.launcher.listener.LauncherListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractLauncher implements Launcher {

    protected volatile boolean stop = false;
    protected AtomicInteger serverSuccessCount = new AtomicInteger(0);
    private static final Logger logger = LoggerFactory.getLogger(AbstractLauncher.class);
    private List<DaemonServer> daemonServerList;
    protected List<DaemonServer> downDaemonServer = new Vector<>();
    private List<LauncherListener> launcherListenerList;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 启动
     * */
    public void startup() {
        logger.info("launcher begin to startup");
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        doStart();
        //回调监听器
        if (launcherListenerList != null && !launcherListenerList.isEmpty()) {
            logger.info(String.format("launcher listener size: %d, begin to call launcher listeners",launcherListenerList.size()));
            for (LauncherListener listener: launcherListenerList) {
                listener.startup(this);
            }
        }
        logger.info("launcher startup successfully");
    }

    public abstract void doStart();

    /**
     * 关闭
     * */
    public void close() {
        logger.info("launcher begin to shutdown");
        stop = true;
        doClose();
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Executor Service shutdown error", e);
            executorService.shutdownNow();
        }
        //回调监听器
        if (launcherListenerList != null && !launcherListenerList.isEmpty()) {
            for (LauncherListener listener: launcherListenerList) {
                try { listener.close(this); } catch (Exception e) { }
            }
        }
        logger.info("launcher shutdown successfully");
    }

    public void startServer(DaemonServer server) {
        if (!executorService.isShutdown()) {
            executorService.submit(() -> server.start(this));
        }
    }

    public abstract void doClose();

    @Override
    public void serverStartSuccess(DaemonServer server) {
        serverSuccessCount.addAndGet(1);
        logger.info("server alive count: " + serverSuccessCount.get());
    }

    @Override
    public void serverShutdownSuccess(DaemonServer server) {
        serverSuccessCount.addAndGet(-1);
        downDaemonServer.add(server);
    }

    public List<DaemonServer> getDaemonServerList() {
        return daemonServerList;
    }

    public AbstractLauncher setDaemonServerList(List<DaemonServer> daemonServerList) {
        this.daemonServerList = daemonServerList;
        return this;
    }

    public List<LauncherListener> getLauncherListenerList() {
        return launcherListenerList;
    }

    public AbstractLauncher setLauncherListenerList(List<LauncherListener> launcherListenerList) {
        this.launcherListenerList = launcherListenerList;
        return this;
    }
}
