package cn.t.tool.dhcptool.protocol;

import java.io.IOException;

public interface DhcpMessageHandler {
    boolean support(Object message);
    void handle(Object message) throws IOException;
}
