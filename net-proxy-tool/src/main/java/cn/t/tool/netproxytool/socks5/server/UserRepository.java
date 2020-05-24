package cn.t.tool.netproxytool.socks5.server;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-05-24 00:29
 **/
public class UserRepository {

    private static final Map<String, String> ALL_USERS = new HashMap<>();
    private static final Map<String, String> USER_SECURITY_MAPPING = new HashMap<>();

    public static void addUser(String username, String password) {
        ALL_USERS.put(username, password);
    }

    public static String getPassword(String username) {
        return ALL_USERS.get(username);
    }

    public static boolean exist(String username) {
        return ALL_USERS.containsKey(username);
    }

    public static void addUserSecurity(String username, String security) {
        USER_SECURITY_MAPPING.put(username, security);
    }

    public static String getUserSecurity(String username) {
        return USER_SECURITY_MAPPING.get(username);
    }

}
