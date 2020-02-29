package cn.t.tool.netproxytool.socks5.constants;

import io.netty.handler.logging.LogLevel;

import java.util.concurrent.TimeUnit;

/**
 * 代理配置
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:00
 **/
public class Socks5ProxyConfig {

    public static final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
    public static final String PROXY_THREAD_POOL_NAME = "socks5-proxy";
    public static final int PROXY_CORE_THREAD_COUNT = (PROCESSOR_COUNT < 4 ? 2 : PROCESSOR_COUNT) * 6;
    public static final int PROXY_BLOCKING_THREAD_COUNT = PROXY_CORE_THREAD_COUNT / 3;
    public static final int PROXY_MAX_THREAD_COUNT = (PROXY_CORE_THREAD_COUNT + PROXY_BLOCKING_THREAD_COUNT) * 2;
    public static final int PROXY_THREAD_TT = 10;
    public static final TimeUnit PROXY_THREAD_TT_TIME_UNIT = TimeUnit.SECONDS;

    public static final int SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS = 0;
    public static final int SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS = 0;
    public static final int SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS = 15;

    public static final LogLevel LOGGING_HANDLER_LOGGER_LEVEL = LogLevel.DEBUG;
}
