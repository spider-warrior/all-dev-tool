package cn.t.tool.rmdbtool.exception;

/**
 * 主键不存在异常
 */
public class PrimaryKeyNotExistException extends RuntimeException {

    public PrimaryKeyNotExistException() {
        super();
    }

    public PrimaryKeyNotExistException(String message) {
        super(message);
    }

    public PrimaryKeyNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrimaryKeyNotExistException(Throwable cause) {
        super(cause);
    }

    protected PrimaryKeyNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
