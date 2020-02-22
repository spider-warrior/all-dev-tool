package cn.t.tool.netproxytool.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程工具
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 22:45
 **/
public class ThreadUtil {

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
        Runtime.getRuntime().availableProcessors(),
        Runtime.getRuntime().availableProcessors() * 2,
        10,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors() * 2)
    );

    public static final void submitTask(Runnable runnable) {
        THREAD_POOL_EXECUTOR.submit(runnable);
    }

}
