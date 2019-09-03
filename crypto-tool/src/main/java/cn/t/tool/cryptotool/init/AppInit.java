package cn.t.tool.cryptotool.init;

import cn.t.tool.cryptotool.construct.Context;
import cn.t.tool.cryptotool.construct.ContextBuilder;

import java.util.Map;

public class AppInit {

    public Context init(Map<String, String> args) {
        ContextBuilder contextBuilder = new ContextBuilder();
        Context context = contextBuilder.buildContext(args);
        context.setArgMap(args);
        //home
        HomeInit homeInit = new HomeInit();
        homeInit.init(context);

        //repository
        RepositoryInit repositoryInit = new RepositoryInit();
        repositoryInit.init(context);

        return context;
    }
}
