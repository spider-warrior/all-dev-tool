package cn.t.tool.redistool.func;

import redis.clients.jedis.JedisPubSub;

public class DefaultChannelMessageHandler extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        System.out.println(String.format("channel: %s received a message: %s", channel, message));
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        System.out.println(String.format("pattern： %s, channel: %s received a message: %s", pattern, channel, message));
    }

}
