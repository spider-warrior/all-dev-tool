package cn.t.tool.dnstool.protocal;

import cn.t.tool.dnstool.model.Message;

public interface MessageHandler {
    boolean support();
    Message handler(Message message);
}
