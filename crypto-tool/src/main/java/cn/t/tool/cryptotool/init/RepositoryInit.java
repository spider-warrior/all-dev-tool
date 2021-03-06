package cn.t.tool.cryptotool.init;

import cn.t.tool.cryptotool.construct.Context;
import cn.t.tool.cryptotool.construct.RepositoryConfig;
import cn.t.tool.cryptotool.exception.AppException;
import cn.t.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RepositoryInit {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryInit.class);

    public void init(Context context) {
        RepositoryConfig repositoryConfig = context.getRepositoryConfig();
        File repositoryDir = new File(repositoryConfig.calculateRepositoryDirectoryName());
        try {
            if(!repositoryDir.exists()) {
                logger.info("first time repository directory: {}", repositoryDir.getCanonicalPath());
                boolean success = FileUtil.initDirectory(repositoryDir);
                if(!success) {
                    throw new AppException("home init error");
                }
                boolean indexSuccess = FileUtil.initDirectory(new File(repositoryConfig.calculateIndexDirectoryName()));
                boolean dataSuccess = FileUtil.initDirectory(new File(repositoryConfig.calculateDataDirectoryName()));
                logger.info("初始化索引文件夹: {}, 数据文件夹: {}", indexSuccess, dataSuccess);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new AppException("home init error");
        }
    }
}
