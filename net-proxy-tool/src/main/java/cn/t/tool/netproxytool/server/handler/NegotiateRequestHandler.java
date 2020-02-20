package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.constants.Method;
import cn.t.tool.netproxytool.constants.Step;
import cn.t.tool.netproxytool.exception.ConnectionException;
import cn.t.tool.netproxytool.model.NegotiateRequest;
import cn.t.tool.netproxytool.model.ConnectionLifeCycle;
import cn.t.tool.netproxytool.model.NegotiateResponse;
import cn.t.tool.netproxytool.socks5.Socks5Constants;
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
        List<Method> methodList =  negotiateRequest.getSupportMethodList();
        if(CollectionUtil.isEmpty(methodList)) {
            throw new ConnectionException("客户端未提供支持的认证方法");
        } else {
            Method selectedMethod = negotiateMethod(methodList);
            if(selectedMethod == null) {
                throw new ConnectionException(String.format("未协商到合适的认证方法, 客户端支持的内容为: %s", methodList));
            }
            NegotiateResponse negotiateResponse = new NegotiateResponse();
            negotiateResponse.setVersion(version);
            negotiateResponse.setMethod(selectedMethod);
            if(selectedMethod == Method.NO_AUTHENTICATION_REQUIRED) {
                lifeCycle.next(Step.COMMAND_EXECUTION);
            } else {
                //下一步骤
                lifeCycle.next();
            }
            return negotiateResponse;
        }
    }
    private Method negotiateMethod(List<Method> methodList) {
        if(methodList.contains(Method.USERNAME_PASSWORD)) {
            return Method.USERNAME_PASSWORD;
        } else if (methodList.contains(Method.NO_AUTHENTICATION_REQUIRED)) {
            return Method.NO_AUTHENTICATION_REQUIRED;
        } else {
            return null;
        }
    }
}
