package cn.t.tool.dhcptool;

import java.util.Arrays;
import java.util.List;

/**
 * @author yj
 * @since 2020-01-17 13:22
 **/
public class ClientInfo {
    /**
     * 客户端ip
     */
    private byte[] ip;

    /**
     * DHCP服务端ip
     */
    private byte[] dhcpServerIp;

    /**
     * 掩码
     */
    private byte[] subnetMask;

    /**
     * 路由
     */
    private byte[] router;

    /**
     * dns服务器地址
     */
    private List<byte[]> dnsServerList;

    public byte[] getIp() {
        return ip;
    }

    public void setIp(byte[] ip) {
        this.ip = ip;
    }

    public byte[] getDhcpServerIp() {
        return dhcpServerIp;
    }

    public void setDhcpServerIp(byte[] dhcpServerIp) {
        this.dhcpServerIp = dhcpServerIp;
    }

    public byte[] getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(byte[] subnetMask) {
        this.subnetMask = subnetMask;
    }

    public byte[] getRouter() {
        return router;
    }

    public void setRouter(byte[] router) {
        this.router = router;
    }

    public List<byte[]> getDnsServerList() {
        return dnsServerList;
    }

    public void setDnsServerList(List<byte[]> dnsServerList) {
        this.dnsServerList = dnsServerList;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
            "ip=" + Arrays.toString(ip) +
            ", dhcpServerIp=" + Arrays.toString(dhcpServerIp) +
            ", subnetMask=" + Arrays.toString(subnetMask) +
            ", router=" + Arrays.toString(router) +
            ", dnsServerList=" + dnsServerList +
            '}';
    }
}
