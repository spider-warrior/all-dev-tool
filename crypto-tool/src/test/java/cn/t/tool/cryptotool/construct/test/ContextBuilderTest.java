package cn.t.tool.cryptotool.construct.test;

import cn.t.tool.cryptotool.construct.ContextBuilder;
import org.junit.Test;

public class ContextBuilderTest {

    private ContextBuilder contextBuilder = new ContextBuilder();

    @Test
    public void buildContextTest() {
        String[] argArr = null;
        contextBuilder.buildContext(argArr);
    }
}
