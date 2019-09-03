package cn.t.tool.cryptotool.init;

import cn.t.tool.cryptotool.construct.Context;
import cn.t.tool.cryptotool.construct.ContextBuilder;

public class AppInit {

    public void init(String[] args) {
        ContextBuilder contextBuilder = new ContextBuilder();
        Context context = contextBuilder.buildContext(args);

        //home
        HomeInit homeInit = new HomeInit();
        homeInit.init(context);

        //repository
        RepositoryInit repositoryInit = new RepositoryInit();
        repositoryInit.init(context);

    }
}
