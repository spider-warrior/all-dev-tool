import cn.t.tool.redistool.JedisHelper;
import cn.t.tool.redistool.common.RedisConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;

import java.io.IOException;

public class JedisHelperTest {

    private static final Logger logger = LoggerFactory.getLogger(JedisHelperTest.class);

    private JedisHelper jedisHelper;

    @Before
    public void init() {
        RedisConfiguration configuration = new RedisConfiguration(
            new HostAndPort("192.168.14.45", 6381),
            new HostAndPort("192.168.14.45", 6382),
            new HostAndPort("192.168.14.45", 6383),
            new HostAndPort("192.168.14.45", 6384),
            new HostAndPort("192.168.14.45", 6385),
            new HostAndPort("192.168.14.45", 6386)
        );
        jedisHelper = new JedisHelper(configuration);
    }

    @Test
    public void getJedisClusterTest() {
        logger.info("jedis cluster: {}", jedisHelper.getJedisCluster());
    }

    @After
    public void destroy() throws IOException {
        jedisHelper.getJedisCluster().close();
    }
}
