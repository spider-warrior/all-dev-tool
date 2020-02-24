package cn.t.tool.netproxytool.socks5.model;

import lombok.Data;

/**
 * 生命周期消息
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 21:30
 **/
@Data
public class ConnectionLifeCycledMessage {
    private ConnectionLifeCycle lifeCycle;
    private Object message;
}
