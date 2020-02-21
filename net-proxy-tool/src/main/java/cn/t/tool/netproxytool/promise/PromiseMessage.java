package cn.t.tool.netproxytool.promise;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-21 21:37
 **/
@Setter
@Getter
public class PromiseMessage {
    private Object message;
    private MessageSender messageSender;
    public void send() {
        messageSender.send(message);
    }
}
