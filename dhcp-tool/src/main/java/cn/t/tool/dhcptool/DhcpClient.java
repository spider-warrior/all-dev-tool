package cn.t.tool.dhcptool;

import cn.t.tool.dhcptool.model.DhcpMessage;
import cn.t.tool.dhcptool.protocol.DhcpMessageSender;
import cn.t.util.common.RegexUtil;
import cn.t.util.common.StringUtil;
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
        return requestClientInfo(null);
    }

    public synchronized ClientInfo requestClientInfo(String ip) throws IOException {
        DhcpMessage message = new DhcpMessage();
        message.setMac(macBytes);
        if(!StringUtil.isEmpty(ip)) {
            if(!RegexUtil.isIp(ip)) {
                throw new RuntimeException("指定的ip格式不正确: " + ip);
            }
            byte[] ipBytes = new byte[4];
            String[] elements = ip.split("\\.");
            for(int i=0; i<ipBytes.length; i++) {
                ipBytes[i] = (byte)(Integer.parseInt(elements[i]));
            }
            message.setExpectIp(ipBytes);
        }
        int tryTimes = 3;
        int timeout = 3000;
        //discover阶段
        for(int i=1; i<=tryTimes; i++) {
            logger.info("第{}次尝试发送discover报文", i);
            dhcpMessageSender.discover(message, this);
            try { wait(timeout); } catch (InterruptedException e) { e.printStackTrace(); }
            if(clientInfo != null) {
                message.setExpectIp(clientInfo.getIp());
                message.setDhcpIdentifier(clientInfo.getDhcpServerIp());
                break;
            }
        }
        if(clientInfo == null) {
            return null;
        } else {
            clientInfo = null;
        }
        //request阶段
        for(int i=1; i<=tryTimes; i++) {
            logger.info("第{}次尝试发送request报文", i);
            dhcpMessageSender.request(message, this);
            try { wait(timeout); } catch (InterruptedException e) { e.printStackTrace(); }
            if(clientInfo != null) {
                break;
            }
        }
        return clientInfo;
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
