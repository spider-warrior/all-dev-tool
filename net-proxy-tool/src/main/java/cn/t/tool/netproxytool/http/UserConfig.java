package cn.t.tool.netproxytool.http;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-05-24 09:29
 **/
public class UserConfig {
    private String socks5ServerHost;
    private short socks5ServerPort;

    private String username;
    private String password;

    private String security;


    public String getSocks5ServerHost() {
        return socks5ServerHost;
    }

    public void setSocks5ServerHost(String socks5ServerHost) {
        this.socks5ServerHost = socks5ServerHost;
    }

    public short getSocks5ServerPort() {
        return socks5ServerPort;
    }

    public void setSocks5ServerPort(short socks5ServerPort) {
        this.socks5ServerPort = socks5ServerPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }
}
