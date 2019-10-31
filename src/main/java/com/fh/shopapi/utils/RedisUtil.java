package com.fh.shopapi.utils;

import redis.clients.jedis.JedisCluster;

public class RedisUtil {
    //redis中存数据
    public static void set(String key, String value) {
        JedisCluster resource = null;
        try {
            resource = RedisPool.getJedisCluster();
            resource.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    //获取redis中数据
    public static String get(String key) {
        JedisCluster resource = null;
        String result = null;
        try {
            resource = RedisPool.getJedisCluster();
            result = resource.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    //删除redis中数据
    public static Long del(String... key) {
        JedisCluster resource = null;
        Long del;
        try {
            resource = RedisPool.getJedisCluster();
            del = resource.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return del;
    }

    //根据key判断redis中数据是否超时
    public static boolean exists(String key) {
        JedisCluster resource = null;
        Boolean exists = false;
        try {
            resource = RedisPool.getJedisCluster();
            exists = resource.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return exists;
    }

    //根据key设置redis中数据的生命周期
    public static void expire(String key, int second) {
        JedisCluster resource = null;
        try {
            resource = RedisPool.getJedisCluster();
            resource.expire(key, second);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    //redis中存数据 并设置生命周期
    public static void setEx(String key, String value, int second) {
        JedisCluster resource = null;
        try {
            resource = RedisPool.getJedisCluster();
            resource.setex(key, second, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }


    }

    //redis中获取 map中的数据
    public static String hget(String key,String filed){
        JedisCluster resource = null;
        String result = null;
        try {
            resource = RedisPool.getJedisCluster();
            result = resource.hget(key, filed);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    //存储redis中 map中的数据
    public static void hset(String key,String filed){
        JedisCluster resource = null;
        try {
            resource = RedisPool.getJedisCluster();
            resource.hset(SystemConstant.PRODUCT_CART_MAP, key, filed);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    //删除redis中 map中的数据
    public static void hdel(String key,String filed){
        JedisCluster resource = null;
        try {
            resource = RedisPool.getJedisCluster();
            resource.hdel(key,filed);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
