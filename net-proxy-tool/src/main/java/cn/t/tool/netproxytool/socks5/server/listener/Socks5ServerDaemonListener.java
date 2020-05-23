package cn.t.tool.netproxytool.socks5.server.listener;

import cn.t.tool.netproxytool.socks5.constants.Socks5ServerConfig;
import cn.t.tool.netproxytool.socks5.server.UserRepository;
import cn.t.tool.nettytool.server.DaemonServer;
import cn.t.tool.nettytool.server.listener.DaemonListener;
import cn.t.util.common.StringUtil;
import cn.t.util.common.SystemUtil;
import cn.t.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-05-24 00:18
 **/
public class Socks5ServerDaemonListener implements DaemonListener {

    private static final Logger logger = LoggerFactory.getLogger(Socks5ServerDaemonListener.class);

    @Override
    public void startup(DaemonServer server) {
        String socks5Home = SystemUtil.getSysEnv(Socks5ServerConfig.SOCKS5_SERVER_HOME_KEY);
        if(!StringUtil.isEmpty(socks5Home)) {
            File home = new File(socks5Home);
            if(home.exists()) {
                File config = new File(FileUtil.appendFilePath(socks5Home, Socks5ServerConfig.SOCKS5_SERVER_USERS_FILE));
                if(config.exists()) {
                    try (
                        FileInputStream fileInputStream = new FileInputStream(config)
                    ) {
                        Properties properties = new Properties();
                        properties.load(fileInputStream);
                        if(!properties.isEmpty()) {
                            properties.forEach((k, v) -> {
                                logger.info("添加用户, username: {}, password: {}", k, v);
                                UserRepository.addUser((String)k, (String)v);
                            });
                        }
                    } catch (IOException e) {
                        logger.error("加载socks5配置文件失败", e);
                    }
                }
            }
        }
    }

    @Override
    public void close(DaemonServer server) {

    }
}
