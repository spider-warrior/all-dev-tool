package cn.t.tool.nettytool.analyser;

public interface MessageAnalyser<In, RuntimeParam> {
    Object analyse(RuntimeParam param, In in);
}
