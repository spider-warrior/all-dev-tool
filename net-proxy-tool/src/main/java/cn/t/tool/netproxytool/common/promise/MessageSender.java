package cn.t.tool.netproxytool.common.promise;

public interface MessageSender {
    void send(Object object);
    void close();
}
