package cn.t.tool.dhcptool.model;

/**
 * discover
 *
 * @author yj
 * @since 2020-01-05 15:32
 **/
public class DiscoverMessage {

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

    public static DiscoverMessage newInstance() {
        return new DiscoverMessage();
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
}
