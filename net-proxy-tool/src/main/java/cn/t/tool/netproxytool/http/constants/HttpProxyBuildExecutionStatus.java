package cn.t.tool.netproxytool.http.constants;

/**
 * htt创建代理响应码
 */
public enum HttpProxyBuildExecutionStatus {
    SUCCEEDED((byte) 0X00),
    FAILED((byte) 0X01);

    public static HttpProxyBuildExecutionStatus getHttpProxyBuildExecutionStatus(byte value) {
        for(HttpProxyBuildExecutionStatus status: values()) {
            if(status.value == value) {
                return status;
            }
        }
        return null;
    }

    public final byte value;
    HttpProxyBuildExecutionStatus(byte value) {
        this.value = value;
    }
}
