package cn.t.tool.netproxytool.util;

/**
 * @author <a href="mailto:yangjian@ifenxi.com">研发部-杨建</a>
 * @version V1.0
 * @since 2021-06-23 21:45
 **/
public class NetProxyUtil {
    public static String buildProxyConnectionName(String clientHost, int clientPort, String targetHost, int targetPort) {
        return clientHost + ":" + clientPort + " -> " + targetHost + ":" + targetPort;
    }
}
