package cn.t.tool.dhcptool.protocol.handler;

import cn.t.tool.dhcptool.listener.EventBroadcaster;
import cn.t.tool.dhcptool.model.AckMessage;
import cn.t.tool.dhcptool.protocol.DhcpMessageHandler;

/**
 * @author yj
 * @since 2020-01-21 17:44
 **/
public class AckMessageHandler implements DhcpMessageHandler {

    private EventBroadcaster eventBroadcaster;

    @Override
    public boolean support(Object message) {
        return message instanceof AckMessage;
    }

    @Override
    public void handle(Object message) {
        eventBroadcaster.broadcast(message);
    }

    public AckMessageHandler(EventBroadcaster eventBroadcaster) {
        this.eventBroadcaster = eventBroadcaster;
    }
}
