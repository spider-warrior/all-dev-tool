package cn.t.tool.cryptotool.construct;

import cn.t.util.common.StringUtil;
import cn.t.util.common.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ContextBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ContextBuilder.class);
    private static final String HOME_PATH = "CRYPTO_HOME";

    public Context buildContext(Map<String, String> argMap) {
        String basePath = argMap.get(HOME_PATH);
        if(StringUtil.isEmpty(basePath)) {
            basePath = SystemUtil.getApplicationVar(HOME_PATH);
        }
        if(StringUtil.isEmpty(basePath)) {
            basePath = SystemUtil.getApplicationVar("user.dir");
        }
        logger.info("use base path: {}", basePath);
        //home
        HomeConfig homeConfig = new HomeConfig();
        homeConfig.setBasePath(basePath);
        //repository
        RepositoryConfig repositoryConfig = new RepositoryConfig();
        repositoryConfig.setHomeConfig(homeConfig);
        Context context = new Context();
        context.setHomeConfig(homeConfig);
        context.setRepositoryConfig(repositoryConfig);
        return context;
    }
}
