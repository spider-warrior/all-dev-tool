package cn.t.tool.netproxytool.socks5.promise;

public interface MessageSender {
    void send(Object object);
    void close();
}
