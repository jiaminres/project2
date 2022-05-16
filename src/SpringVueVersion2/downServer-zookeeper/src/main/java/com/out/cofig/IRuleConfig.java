package com.out.cofig;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLOutput;

@Configuration(proxyBeanMethods = false)
public class IRuleConfig {
    @Bean
    RandomRule randomRule(){
        return new RandomRulePlus();
    }

    public static class RandomRulePlus extends  RandomRule{
        public  RandomRulePlus(){
            super();
        }

        @Override
        public Server choose(ILoadBalancer lb, Object key) {
            System.out.println("使用随机负载均衡算法");
            return super.choose(lb, key);
        }

    }
}
