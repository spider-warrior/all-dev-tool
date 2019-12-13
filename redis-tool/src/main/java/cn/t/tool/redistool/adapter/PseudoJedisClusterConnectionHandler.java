package cn.t.tool.redistool.adapter;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;

import java.util.Collections;

/**
 * @author yj
 * @since 2019-12-13 13:47
 **/
public class PseudoJedisClusterConnectionHandler extends JedisSlotBasedConnectionHandler {

    private final JedisPool jedispool;

    public PseudoJedisClusterConnectionHandler(HostAndPort hostAndPort, int timeout) {
        super(Collections.emptySet(), new GenericObjectPoolConfig(), timeout);
        jedispool = new JedisPool(hostAndPort.getHost(),hostAndPort.getPort());
    }

    public PseudoJedisClusterConnectionHandler(HostAndPort hostAndPort, int connectionTimeout, int soTimeout) {
        super(Collections.emptySet(), new GenericObjectPoolConfig(), connectionTimeout, soTimeout);
        jedispool = new JedisPool(hostAndPort.getHost(),hostAndPort.getPort());
    }

    public PseudoJedisClusterConnectionHandler(HostAndPort hostAndPort, int connectionTimeout, int soTimeout, String password) {
        super(Collections.emptySet(), new GenericObjectPoolConfig(), connectionTimeout, soTimeout, password);
        jedispool = new JedisPool(new GenericObjectPoolConfig(), hostAndPort.getHost(),hostAndPort.getPort(), connectionTimeout, password);
    }

    public Jedis getConnection() {
        return jedispool.getResource();
    }

    public Jedis getConnectionFromSlot(int slot) {
        return jedispool.getResource();
    }

    @Override
    public void close() {
        if(jedispool != null && !jedispool.isClosed()) {
            jedispool.close();
        }
    }
}
