package cn.t.tool.netproxytool.socks5.model;

import lombok.Data;

/**
 * 用户名密码鉴权请求
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-03-14 19:33
 **/
@Data
public class UsernamePasswordAuthenticationRequest {
    private byte version;
    private byte[] username;
    private byte[] password;
}
