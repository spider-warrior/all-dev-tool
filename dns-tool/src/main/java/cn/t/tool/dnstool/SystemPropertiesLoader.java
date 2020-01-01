package cn.t.tool.dnstool;

/**
 * @author yj
 * @since 2020-01-01 12:27
 **/
public class SystemPropertiesLoader {
    public static void loadDefaultProperties() {
        //sun.net.spi.nameservice.provider.<n>=<default|dns,sun|...> 用于设置域名服务提供者
        //default的时候调用系统自带的DNS
        //dns,sun的时候，会调用sun.net.spi.nameservice.nameservers=<server1_ipaddr,server2_ipaddr ...>指定的DNS来解析
        System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
        System.setProperty("sun.net.spi.nameservice.nameservers", "192.168.10.48,192.168.10.77,219.141.136.10");
    }
}
