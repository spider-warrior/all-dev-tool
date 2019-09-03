package cn.t.tool.cryptotool.construct;

import cn.t.util.common.ArgUtil;
import cn.t.util.common.StringUtil;
import cn.t.util.common.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ContextBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ContextBuilder.class);
    private static final String HOME_PATH = "CRYPTO_HOME";

    public Context buildContext(String[] args) {
        Map<String, String> argMap = ArgUtil.resolveMainArgs(args);
        String basePath = argMap.get(HOME_PATH);
        if(StringUtil.isEmpty(basePath)) {
            basePath = SystemUtil.getApplicationVar(HOME_PATH);
        }
        if(StringUtil.isEmpty(basePath)) {
            basePath = SystemUtil.getApplicationVar("user.dir");
        }
        logger.info("use base path: {}", basePath);
        //home
        HomeConstruct homeConstruct = new HomeConstruct();
        homeConstruct.setBasePath(basePath);
        //repository
        RepositoryConstruct repositoryConstruct = new RepositoryConstruct();
        repositoryConstruct.setHomeConstruct(homeConstruct);
        Context context = new Context();
        context.setHomeConstruct(homeConstruct);
        context.setRepositoryConstruct(repositoryConstruct);
        return context;
    }
}
