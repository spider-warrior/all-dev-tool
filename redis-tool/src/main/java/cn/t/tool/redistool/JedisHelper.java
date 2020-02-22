package cn.t.tool.redistool;

import cn.t.tool.redistool.common.RedisConfiguration;
import cn.t.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.SingleNodeJedisClusterAdapter;

import java.io.IOException;
import java.io.InputStream;
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
            if(jc != null) {
                jc.close();
            }
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public JedisHelper(RedisConfiguration redisConfiguration) {
        Set<HostAndPort> hostAndPortSet = redisConfiguration.getHostAndPortSet();
        if(hostAndPortSet.size() == 1) {
            Object[] arr = hostAndPortSet.toArray();
            jc = new SingleNodeJedisClusterAdapter(
                (HostAndPort)arr[0],
                redisConfiguration.getPassword());
        } else {
            jc = new JedisCluster(redisConfiguration.getHostAndPortSet(),
                redisConfiguration.getConnectionTimeout(),
                redisConfiguration.getSoTimeout(),
                redisConfiguration.getMaxAttempts(),
                redisConfiguration.getPassword(),
                redisConfiguration.getPoolConfig());
        }
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
        return new RedisConfiguration(properties);
    }
}
