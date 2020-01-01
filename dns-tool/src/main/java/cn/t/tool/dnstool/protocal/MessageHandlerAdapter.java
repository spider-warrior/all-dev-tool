package cn.t.tool.dnstool.protocal;

import cn.t.tool.dnstool.model.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yj
 * @since 2020-01-01 10:43
 **/
@Slf4j
public class MessageHandlerAdapter {
    private final List<MessageHandler> messageHandlerList = new ArrayList<>();
    public Message handle(Context context, Message message) {
        MessageHandler messageHandler = selectMessageHandler(message);
        if(messageHandler != null) {
            log.info("client: {}:{} query detail:\n domain: {}, queryType: {}, clazz: {}", context.getInetAddress(), context.getPort(), message.getDomain(), message.getType(), message.getClazz());
            return messageHandler.handler(message);
        } else {
            log.warn("no MessageHandler fond for message: {}", message);
            return null;
        }
    }
    private MessageHandler selectMessageHandler(Message message) {
        for(MessageHandler messageHandler: messageHandlerList) {
            if(messageHandler.support()) {
                return messageHandler;
            }
        }
        return null;
    }

    public void init() {

    }
}
