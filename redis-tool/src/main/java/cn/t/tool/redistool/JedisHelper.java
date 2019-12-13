package cn.t.tool.redistool;

import cn.t.tool.redistool.adapter.PseudoJedisCluster;
import cn.t.tool.redistool.common.RedisConfiguration;
import cn.t.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class JedisHelper {

    private static final Logger logger = LoggerFactory.getLogger(JedisHelper.class);

    private JedisCommands jedisCommands;

    public JedisCommands getJedisCommands() {
        return jedisCommands;
    }

    public void close() {
        try {
            if(jedisCommands != null) {
                if(jedisCommands instanceof JedisCluster) {
                    ((JedisCluster)jedisCommands).close();
                } else if(jedisCommands instanceof PseudoJedisCluster) {
                    ((PseudoJedisCluster)jedisCommands).close();
                } else {
                    throw new RuntimeException("未知的redis集群客户端实现");
                }
            }
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public JedisHelper(RedisConfiguration redisConfiguration) {
        Set<HostAndPort> hostAndPortSet = redisConfiguration.getHostAndPortSet();
        if(hostAndPortSet.size() == 1) {
            Object[] arr = hostAndPortSet.toArray();
            jedisCommands = new PseudoJedisCluster(
                (HostAndPort)arr[0],
                redisConfiguration.getConnectionTimeout(),
                redisConfiguration.getSoTimeout(),
                redisConfiguration.getPassword());
        } else {
            jedisCommands = new JedisCluster(redisConfiguration.getHostAndPortSet(),
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
