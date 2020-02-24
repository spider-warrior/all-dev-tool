package cn.t.tool.netproxytool.socks5.model;

import cn.t.tool.netproxytool.socks5.constants.Method;
import lombok.Data;

/**
 * 服务器协商响应
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 20:37
 **/
@Data
public class NegotiateResponse {
    private byte version;
    private Method method;
}
