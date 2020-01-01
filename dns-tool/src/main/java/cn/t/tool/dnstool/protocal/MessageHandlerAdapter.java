package cn.t.tool.dnstool.protocal;

import cn.t.tool.dnstool.model.Message;
import cn.t.tool.dnstool.protocal.handler.in.InternetIpV4DomainQueryHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yj
 * @since 2020-01-01 10:43
 **/
@Slf4j
public class MessageHandlerAdapter {
    private final List<MessageHandler> messageHandlerList = new ArrayList<>();
    public Object handle(Context context, Message message) throws IOException {
        MessageHandler messageHandler = selectMessageHandler(message);
        if(messageHandler != null) {
            log.info("client: {}:{} query detail:\n domain: {}, queryType: {}, clazz: {}", context.getInetAddress(), context.getPort(), message.getDomain(), message.getType(), message.getClazz());
            return messageHandler.handler(message);
        } else {
            log.warn("未能处理的消息: {}", message);
            return null;
        }
    }
    private MessageHandler selectMessageHandler(Message message) {
        for(MessageHandler messageHandler: messageHandlerList) {
            if(messageHandler.support(message)) {
                return messageHandler;
            }
        }
        return null;
    }

    public void init() {
        messageHandlerList.add(new InternetIpV4DomainQueryHandler());
    }
}
