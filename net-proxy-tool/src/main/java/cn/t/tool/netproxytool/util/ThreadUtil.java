package cn.t.tool.netproxytool.util;

import cn.t.tool.netproxytool.monitor.ThreadPoolMonitor;
import cn.t.tool.netproxytool.socks5.constants.Socks5ServerConfig;

import java.util.concurrent.*;

/**
 * 线程工具
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 22:45
 **/
public class ThreadUtil {

    public static final ScheduledExecutorService scheduledExecutorService =  Executors.newScheduledThreadPool(1);

    public static void submitTask(Runnable runnable) {
        Socks5ThreadPoolHolder.THREAD_POOL_EXECUTOR.submit(runnable);
    }

    public static void scheduleTask(Runnable runnable, int initialDelayInSeconds, int periodInSeconds) {
        scheduledExecutorService.scheduleAtFixedRate(runnable, initialDelayInSeconds, periodInSeconds, TimeUnit.SECONDS);
    }

    private static class Socks5ThreadPoolHolder {
        private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new MonitoredThreadPool(
            Socks5ServerConfig.CORE_THREAD_COUNT,
            Socks5ServerConfig.MAX_THREAD_COUNT,
            Socks5ServerConfig.THREAD_TT,
            Socks5ServerConfig.THREAD_TT_TIME_UNIT,
            new ArrayBlockingQueue<>(Socks5ServerConfig.BLOCKING_THREAD_COUNT),
            new MonitoredThreadFactory(Socks5ServerConfig.THREAD_POOL_NAME),
            Socks5ServerConfig.THREAD_POOL_NAME
        );
        static {
            scheduleTask(new ThreadPoolMonitor(Socks5ServerConfig.THREAD_POOL_NAME, THREAD_POOL_EXECUTOR), 3, 5);
        }
    }



}
