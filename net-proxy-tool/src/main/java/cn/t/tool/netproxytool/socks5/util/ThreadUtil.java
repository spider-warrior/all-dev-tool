package cn.t.tool.netproxytool.socks5.util;

import cn.t.tool.netproxytool.socks5.constants.Socks5ServerConfig;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程工具
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 22:45
 **/
public class ThreadUtil {

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
        Socks5ServerConfig.CORE_THREAD_COUNT,
        Socks5ServerConfig.MAX_THREAD_COUNT,
        Socks5ServerConfig.THREAD_TT,
        Socks5ServerConfig.THREAD_TT_TIME_UNIT,
        new ArrayBlockingQueue<>(Socks5ServerConfig.BLOCKING_THREAD_COUNT)
    );

    public static void submitTask(Runnable runnable) {
        THREAD_POOL_EXECUTOR.submit(runnable);
    }

}
