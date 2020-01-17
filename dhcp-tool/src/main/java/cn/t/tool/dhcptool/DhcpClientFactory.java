package cn.t.tool.dhcptool;

/**
 * dhcp客户端工厂
 *
 * @author yj
 * @since 2020-01-17 13:24
 **/
public abstract class DhcpClientFactory {

    private static volatile DhcpClientFactory singleton = null;

    /**
     * 获取全局唯一client工厂实例
     * @return 客户端单例工厂
     */
    public static DhcpClientFactory singleton() {
        if(singleton != null) {
            return singleton;
        } else {
            singleton = initSingletonDhcpClientFactory();
            return singleton;
        }
    }

    public DhcpClient acquireDhcpClient() {

        //todo
        return null;
    }

    /**
     * 工厂初始化
     */
    public void init() {

    }

    /**
     * 工厂关闭
     */
    public void destroy() {

    }

    /**
     * 初始化客户端单例工厂
     * @return 客户端单例工厂
     */
    private static DhcpClientFactory initSingletonDhcpClientFactory() {
        DhcpClientFactory factory = new DhcpClientFactoryImpl();
        return factory;
    }

    private static class DhcpClientFactoryImpl extends DhcpClientFactory {
    }
}
