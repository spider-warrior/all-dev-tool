package cn.t.tool.nettytool.launcher.listener;

import cn.t.tool.nettytool.launcher.AbstractLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLauncherListener implements LauncherListener{

    private static final Logger logger = LoggerFactory.getLogger(DefaultLauncherListener.class);

    @Override
    public void startup(AbstractLauncher launcher) {
        logger.info(launcher.getClass() + " start....");
    }

    @Override
    public void close(AbstractLauncher launcher) {
        logger.info(launcher.getClass() + " stop....");
    }
}
