package cn.t.tool.dhcptool;

import cn.t.tool.dhcptool.listener.EventBroadcaster;
import cn.t.tool.dhcptool.protocol.DhcpMessageHandler;
import cn.t.tool.dhcptool.protocol.DhcpMessageReceiver;
import cn.t.tool.dhcptool.protocol.DhcpMessageSender;
import cn.t.tool.dhcptool.protocol.handler.NakMessageHandler;
import cn.t.tool.dhcptool.protocol.handler.OfferMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * dhcp客户端工厂
 *
 * @author yj
 * @since 2020-01-17 13:24
 **/
public abstract class DhcpClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(DhcpClientFactory.class);
    private List<DhcpMessageHandler> handlerList = new ArrayList<>();
    private EventBroadcaster eventBroadcaster = new EventBroadcaster();
    private DhcpMessageReceiver dhcpMessageReceiver;
    private DhcpMessageSender dhcpMessageSender;
    /**
     * 获取全局唯一client工厂实例
     * @return 客户端单例工厂
     */
    public static DhcpClientFactory singleton() {
        return DhcpClientFactoryHolder.dhcpClientFactory;
    }

    public DhcpClient acquireDhcpClient(byte[] macBytes) {
        DhcpClient client = new DhcpClient(dhcpMessageSender);
        client.setMacBytes(macBytes);
        return client;
    }

    /**
     * 工厂初始化,启动监听dhcp消息
     */
    public synchronized void init() throws SocketException {
        dhcpMessageReceiver = new DhcpMessageReceiver(eventBroadcaster);
        dhcpMessageReceiver.addDhcpMessageHandlerList(handlerList);
        Thread receiverThread = new Thread(dhcpMessageReceiver);
        receiverThread.setDaemon(true);
        receiverThread.start();
        dhcpMessageSender = new DhcpMessageSender(dhcpMessageReceiver);
    }


    private static class DhcpClientFactoryHolder {
        private static DhcpClientFactory dhcpClientFactory = new DhcpClientFactory(){};
        static {
            //handler
            dhcpClientFactory.handlerList.add(new OfferMessageHandler(dhcpClientFactory.eventBroadcaster));
            dhcpClientFactory.handlerList.add(new NakMessageHandler());
        }
    }
}
