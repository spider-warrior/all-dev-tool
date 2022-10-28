package cn.t.tool.redistool.func;

import redis.clients.jedis.JedisPubSub;

public class DefaultChannelMessageHandler extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        System.out.printf("channel: %s received a message: %s%n", channel, message);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        System.out.printf("pattern： %s, channel: %s received a message: %s%n", pattern, channel, message);
    }

}
