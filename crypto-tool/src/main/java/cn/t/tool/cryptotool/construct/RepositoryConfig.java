package cn.t.tool.cryptotool.construct;

import cn.t.util.io.FileUtil;

public class RepositoryConfig {

    private HomeConfig homeConfig;

    /**
     * 资源库文件夹名称
     * */
    private String repositoryDirectoryName = "repository";

    /**
     * index directory
     * */
    private String indexDirectoryName = "index";

    /**
     * data directory
     * */
    private String dataDirectoryName = "data";

    public HomeConfig getHomeConfig() {
        return homeConfig;
    }

    public void setHomeConfig(HomeConfig homeConfig) {
        this.homeConfig = homeConfig;
    }

    public String getRepositoryDirectoryName() {
        return repositoryDirectoryName;
    }

    public void setRepositoryDirectoryName(String repositoryDirectoryName) {
        this.repositoryDirectoryName = repositoryDirectoryName;
    }

    public String getIndexDirectoryName() {
        return indexDirectoryName;
    }

    public void setIndexDirectoryName(String indexDirectoryName) {
        this.indexDirectoryName = indexDirectoryName;
    }

    public String getDataDirectoryName() {
        return dataDirectoryName;
    }

    public void setDataDirectoryName(String dataDirectoryName) {
        this.dataDirectoryName = dataDirectoryName;
    }

    public String calculateRepositoryDirectoryName() {
        return FileUtil.appendFilePath(homeConfig.getBasePath(), repositoryDirectoryName);
    }

    public String calculateIndexDirectoryName() {
        return FileUtil.appendFilePath(calculateRepositoryDirectoryName(), indexDirectoryName);
    }

    public String calculateDataDirectoryName() {
        return FileUtil.appendFilePath(calculateRepositoryDirectoryName(), dataDirectoryName);
    }
}
