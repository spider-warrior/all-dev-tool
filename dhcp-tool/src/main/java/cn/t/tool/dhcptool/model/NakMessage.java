package cn.t.tool.dhcptool.model;

/**
 * @author yj
 * @since 2020-01-05 16:23
 **/
public class NakMessage {
    /**
     * 事务ID
     */
    private int transactionId;
    /**
     * 下一个dhcp服务器地址
     */
    private byte[] nextServerIp;

    /**
     * 文字消息
     */
    private String message;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public byte[] getNextServerIp() {
        return nextServerIp;
    }

    public void setNextServerIp(byte[] nextServerIp) {
        this.nextServerIp = nextServerIp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
