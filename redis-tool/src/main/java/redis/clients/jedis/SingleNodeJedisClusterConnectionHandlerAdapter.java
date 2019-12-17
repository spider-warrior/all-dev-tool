package redis.clients.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Collections;

/**
 * @author yj
 * @since 2019-12-17 21:30
 **/
public class SingleNodeJedisClusterConnectionHandlerAdapter extends JedisClusterConnectionHandler {

    private final JedisPool jedispool;

    public SingleNodeJedisClusterConnectionHandlerAdapter(HostAndPort node, int connectionTimeout, int soTimeout, String password, GenericObjectPoolConfig poolConfig) {
        super(Collections.emptySet(), poolConfig, connectionTimeout, soTimeout, password);
        jedispool = new JedisPool(new GenericObjectPoolConfig(), node.getHost(),node.getPort(), connectionTimeout, password);
    }

    @Override
    public Jedis getConnection() {
        return jedispool.getResource();
    }

    @Override
    public Jedis getConnectionFromSlot(int slot) {
        return jedispool.getResource();
    }


}
