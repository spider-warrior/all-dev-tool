package cn.t.tool.netproxytool.component;

public interface MessageSender {
    void send(Object object);
    void close();
}
