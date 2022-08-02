package com.jiamin.config;

/*
* 因为SpringBoot只能对Redis单机进行自动装配，故集群只能手动配置
* 因为mybatis提供的mybatis和redis的缓存桥接包，只能是将单机版Redis作为缓存，所以先通过自动装配类向Spring注入RedisTemplate和
*    StringRedisTemplate,然后实现一个实现Cache接口的自定义缓存对象
* */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisClusterConfiguration;
//import org.springframework.data.redis.connection.RedisNode;
//import org.springframework.data.redis.connection.RedisPassword;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

@Configuration(proxyBeanMethods = true)
@ConfigurationProperties("spring.redis.cluster")
public class RedisClusterConfig {
    List<Integer> ports;
    String host;
    JedisPoolConfig poolConfig;

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    @Bean
    RedisClusterConfiguration redisClusterConfiguration(){
//        System.out.println(ports+"-----------------------------------------");
        List<RedisNode> nodes = new ArrayList<>();
        for(Integer port : ports){
            nodes.add(new RedisNode(host,port));
        }
        RedisClusterConfiguration configuration = new RedisClusterConfiguration();
        configuration.setClusterNodes(nodes);
        configuration.setPassword(RedisPassword.of("nvzhuangdalao1"));
        return configuration;
    }
    @Bean
    JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory factory = new JedisConnectionFactory(redisClusterConfiguration(),poolConfig);
        return factory;
    }
    @Bean
    RedisTemplate redisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }
    @Bean
    StringRedisTemplate stringRedisTemplate(){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory());
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return stringRedisTemplate;
    }

}
