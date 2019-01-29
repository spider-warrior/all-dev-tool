package cn.t.tool.redistool;

import cn.t.tool.redistool.common.RedisConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;

public class JedisHelper {

    private static final Logger logger = LoggerFactory.getLogger(JedisHelper.class);

    private final JedisCluster jc;

    public JedisCluster getJedisCluster() {
        return jc;
    }

    public void close() {
        try {
            jc.close();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public JedisHelper(RedisConfiguration redisConfiguration) {
        jc = new JedisCluster(redisConfiguration.getHosts());
    }
}
