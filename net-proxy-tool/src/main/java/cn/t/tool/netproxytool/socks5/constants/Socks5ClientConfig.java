package cn.t.tool.netproxytool.socks5.constants;

import io.netty.handler.logging.LogLevel;
import io.netty.util.AttributeKey;

/**
 * 服务配置
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:00
 **/
public class Socks5ClientConfig {
    public static final LogLevel LOGGING_HANDLER_LOGGER_LEVEL = LogLevel.DEBUG;

    public static final int SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS = 0;
    public static final int SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS = 0;
    public static final int SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS = 10;

    public static final byte[] SUPPORT_METHODS = {Socks5Method.NO_AUTHENTICATION_REQUIRED.rangeStart, Socks5Method.USERNAME_PASSWORD.rangeStart};

    public static final AttributeKey<String> TARGET_HOST_KEY = AttributeKey.newInstance("targetHost");
    public static final AttributeKey<Short> TARGET_PORT_KEY = AttributeKey.newInstance("targetPort");

    public static final AttributeKey<String> USERNAME_KEY = AttributeKey.newInstance("username");
    public static final AttributeKey<String> PASSWORD_KEY = AttributeKey.newInstance("password");



}
