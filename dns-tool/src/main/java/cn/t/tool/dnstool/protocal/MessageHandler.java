package cn.t.tool.dnstool.protocal;

import cn.t.tool.dnstool.model.Message;

import java.io.IOException;

public interface MessageHandler {
    boolean support(Message message);
    Object handler(Message message) throws IOException;
}
