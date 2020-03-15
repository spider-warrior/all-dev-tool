package cn.t.tool.netproxytool.socks5.model;

import lombok.Data;

/**
 * method请求
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-03-15 09:44
 **/
@Data
public class MethodRequest {
    private byte version;
    private byte[] methods;
}
