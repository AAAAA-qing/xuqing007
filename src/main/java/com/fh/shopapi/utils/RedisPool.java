package com.fh.shopapi.utils;

import redis.clients.jedis.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class RedisPool {

    private RedisPool() {

    }

    private static JedisCluster cluster;

    private static void initJedisCluster(){
        /*JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMinIdle(100);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(jedisPoolConfig, "192.168.59.128", 7020);*/
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(1000);
        // 最大空闲数
        poolConfig.setMaxIdle(100);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
        // Could not get a resource from the pool
        //poolConfig.setMaxWaitMillis(1000);
        Set<HostAndPort> nodes = new LinkedHashSet<>();
        nodes.add(new HostAndPort("192.168.150.128", 30001));
        nodes.add(new HostAndPort("192.168.150.128", 30002));
        nodes.add(new HostAndPort("192.168.150.128", 30003));
        nodes.add(new HostAndPort("192.168.150.128", 30004));
        nodes.add(new HostAndPort("192.168.150.128", 30005));
        nodes.add(new HostAndPort("192.168.150.128", 30006));
        cluster = new JedisCluster(nodes, poolConfig);

    }

    //静态块，只执行一次，在加载类的时候执行
    static {
        initJedisCluster();
    }

    public static JedisCluster getJedisCluster(){
        return cluster;
    }
}
