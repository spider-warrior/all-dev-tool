package cn.t.tool.dnstool.protocal;

import cn.t.tool.dnstool.model.Request;

import java.io.IOException;

public interface MessageHandler {
    boolean support(Request request);
    Object handler(Request request) throws IOException;
}
