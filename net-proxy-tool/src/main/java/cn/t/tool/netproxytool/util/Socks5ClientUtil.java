package cn.t.tool.netproxytool.util;

import cn.t.tool.netproxytool.http.UserConfig;
import cn.t.util.common.StringUtil;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-05-24 09:31
 **/
public class Socks5ClientUtil {
    public static UserConfig analyseMainParamUserConfig(String param) {
        if(StringUtil.isEmpty(param)) {
            return null;
        }
        UserConfig userConfig = new UserConfig();
        String[] elements = param.split(" ");
        String socks5ServerElement = elements[0];
        String userElement = elements[1];
        String security = elements[2];
        analyseAndConfigSocks5Server(userConfig, socks5ServerElement);
        analyseAndConfigUser(userConfig, userElement);
        analyseAndConfigSecurity(userConfig, security);
        return userConfig;
    }

    public static void analyseAndConfigSocks5Server(UserConfig userConfig, String socks5ServerElement) {
        if(StringUtil.isEmpty(socks5ServerElement)) {
            return;
        }
        String[] elements = socks5ServerElement.split(":");
        userConfig.setSocks5ServerHost(elements[0]);
        if(elements.length > 1) {
            userConfig.setSocks5ServerPort(Short.parseShort(elements[1]));
        }
    }

    public static void analyseAndConfigUser(UserConfig userConfig, String userElement) {
        if(StringUtil.isEmpty(userElement)) {
            return;
        }
        String[] elements = userElement.split(":");
        userConfig.setUsername(elements[0]);
        if(elements.length > 1) {
            userConfig.setPassword(elements[1]);
        }
    }

    public static void analyseAndConfigSecurity(UserConfig userConfig, String security) {
        if(StringUtil.isEmpty(security)) {
            return;
        }
        userConfig.setSecurity(security);
    }
}
