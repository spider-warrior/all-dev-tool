package cn.t.tool.dnstool.protocal;

import cn.t.tool.dnstool.model.Message;

import java.net.UnknownHostException;

public interface MessageHandler {
    boolean support(Message message);
    Message handler(Message message) throws UnknownHostException;
}
