package cn.t.tool.cryptotool.construct.test;

import cn.t.tool.cryptotool.construct.ContextBuilder;
import org.junit.Test;

import java.util.Map;

public class ContextBuilderTest {

    private ContextBuilder contextBuilder = new ContextBuilder();

    @Test
    public void buildContextTest() {
        Map<String, String> argMap = null;
        contextBuilder.buildContext(argMap);
    }
}
