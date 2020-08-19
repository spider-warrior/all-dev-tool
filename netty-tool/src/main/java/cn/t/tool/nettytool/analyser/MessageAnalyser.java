package cn.t.tool.nettytool.analyser;

public interface MessageAnalyser<In, RuntimeParam> {
    Object analyse(In in, RuntimeParam param);
}
