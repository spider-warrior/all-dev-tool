package cn.t.tool.cryptotool.init;

import cn.t.tool.cryptotool.construct.Context;
import cn.t.tool.cryptotool.construct.HomeConstruct;
import cn.t.tool.cryptotool.exception.AppException;
import cn.t.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class HomeInit {

    private static final Logger logger = LoggerFactory.getLogger(HomeInit.class);

    public void init(Context context) {
        HomeConstruct homeConstruct = context.getHomeConstruct();
        File file = new File(homeConstruct.getBasePath());
        try {
            if(!file.exists()) {
                logger.info("first time init home directory: {}", file.getCanonicalPath());
                boolean success = FileUtil.initDirectory(file);
                if(!success) {
                    throw new AppException("home init error");
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new AppException("home init error");
        }
    }
}
