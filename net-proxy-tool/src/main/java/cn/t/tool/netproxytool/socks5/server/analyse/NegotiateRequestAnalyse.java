package cn.t.tool.netproxytool.socks5.server.analyse;

import cn.t.tool.netproxytool.socks5.constants.Method;
import cn.t.tool.netproxytool.socks5.model.NegotiateRequest;
import io.netty.buffer.ByteBuf;

/**
 * 客户端协商请求解析器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 20:53
 **/
public class NegotiateRequestAnalyse {

    public Object analyse(ByteBuf byteBuf) {
        if(byteBuf.readableBytes() < 2) {
            return null;
        }
        NegotiateRequest negotiateRequest = new NegotiateRequest();
        negotiateRequest.setVersion(byteBuf.readByte());
        byte methodLength = byteBuf.readByte();
        byte[] methodBytes = new byte[methodLength];
        byteBuf.readBytes(methodBytes);
        negotiateRequest.setSupportMethodList(Method.convertToMethod(methodBytes));
        return negotiateRequest;
    }
}
