package cn.t.tool.netproxytool.constants;

import cn.t.util.common.SystemUtil;

import java.util.concurrent.TimeUnit;

/**
 * 服务器配置
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:00
 **/
public class ServerConfig {
    public static final String SERVER_HOST = SystemUtil.getLocalIpV4(true);
    public static final byte[] SERVER_HOST_BYTES = SystemUtil.convertHostToBytes(SERVER_HOST);
    public static final short SERVER_PORT = 10086;
    public static final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int CORE_THREAD_COUNT = PROCESSOR_COUNT < 4 ? 2 : PROCESSOR_COUNT;
    public static final int MAX_THREAD_COUNT = CORE_THREAD_COUNT * 5;
    public static final int BLOCKING_THREAD_COUNT = MAX_THREAD_COUNT;
    public static final int THREAD_TT = 10;
    public static final TimeUnit THREAD_TT_TIME_UNIT = TimeUnit.SECONDS;
}
