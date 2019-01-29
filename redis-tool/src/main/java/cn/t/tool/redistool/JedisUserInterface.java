package cn.t.tool.redistool;

import cn.t.tool.redistool.common.RedisConfiguration;
import redis.clients.jedis.HostAndPort;

import java.util.*;

public class JedisUserInterface {


    private static final RedisConfiguration configuration = new RedisConfiguration(
        new HostAndPort("192.168.14.45", 6381),
        new HostAndPort("192.168.14.45", 6382),
        new HostAndPort("192.168.14.45", 6383),
        new HostAndPort("192.168.14.45", 6384),
        new HostAndPort("192.168.14.45", 6385),
        new HostAndPort("192.168.14.45", 6386)
    );

    private static final JedisHelper jedisHelper = new JedisHelper(configuration);

    public static void main(String[] args) {

        String KEY_QUERY = "1";
        String KEY_DEL = "2";
        String KEY_EXIST = "3";
        String EXIT = "99";

        List<String> keyOperations = new ArrayList<>();
        keyOperations.add(KEY_QUERY);
        keyOperations.add(KEY_DEL);
        keyOperations.add(KEY_EXIST);

        Map<String, String> functions = new LinkedHashMap<>();
        functions.put(KEY_QUERY, "key查询");
        functions.put(KEY_DEL, "key删除");
        functions.put(KEY_EXIST, "key是否存在");
        functions.put(EXIT, "退出");

        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("功能选项: ");
                for (Map.Entry<String, String> entry : functions.entrySet()) {
                    System.out.println(entry.getValue() + " : " + entry.getKey());
                }
                String command;
                do {
                    command = scanner.nextLine();
                } while (!functions.containsKey(command));
                if(EXIT.equalsIgnoreCase(command)) {
                    break;
                }
                if(keyOperations.contains(command)) {
                    System.out.println("请输入要操作的key:");
                    String key = scanner.nextLine();
                    if (KEY_QUERY.equals(command)) {
                        System.out.println(jedisHelper.getJedisCluster().get(key));
                    } else if (KEY_DEL.equals(command)) {
                        jedisHelper.getJedisCluster().del(key);
                        System.out.println("del ok");
                    } else if (KEY_EXIST.equals(command)) {
                        System.out.println((jedisHelper.getJedisCluster().exists(key)) ? "存在" : "不存在");
                    } else if (EXIT.equals(command)) {
                        System.out.println("该功能未实现");
                    }
                } else {
                    System.out.println("该功能未实现");
                }
                System.out.println("==========================================================================");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("exit, bye!");
    }
}
