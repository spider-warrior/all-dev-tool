package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.socks5.constants.Socks5Method;
import cn.t.tool.netproxytool.socks5.constants.Socks5Constants;
import cn.t.tool.netproxytool.socks5.constants.Socks5Step;
import cn.t.tool.netproxytool.exception.ConnectionException;
import cn.t.tool.netproxytool.socks5.model.ConnectionLifeCycle;
import cn.t.tool.netproxytool.socks5.model.NegotiateRequest;
import cn.t.tool.netproxytool.socks5.model.NegotiateResponse;
import cn.t.util.common.CollectionUtil;

import java.util.List;

/**
 * 协商请求处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
public class NegotiateRequestHandler {
    public Object handle(NegotiateRequest negotiateRequest, ConnectionLifeCycle lifeCycle) {
        byte version = negotiateRequest.getVersion();
        if(version != Socks5Constants.VERSION) {
            throw new ConnectionException(String.format("不支持的协议版本: %d", version));
        }
        List<Socks5Method> socks5MethodList =  negotiateRequest.getSupportSocks5MethodList();
        if(CollectionUtil.isEmpty(socks5MethodList)) {
            throw new ConnectionException("客户端未提供支持的认证方法");
        } else {
            Socks5Method selectedSocks5Method = negotiateMethod(socks5MethodList);
            if(selectedSocks5Method == null) {
                throw new ConnectionException(String.format("未协商到合适的认证方法, 客户端支持的内容为: %s", socks5MethodList));
            }
            NegotiateResponse negotiateResponse = new NegotiateResponse();
            negotiateResponse.setVersion(version);
            negotiateResponse.setSocks5Method(selectedSocks5Method);
            if(selectedSocks5Method == Socks5Method.NO_AUTHENTICATION_REQUIRED) {
                lifeCycle.next(Socks5Step.COMMAND_EXECUTION);
            } else {
                //下一步骤
                lifeCycle.next(Socks5Step.AUTHENTICATION);
            }
            return negotiateResponse;
        }
    }
    private Socks5Method negotiateMethod(List<Socks5Method> socks5MethodList) {
        if(socks5MethodList.contains(Socks5Method.USERNAME_PASSWORD)) {
            return Socks5Method.USERNAME_PASSWORD;
        } else if (socks5MethodList.contains(Socks5Method.NO_AUTHENTICATION_REQUIRED)) {
            return Socks5Method.NO_AUTHENTICATION_REQUIRED;
        } else {
            return null;
        }
    }
}
