package cn.t.tool.dhcptool;

import cn.t.tool.dhcptool.model.DiscoverMessage;
import cn.t.tool.dhcptool.protocol.DhcpMessageSender;

import java.io.IOException;

/**
 * dhcp客户端
 *
 * @author yj
 * @since 2020-01-17 13:21
 **/
public class DhcpClient {

    private byte[] macBytes;
    private DhcpMessageSender dhcpMessageSender;

    /**
     * 请求客户端IP信息
     * @return dns分配的ip信息
     */
    public ClientInfo requestClientInfo() throws IOException {
        DiscoverMessage discoverMessage = new DiscoverMessage();
        discoverMessage.setMac(macBytes);
        dhcpMessageSender.discover(discoverMessage, this);
        return null;
    }

    public byte[] getMacBytes() {
        return macBytes;
    }

    public void setMacBytes(byte[] macBytes) {
        this.macBytes = macBytes;
    }

    public DhcpClient(DhcpMessageSender dhcpMessageSender) {
        this.dhcpMessageSender = dhcpMessageSender;
    }
}
