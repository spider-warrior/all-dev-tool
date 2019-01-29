package cn.t.tool.dbtool.exception;

public class RequiredParamMissingException extends Exception {

    private String param;

    public RequiredParamMissingException(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public RequiredParamMissingException setParam(String param) {
        this.param = param;
        return this;
    }
}
