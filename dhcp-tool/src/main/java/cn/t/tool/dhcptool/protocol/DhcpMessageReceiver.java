package cn.t.tool.dhcptool.protocol;

import cn.t.tool.dhcptool.ClientInfo;
import cn.t.tool.dhcptool.DhcpClient;
import cn.t.tool.dhcptool.listener.DhcpEventListener;
import cn.t.tool.dhcptool.listener.EventBroadcaster;
import cn.t.tool.dhcptool.model.OfferMessage;
import cn.t.util.common.CollectionUtil;
import cn.t.util.common.reflect.GenericUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yj
 * @since 2020-01-05 17:23
 **/
public class DhcpMessageReceiver implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DhcpMessageReceiver.class);
    private DhcpMessageDecoder decoder = new DhcpMessageDecoder();
    private List<DhcpMessageHandler> handlerList = new ArrayList<>();
    private Map<Integer, DhcpClient> txIdClientMapping = new HashMap<>();
    //MTU一般都是1500bytes，DhcpMessageEncoder#encode()方法中设置OptionType.DHCP_MAX_MSG_SIZE一致
    private byte[] buf = new byte[1500];


    @Override
    public void run() {
        try (
            DatagramSocket datagramSocket = new DatagramSocket(68)
        ){
            logger.info("dhcp监听器启动成功");
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while(true) {
                datagramSocket.receive(packet);
                ByteBuffer buffer = ByteBuffer.wrap(buf, 0, packet.getLength());
                Object message = decoder.decode(buffer);
                if(message != null) {
                    DhcpMessageHandler handler = selectDhcpMessageHandler(message);
                    if(handler != null) {
                        logger.info("handler: {} handle message: {}", handler, message);
                        handler.handle(message);
                    } else {
                        logger.warn("未找到消息处理器, msg: {}", message);
                    }
                } else {
                    logger.info("解码器未解析出任何消息");
                }
            }
        } catch (IOException e) {
            logger.error("", e);
        }
        logger.info("消息监听器退出");
    }

    public void addDhcpMessageHandlerList(List<DhcpMessageHandler> handlerList) {
        if(!CollectionUtil.isEmpty(handlerList)) {
            this.handlerList.addAll(handlerList);
        }
    }

    public DhcpMessageReceiver(EventBroadcaster eventBroadcaster) {
        //listener
        DhcpEventListener<OfferMessage> offerMessageListener = new DhcpEventListener<OfferMessage>() {
            @Override
            public void onEvent(OfferMessage message) {
                DhcpClient client = txIdClientMapping.get(message.getTxId());
                if(client != null) {
                    ClientInfo clientInfo = new ClientInfo();
                    clientInfo.setIp(message.getClientIp());
                    clientInfo.setDhcpServerIp(message.getDhcpServerIp());
                    clientInfo.setRouter(message.getRouter());
                    clientInfo.setSubnetMask(message.getSubnetMask());
                    clientInfo.setDnsServerList(message.getDnsServerList());
                    logger.info("{}", clientInfo);
                }
            }
        };
        eventBroadcaster.addEventListener(offerMessageListener);
    }

    /**
     * 注册客户端
     * @param txId 事务id
     * @param dhcpClient 注册的客户端
     */
    public void bindClientClient(Integer txId, DhcpClient dhcpClient) {
        txIdClientMapping.put(txId, dhcpClient);
    }


    private DhcpMessageHandler selectDhcpMessageHandler(Object msg) {
        for(DhcpMessageHandler handler: handlerList) {
            if(handler.support(msg)) {
                return handler;
            }
        }
        return null;
    }
}
