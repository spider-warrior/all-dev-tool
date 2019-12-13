package cn.t.tool.redistool.common;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class RedisConfiguration {
    private int connectionTimeout;
    private int soTimeout;
    private int maxAttempts;
    private String password;
    private final GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    private final Set<HostAndPort> hostAndPortSet = new HashSet<>();

    public RedisConfiguration(Properties properties) {
        String connectionTimeoutStr = properties.getProperty("redis.connection.timeout", "5000");
        String soTimeoutStr = properties.getProperty("redis.socket.timeout", "5000");
        String maxAttemptsStr = properties.getProperty("redis.attempt.max", "3");
        password = properties.getProperty("redis.password", "");
        connectionTimeout = Integer.parseInt(connectionTimeoutStr);
        soTimeout = Integer.parseInt(soTimeoutStr);
        maxAttempts = Integer.parseInt(maxAttemptsStr);
        //hosts
        String hostStr = properties.getProperty("redis.host", "localhost");
        String[] hosts = hostStr.split(",");
        for (String host : hosts) {
            String[] elements = host.split(":");
            hostAndPortSet.add(new HostAndPort(elements[0], Integer.parseInt(elements[1])));
        }
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GenericObjectPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public Set<HostAndPort> getHostAndPortSet() {
        return hostAndPortSet;
    }
}
