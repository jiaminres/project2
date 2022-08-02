package com.jiamin.config;

import org.apache.ibatis.cache.Cache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class MyRedisClusterCache implements Cache, InitializingBean {
    //提供一个接受 String 参数作为 id 的构造器 缓存的唯一标识，不用自己赋值，每次缓存任何对象时，都会自动生成一个唯一的ID
    private String id;
    //依赖注入redis集群连接的数据源
    private static JedisConnectionFactory jedisConnectionFactory;
    //依赖注入RedisTemplate
    private static RedisTemplate redisTemplate;
    //依赖注入StringRedisTemplate
    private static StringRedisTemplate stringRedisTemplate;
    //实例化读写策略类 读读共享 读写互斥 写读互斥 写写互斥
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory0;
//    redisTemplcate是stringRedisTemplate的父类，所以装配时不能用同一类型装配
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate0;
    @Autowired
    private StringRedisTemplate stringRedisTemplate0;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisConnectionFactory = jedisConnectionFactory0;
        redisTemplate = redisTemplate0;
        stringRedisTemplate = stringRedisTemplate0;

    }

    // 空构造
    //因为二级缓存会根据不同的mapper文件创建不同的Cache实例，有了id就可以区别不同mappeer作用域进行数据存储，在删除也要确保不要波及其他作用域里的数据
    public MyRedisClusterCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        this.id = id;
    }

    public  MyRedisClusterCache(){}


    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        System.out.println("二级缓存存入" + "key: "+key +"value: "+ value);
        HashOperations hashOps = redisTemplate.opsForHash();
        hashOps.put(id, key, value);
    }

    @Override
    public Object getObject(Object key) {
        HashOperations hashOps = redisTemplate.opsForHash();
        return hashOps.get(id, key);
    }

    @Override
    public Object removeObject(Object key) {
        HashOperations hashOps = redisTemplate.opsForHash();
        Object o = hashOps.get(id, key);
        hashOps.delete(id,key);
        return o;
//获取连接对象
//        RedisConnection connection = jedisConnectionFactory.getConnection();
//        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
//        byte[] serializeKey = jdkSerializationRedisSerializer.serialize(key);
//        return connection.expire(serializeKey, 0);
    }

    @Override
    public void clear() {
        redisTemplate.delete(id);
    }

    @Override
    public int getSize() {
        RedisConnection connection = jedisConnectionFactory.getConnection();
        Long aLong = connection.dbSize();
        return Integer.valueOf(aLong.toString());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
