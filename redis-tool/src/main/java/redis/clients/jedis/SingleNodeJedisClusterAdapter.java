package redis.clients.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Collections;

/**
 * @author yj
 * @since 2019-12-17 21:22
 **/
public class SingleNodeJedisClusterAdapter extends JedisCluster {

    public SingleNodeJedisClusterAdapter(HostAndPort node, String password) {
        super(Collections.emptySet(), DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, DEFAULT_MAX_ATTEMPTS, password, new GenericObjectPoolConfig<Jedis>());
        connectionHandler = new SingleNodeJedisClusterConnectionHandlerAdapter(node, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, password, new GenericObjectPoolConfig<Jedis>());
    }
}
