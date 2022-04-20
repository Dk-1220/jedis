package com.dk.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisDemo01 {

    Jedis jedis = null;

    @Before
    public void connect() {
        //连接指定的redis，需要ip地址和端口号
        jedis = new Jedis("192.168.95.128", 6379);
    }

    @Test
    // 测试连接
    public void testConnect() {
        String ping = jedis.ping();
        System.out.println(ping);
    }

    @Test
    // 测试key
    public void testKeys() {
        //获取所有key
        Set<String> keys = jedis.keys("*");

        for (String key : keys) {
            System.out.println(key);
        }

        //判断是否存在某个key
        System.out.println("是否存在k1:" + jedis.exists("k1"));
        //测试某个key的过期时间
        System.out.println("k1的存活时间:" + jedis.ttl("k1"));
    }

    @Test
    // 测试String
    public void testString() {
        System.out.println("获取K1的值：" + jedis.get("k1"));

        jedis.msetnx("k11", "v12", "k22", "v22", "k33", "v33");
        System.out.println(jedis.mget("k11", "k22", "k33"));
    }

    @Test
    // 测试list
    public void testList() {
        jedis.lpush("mylist", "1", "2", "3", "4");
        List<String> list = jedis.lrange("mylist", 0, -1);
        for (String element : list) {
            System.out.println(element);
        }
    }

    @Test
    // 测试set
    public void testSet() {
        //添加元素
        jedis.sadd("mySet", "Jack", "Marry", "Tom", "Tony");
        //删除指定元素
        jedis.srem("mySet", "Tony");
        //获取指定key的元素
        Set<String> smembers = jedis.smembers("mySet");
        for (String member : smembers) {
            System.out.println(member);
        }
    }

    @Test
    // 测试hash
    public void testHash() {
        jedis.hset("myHash", "username", "Jack");
        jedis.hset("myHash", "password", "123123");
        jedis.hset("myHash", "age", "11");
        //将多个数据封装为一个map
        Map<String, String> map = new HashMap<String, String>();
        map.put("gender", "male");
        map.put("department", "研发部");
        //批量设置多个数据
        jedis.hmset("myHash", map);
        List<String> values = jedis.hmget("myHash", "username", "password");
        for (String val : values) {
            System.out.println(val);
        }
    }

    @Test
    // 测试zset
    public void testZset() {
        jedis.zadd("myZset", 100, "math");
        //将多个数据封装为一个map
        Map<String, Double> subject = new HashMap<String, Double>();
        subject.put("chinese", 88d);
        subject.put("english", 86d);
        //批量添加数据
        jedis.zadd("myZset", subject);
        Set<String> zset = jedis.zrange("myZset", 0, -1);
        for (String val : zset) {
            System.out.println(val);
        }
    }

    @After
    public void close() {
        // 关闭连接
        jedis.close();
    }
}
