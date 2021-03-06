package cn.t.tool.redistool;

import cn.t.tool.redistool.func.DefaultChannelMessageHandler;

import java.util.*;

public class JedisUserInterface {

    private static final JedisHelper jedisHelper = new JedisHelper();

    public static void main(String[] args) {

        String KEY_QUERY = "1";
        String KEY_SET = "2";
        String KEY_DEL = "3";
        String KEY_EXIST = "4";
        String SUBSCRIBE_CHANNEL = "5";
        String PUBLISH_CHANNEL = "6";
        String EXIT = "99";

        List<String> keyOperations = new ArrayList<>();
        keyOperations.add(KEY_QUERY);
        keyOperations.add(KEY_SET);
        keyOperations.add(KEY_DEL);
        keyOperations.add(KEY_EXIST);
        keyOperations.add(SUBSCRIBE_CHANNEL);
        keyOperations.add(PUBLISH_CHANNEL);

        Map<String, String> functions = new LinkedHashMap<>();
        functions.put(KEY_QUERY, "key查询");
        functions.put(KEY_SET, "key设置");
        functions.put(KEY_DEL, "key删除");
        functions.put(KEY_EXIST, "key是否存在");
        functions.put(SUBSCRIBE_CHANNEL, "订阅消息");
        functions.put(PUBLISH_CHANNEL, "发布消息");
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
                    } else if (KEY_SET.equals(command)) {
                        System.out.println("请输入值:");
                        String value = scanner.nextLine();
                        jedisHelper.getJedisCluster().set(key, value);
                    } else if (KEY_DEL.equals(command)) {
                        jedisHelper.getJedisCluster().del(key);
                        System.out.println("del ok");
                    } else if (KEY_EXIST.equals(command)) {
                        System.out.println((jedisHelper.getJedisCluster().exists(key)) ? "存在" : "不存在");
                    } else if(SUBSCRIBE_CHANNEL.equals(command)) {
                        System.out.println("going to subscribe channel: " + key);
                        jedisHelper.getJedisCluster().psubscribe(new DefaultChannelMessageHandler(), key);
                    } else if(PUBLISH_CHANNEL.equals(command)) {
                        System.out.println("going to publish channel: " + key);
                        System.out.println("请输入值:");
                        String value = scanner.nextLine();
                        jedisHelper.getJedisCluster().publish(key, value);
                    } else {
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
