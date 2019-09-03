package cn.t.tool.cryptotool.construct;

import java.util.Map;

public class Context {
    private Map<String, String> argMap;
    private HomeConfig homeConfig;
    private RepositoryConfig repositoryConfig;

    public Map<String, String> getArgMap() {
        return argMap;
    }
    public void setArgMap(Map<String, String> argMap) {
        this.argMap = argMap;
    }
    public HomeConfig getHomeConfig() {
        return homeConfig;
    }
    public void setHomeConfig(HomeConfig homeConfig) {
        this.homeConfig = homeConfig;
    }
    public RepositoryConfig getRepositoryConfig() {
        return repositoryConfig;
    }
    public void setRepositoryConfig(RepositoryConfig repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
    }
}
