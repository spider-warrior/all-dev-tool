package cn.t.tool.dhcptool;

import cn.t.tool.dhcptool.model.DiscoverMessage;
import cn.t.tool.dhcptool.protocol.DhcpMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * dhcp客户端
 *
 * @author yj
 * @since 2020-01-17 13:21
 **/
public class DhcpClient {

    private static final Logger logger = LoggerFactory.getLogger(DhcpClient.class);

    private byte[] macBytes;
    private DhcpMessageSender dhcpMessageSender;
    private ClientInfo clientInfo;

    /**
     * 请求客户端IP信息
     * @return dns分配的ip信息
     */
    public synchronized ClientInfo requestClientInfo() throws IOException {
        DiscoverMessage discoverMessage = new DiscoverMessage();
        discoverMessage.setMac(macBytes);
        int tryTimes = 3;
        int timeout = 300000;
        for(int i=1; i<=tryTimes; i++) {
            logger.info("第{}次尝试获取client info", i);
            dhcpMessageSender.discover(discoverMessage, this);
            try { wait(timeout); } catch (InterruptedException e) { e.printStackTrace(); }
            if(clientInfo != null) {
                return clientInfo;
            }
        }
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

    public synchronized void acceptClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
        notify();
    }
}
