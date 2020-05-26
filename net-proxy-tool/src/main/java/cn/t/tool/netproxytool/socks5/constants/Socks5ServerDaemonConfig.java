package cn.t.tool.netproxytool.socks5.constants;

import cn.t.tool.netproxytool.socks5.config.ServerConfig;
import cn.t.util.common.SystemUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.util.AttributeKey;

/**
 * 服务配置
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:00
 **/
public class Socks5ServerDaemonConfig {
    public static final String SERVER_HOST = SystemUtil.getLocalIpV4(true);
    public static final byte[] SERVER_HOST_BYTES = SystemUtil.convertHostToBytes(SERVER_HOST);
    public static final short SERVER_PORT = 10086;

    public static final LogLevel LOGGING_HANDLER_LOGGER_LEVEL = LogLevel.DEBUG;

    public static final int SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS = 0;
    public static final int SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS = 0;
    public static final int SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS = 60;

    public static final String SOCKS5_SERVER_HOME_KEY = "SOCKS5_HOME";
    public static final String SOCKS5_SERVER_USERS_CONFIG_FILE = "users";

    public static final AttributeKey<String> CHANNEL_USERNAME = AttributeKey.newInstance("channel_username");

    public static final AttributeKey<ServerConfig> SERVER_CONFIG_KEY = AttributeKey.newInstance("server_config_key");
}
