package cn.t.tool.dhcptool.model;

import java.util.Arrays;
import java.util.List;

/**
 * @author yj
 * @since 2020-01-11 16:40
 **/
public class OfferMessage {
    /**
     * 事务id
     */
    private int txId;
    /**
     * 客户端ip
     */
    private byte[] clientIp;
    /**
     * 客户端mac
     */
    private byte[] clientMac;
    /**
     * dhcp服务端ip
     */
    private byte[] dhcpServerIp;
    /**
     * ip释放时间(秒)
     */
    private int leaseTime;
    /**
     * renew time
     */
    private int renewTime;
    /**
     * rebinding time
     */
    private int rebindingTime;
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

    public int getTxId() {
        return txId;
    }

    public void setTxId(int txId) {
        this.txId = txId;
    }

    public byte[] getClientIp() {
        return clientIp;
    }

    public void setClientIp(byte[] clientIp) {
        this.clientIp = clientIp;
    }

    public byte[] getClientMac() {
        return clientMac;
    }

    public void setClientMac(byte[] clientMac) {
        this.clientMac = clientMac;
    }

    public byte[] getDhcpServerIp() {
        return dhcpServerIp;
    }

    public void setDhcpServerIp(byte[] dhcpServerIp) {
        this.dhcpServerIp = dhcpServerIp;
    }

    public int getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(int leaseTime) {
        this.leaseTime = leaseTime;
    }

    public int getRenewTime() {
        return renewTime;
    }

    public void setRenewTime(int renewTime) {
        this.renewTime = renewTime;
    }

    public int getRebindingTime() {
        return rebindingTime;
    }

    public void setRebindingTime(int rebindingTime) {
        this.rebindingTime = rebindingTime;
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
        return "OfferMessage{" +
            "txId=" + txId +
            ", clientIp=" + Arrays.toString(clientIp) +
            ", clientMac=" + Arrays.toString(clientMac) +
            ", dhcpServerIp=" + Arrays.toString(dhcpServerIp) +
            ", leaseTime=" + leaseTime +
            ", renewTime=" + renewTime +
            ", rebindingTime=" + rebindingTime +
            ", subnetMask=" + Arrays.toString(subnetMask) +
            ", router=" + Arrays.toString(router) +
            ", dnsServerList=" + dnsServerList +
            '}';
    }
}
