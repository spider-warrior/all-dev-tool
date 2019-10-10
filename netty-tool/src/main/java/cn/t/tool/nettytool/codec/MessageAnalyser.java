package cn.t.tool.nettytool.codec;

public interface MessageAnalyser<In, Out, RuntimeParam> {
    Out analyse(RuntimeParam param, In in);
}
