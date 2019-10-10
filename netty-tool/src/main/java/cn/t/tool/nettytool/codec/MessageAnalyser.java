package cn.t.tool.nettytool.codec;

public interface MessageAnalyser<In, RuntimeParam> {
    Object analyse(RuntimeParam param, In in);
}
