package cn.t.tool.dnstool.protocal;

import cn.t.tool.dnstool.model.Request;
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
    public Object handle(Context context, Request request) throws IOException {
        MessageHandler messageHandler = selectMessageHandler(request);
        if(messageHandler != null) {
            log.info("client: {}:{} query detail:\n domain: {}, queryType: {}, clazz: {}", context.getInetAddress(), context.getPort(), request.getDomain(), request.getType(), request.getClazz());
            return messageHandler.handler(request);
        } else {
            log.warn("未能处理的消息: {}", request);
            return null;
        }
    }
    private MessageHandler selectMessageHandler(Request request) {
        for(MessageHandler messageHandler: messageHandlerList) {
            if(messageHandler.support(request)) {
                return messageHandler;
            }
        }
        return null;
    }

    public MessageHandlerAdapter() {
        messageHandlerList.add(new InternetIpV4DomainQueryHandler());
    }
}
