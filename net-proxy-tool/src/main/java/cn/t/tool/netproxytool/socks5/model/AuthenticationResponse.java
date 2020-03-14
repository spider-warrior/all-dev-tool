package cn.t.tool.netproxytool.socks5.model;

import lombok.Data;

/**
 * 用户名密码鉴权结果
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-03-14 20:08
 **/
@Data
public class AuthenticationResponse {
    private byte version;
    private byte status;
}
