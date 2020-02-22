package cn.t.tool.netproxytool.constants;

import cn.t.util.common.SystemUtil;

/**
 * 服务器配置
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 23:00
 **/
public class ServerConfig {
    public static final String SERVER_HOST = SystemUtil.getLocalIp();
    public static final byte[] SERVER_HOST_BYTES = SystemUtil.convertHostToBytes(SERVER_HOST);
    public static final short PORT = 10086;
}
