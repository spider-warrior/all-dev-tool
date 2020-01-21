package cn.t.tool.dhcptool;

import cn.t.tool.dhcptool.listener.EventBroadcaster;
import cn.t.tool.dhcptool.protocol.DhcpMessageHandler;
import cn.t.tool.dhcptool.protocol.DhcpMessageReceiver;
import cn.t.tool.dhcptool.protocol.DhcpMessageSender;
import cn.t.tool.dhcptool.protocol.handler.AckMessageHandler;
import cn.t.tool.dhcptool.protocol.handler.NakMessageHandler;
import cn.t.tool.dhcptool.protocol.handler.OfferMessageHandler;
import cn.t.util.common.digital.HexUtil;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * dhcp客户端工厂
 *
 * @author yj
 * @since 2020-01-17 13:24
 **/
public abstract class DhcpClientFactory {


    private List<DhcpMessageHandler> handlerList = new ArrayList<>();
    private EventBroadcaster eventBroadcaster = new EventBroadcaster();
    private DhcpMessageSender dhcpMessageSender;
    private WeakHashMap<String, DhcpClient> macClientMap = new WeakHashMap<>();
    /**
     * 获取全局唯一client工厂实例
     * @return 客户端单例工厂
     */
    public static DhcpClientFactory singleton() {
        return DhcpClientFactoryHolder.dhcpClientFactory;
    }

    /**
     * 获取dhcp客户端
     * @param macBytes 网卡mac
     * @return dhcp客户端
     */
    public DhcpClient acquireDhcpClient(byte[] macBytes) {
        String mac = HexUtil.bytesToHex(macBytes);
        DhcpClient client = macClientMap.get(mac);
        if(client == null) {
            synchronized (mac.intern()) {
                client = macClientMap.get(mac);
                if(client == null) {
                    client = new DhcpClient(dhcpMessageSender);
                    macClientMap.put(mac, client);
                }
            }
        }
        client.setMacBytes(macBytes);
        return client;
    }

    /**
     * 工厂初始化,启动监听dhcp消息
     */
    public synchronized void init() throws SocketException {
        //构建receiver并启动
        DhcpMessageReceiver dhcpMessageReceiver = new DhcpMessageReceiver(eventBroadcaster);
        dhcpMessageReceiver.addDhcpMessageHandlerList(handlerList);
        Thread receiverThread = new Thread(dhcpMessageReceiver);
        receiverThread.setDaemon(true);
        receiverThread.start();
        //构建sender
        dhcpMessageSender = new DhcpMessageSender(dhcpMessageReceiver);
    }


    private static class DhcpClientFactoryHolder {
        private static DhcpClientFactory dhcpClientFactory = new DhcpClientFactory(){};
        static {
            //handler
            dhcpClientFactory.handlerList.add(new OfferMessageHandler(dhcpClientFactory.eventBroadcaster));
            dhcpClientFactory.handlerList.add(new AckMessageHandler(dhcpClientFactory.eventBroadcaster));
            dhcpClientFactory.handlerList.add(new NakMessageHandler());
        }
    }
}
