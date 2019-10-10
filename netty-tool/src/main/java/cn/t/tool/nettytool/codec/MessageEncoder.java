package cn.t.tool.nettytool.codec;

public interface MessageEncoder<Source, Msg, RuntimeParam> {
    void encode(Source source, Msg msg, RuntimeParam param);
    boolean support(Msg msg);
}
