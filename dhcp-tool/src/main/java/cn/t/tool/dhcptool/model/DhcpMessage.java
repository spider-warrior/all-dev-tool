package cn.t.tool.dhcptool.model;

import cn.t.tool.dhcptool.constants.OperationType;

/**
 * discover
 *
 * @author yj
 * @since 2020-01-05 15:32
 **/
public class DhcpMessage {

    /**
     * 消息类型
     */
    private OperationType operationType;

    /**
     * mac地址
     * */
    private byte[] mac;

    /**
     * 期待IP
     * */
    private byte[] expectIp;

    /**
     * 事务id
     */
    private Integer txId;

    /**
     * dhcp identifier
     */
    private byte[] dhcpIdentifier;

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public static DhcpMessage newInstance() {
        return new DhcpMessage();
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }

    public byte[] getExpectIp() {
        return expectIp;
    }

    public void setExpectIp(byte[] expectIp) {
        this.expectIp = expectIp;
    }

    public Integer getTxId() {
        return txId;
    }

    public void setTxId(Integer txId) {
        this.txId = txId;
    }

    public byte[] getDhcpIdentifier() {
        return dhcpIdentifier;
    }

    public void setDhcpIdentifier(byte[] dhcpIdentifier) {
        this.dhcpIdentifier = dhcpIdentifier;
    }
}
