package cn.t.tool.netproxytool.util;

import cn.t.tool.netproxytool.monitor.ThreadPoolMonitor;
import cn.t.tool.netproxytool.socks5.constants.Socks5ProxyConfig;

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

    public static void submitProxyTask(Runnable runnable) {
        Socks5ThreadPoolHolder.PROXY_THREAD_POOL_EXECUTOR.submit(runnable);
    }

    public static void scheduleTask(Runnable runnable, int initialDelayInSeconds, int periodInSeconds) {
        scheduledExecutorService.scheduleAtFixedRate(runnable, initialDelayInSeconds, periodInSeconds, TimeUnit.SECONDS);
    }

    public static void closeThreadPool() {
        if(!Socks5ThreadPoolHolder.PROXY_THREAD_POOL_EXECUTOR.isShutdown()) {
            Socks5ThreadPoolHolder.PROXY_THREAD_POOL_EXECUTOR.shutdownNow();
        }
        if(!scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdownNow();
        }
    }

    private static class Socks5ThreadPoolHolder {
        private static final ThreadPoolExecutor PROXY_THREAD_POOL_EXECUTOR = new MonitoredThreadPool(
            Socks5ProxyConfig.PROXY_CORE_THREAD_COUNT,
            Socks5ProxyConfig.PROXY_MAX_THREAD_COUNT,
            Socks5ProxyConfig.PROXY_THREAD_TT,
            Socks5ProxyConfig.PROXY_THREAD_TT_TIME_UNIT,
            new ArrayBlockingQueue<>(Socks5ProxyConfig.PROXY_BLOCKING_THREAD_COUNT),
            new MonitoredThreadFactory(Socks5ProxyConfig.PROXY_THREAD_POOL_NAME),
            Socks5ProxyConfig.PROXY_THREAD_POOL_NAME
        );
        static {
            scheduleTask(new ThreadPoolMonitor(Socks5ProxyConfig.PROXY_THREAD_POOL_NAME, PROXY_THREAD_POOL_EXECUTOR), 3, 5);
        }
    }



}
