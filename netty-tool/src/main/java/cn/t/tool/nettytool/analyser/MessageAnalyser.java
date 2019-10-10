package cn.t.tool.nettytool.analyser;

public interface MessageAnalyser<In, Out, RuntimeParam> {
    Out analyse(RuntimeParam param, In in);
}
