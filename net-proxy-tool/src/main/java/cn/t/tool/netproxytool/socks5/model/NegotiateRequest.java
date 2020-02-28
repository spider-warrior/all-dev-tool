package cn.t.tool.netproxytool.socks5.model;

import cn.t.tool.netproxytool.socks5.constants.Socks5Method;
import lombok.Data;

import java.util.List;

/**
 * 客户端协商请求
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 20:13
 **/
@Data
public class NegotiateRequest {
    private byte version;
    private List<Socks5Method> supportSocks5MethodList;
}
