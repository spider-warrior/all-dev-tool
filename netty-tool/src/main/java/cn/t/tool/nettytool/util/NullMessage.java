package cn.t.tool.nettytool.util;

public class NullMessage {

    private static class NullMessageHolder {
        private static final NullMessage nullMessage = new NullMessage();
    }

    public static NullMessage getNullMessage() {
        return NullMessageHolder.nullMessage;
    }
}
