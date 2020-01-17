package cn.t.tool.dhcptool;

/**
 * dhcp客户端
 *
 * @author yj
 * @since 2020-01-17 13:21
 **/
public interface DhcpClient {

    /**
     * 请求客户端IP信息
     * @return dns分配的ip信息
     */
    ClientInfo requestClientInfo();
}
