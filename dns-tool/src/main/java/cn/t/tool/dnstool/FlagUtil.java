package cn.t.tool.dnstool;

/**
 * @author yj
 * @since 2020-01-01 12:47
 **/
public class FlagUtil {

    private static final short QR_BIT =          (short)0b1000000000000000;
    private static final short OP_CODE_BIT =     (short)0b0111100000000000;
    private static final short AA_BIT =          (short)0b0000010000000000;
    private static final short TC_BIT =          (short)0b0000001000000000;
    private static final short RD_BIT =          (short)0b0000000100000000;
    private static final short RA_BIT =          (short)0b0000000010000000;
    private static final short R_CODE__BIT =      (short)0b0000000000001111;

    /**
     * 是否未查询
     * @param flag 标志
     * @return true|false
     */
    public static boolean isQuery(short flag) {
        return (QR_BIT & flag) == QR_BIT;
    }

    /**
     * 是否为正向查询
     * @param flag 标志
     * @return true|false
     */
    public static boolean isForwardDirection(short flag) {
        return (OP_CODE_BIT & flag) == 0;
    }

    /**
     * 是否为权威主机
     * @param flag 标
     * @return true|false
     */
    public static boolean isAuthoritative(short flag) {
        return (AA_BIT & flag) == AA_BIT;
    }

    /**
     * 是否被截断
     * @param flag 标志
     * @return true|false
     */
    public static boolean isTruncated(short flag) {
        return (TC_BIT & flag) == TC_BIT;
    }

    /**
     * 是否建议域名服务器进行递归解析
     * @param flag 标志
     * @return true|false
     */
    public static boolean isRecursionResolve(short flag) {
        return (RD_BIT & flag) == RD_BIT;
    }

    /**
     * 否支持递归查询
     * @param flag 标志
     * @return true|false
     */
    public static boolean isRecursionResolveSupported(short flag) {
        return (RA_BIT & flag) == RA_BIT;
    }

    /**
     * 是否响应成功
     * @param flag 标志
     * @return true|false
     */
    public static boolean isResponseSuccess(short flag) {
        return (R_CODE__BIT & flag) == 0;
    }

    /**
     * 是否格式错误
     * @param flag 标志
     * @return true|false
     */
    public static boolean isFormatError(short flag) {
        return (R_CODE__BIT & flag) == 1;
    }

    /**
     * 是否服务器错误
     * @param flag 标志
     * @return true|false
     */
    public static boolean isServerError(short flag) {
        return (R_CODE__BIT & flag) == 2;
    }

    /**
     * 是否请求的域名不存在
     * @param flag 标志
     * @return true|false
     */
    public static boolean isDomainNotExist(short flag) {
        return (R_CODE__BIT & flag) == 3;
    }

    /**
     * 是否域名服务器不支持请求的类型
     * @param flag 标志
     * @return true|false
     */
    public static boolean isServerNotSupportedQueryType(short flag) {
        return (R_CODE__BIT & flag) == 4;
    }

    /**
     * 是否域名服务器因为策略的原因拒绝执行请求的操作(例如域名服务器不会为特定的请求者返回查询结果，或者域名服务器不会为特定的请求返回特定的数据)
     * @param flag 标志
     * @return true|false
     */
    public static boolean isStrategyForbid(short flag) {
        return (R_CODE__BIT & flag) == 5;
    }

    /**
     * 标记为响应
     * @param flag 标志
     * @return 标志
     */
    public static short markResponse(short flag) {
        return (short)(flag | 0b1000000000000000);
    }

    /**
     * 标记为支持递归查询
     * @param flag 标志
     * @return 标志
     */
    public static short markRecursionSupported(short flag) {
        return (short)(flag | 0b0000000010000000);
    }

}
