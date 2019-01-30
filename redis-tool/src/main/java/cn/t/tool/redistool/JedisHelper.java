package cn.t.tool.redistool;

import cn.t.tool.redistool.common.RedisConfiguration;
import cn.t.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class JedisHelper {

    private static final Logger logger = LoggerFactory.getLogger(JedisHelper.class);

    private JedisCluster jc;

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

    public JedisHelper() {
        this(tryRedisConfiguration());
    }

    private static RedisConfiguration tryRedisConfiguration() {
        Properties properties = new Properties();
        try (
            InputStream is = FileUtil.getResourceInputStream(JedisHelper.class, "/redis-cluster.properties")
        ) {
            if(is == null) {
                logger.error("redis集群配置文件未找到: {}", "redis-cluster.properties");
            } else {
                properties.load(is);
            }
        } catch (IOException e) {
            logger.error("", e);
        }
        String hostStr = properties.getProperty("redis.host", "localhost");
        String[] hosts = hostStr.split(",");
        Set<HostAndPort> hostAndPortSet = new LinkedHashSet<>();
        for (String host : hosts) {
            String[] elements = host.split(":");
            hostAndPortSet.add(new HostAndPort(elements[0], Integer.parseInt(elements[1])));
        }
        return new RedisConfiguration(hostAndPortSet);
    }
}
