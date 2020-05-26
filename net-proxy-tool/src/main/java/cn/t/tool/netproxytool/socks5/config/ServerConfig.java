package cn.t.tool.netproxytool.socks5.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yj
 * @since 2020-05-26 20:04
 **/
public class ServerConfig {

    /**
     * 用户配置
     */
    private List<UserConfig> userConfigList = new ArrayList<>();

    public List<UserConfig> getUserConfigList() {
        return userConfigList;
    }

    public void setUserConfigList(List<UserConfig> userConfigList) {
        this.userConfigList = userConfigList;
    }
}
