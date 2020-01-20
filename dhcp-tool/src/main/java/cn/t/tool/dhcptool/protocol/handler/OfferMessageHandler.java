package cn.t.tool.dhcptool.protocol.handler;

import cn.t.tool.dhcptool.listener.EventBroadcaster;
import cn.t.tool.dhcptool.model.OfferMessage;
import cn.t.tool.dhcptool.protocol.DhcpMessageHandler;

/**
 * @author yj
 * @since 2020-01-05 17:29
 **/
public class OfferMessageHandler implements DhcpMessageHandler {

    private EventBroadcaster eventBroadcaster;

    @Override
    public boolean support(Object message) {
        return message instanceof OfferMessage;
    }

    @Override
    public void handle(Object message) {
        eventBroadcaster.broadcast(message);
    }

    public OfferMessageHandler(EventBroadcaster eventBroadcaster) {
        this.eventBroadcaster = eventBroadcaster;
    }
}
