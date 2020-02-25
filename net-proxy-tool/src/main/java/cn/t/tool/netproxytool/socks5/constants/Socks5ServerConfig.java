package cn.t.tool.netproxytool.socks5.constants;

import cn.t.util.common.SystemUtil;

import java.util.concurrent.TimeUnit;

/**
 * 服务配置
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:00
 **/
public class Socks5ServerConfig {
    public static final String SERVER_HOST = SystemUtil.getLocalIpV4(true);
    public static final byte[] SERVER_HOST_BYTES = SystemUtil.convertHostToBytes(SERVER_HOST);
    public static final short SERVER_PORT = 10086;

    public static final int SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS = 0;
    public static final int SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS = 0;
    public static final int SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS = 10;
}
