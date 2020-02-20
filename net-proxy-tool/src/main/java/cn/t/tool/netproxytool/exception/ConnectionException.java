package cn.t.tool.netproxytool.exception;

/**
 * 连接异常
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 21:27
 **/
public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }
}
