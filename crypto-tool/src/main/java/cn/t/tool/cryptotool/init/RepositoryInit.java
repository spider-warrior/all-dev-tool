package cn.t.tool.cryptotool.init;

import cn.t.tool.cryptotool.construct.Context;
import cn.t.tool.cryptotool.construct.RepositoryConstruct;
import cn.t.tool.cryptotool.exception.AppException;
import cn.t.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RepositoryInit {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryInit.class);

    public void init(Context context) {
        RepositoryConstruct repositoryConstruct = context.getRepositoryConstruct();
        File repositoryDir = new File(repositoryConstruct.calculateRepositoryDirectoryName());
        try {
            if(!repositoryDir.exists()) {
                logger.info("first time repository directory: {}", repositoryDir.getCanonicalPath());
                boolean success = FileUtil.initDirectory(repositoryDir);
                if(!success) {
                    throw new AppException("home init error");
                }
                boolean indexSuccess = FileUtil.initDirectory(new File(repositoryConstruct.calculateIndexDirectoryName()));
                boolean dataSuccess = FileUtil.initDirectory(new File(repositoryConstruct.calculateDataDirectoryName()));
                logger.info("初始化索引文件夹: {}, 数据文件夹: {}", indexSuccess, dataSuccess);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new AppException("home init error");
        }
    }
}
