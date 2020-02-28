package cn.t.tool.netproxytool.socks5.constants;

public enum Socks5Step {

    /**
     * 协商
     */
    NEGOTIATE,

    /**
     * 认证
     */
    AUTHENTICATION,

    /**
     * 命令执行
     */
    COMMAND_EXECUTION,

    /**
     * 数据转发
     */
    FORWARDING_DATA

}
