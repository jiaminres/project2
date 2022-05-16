package com.jiamin.config;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/*
* Feign 是对 Ribbon (即 RestTemplate + 负载均衡)的进一步包装，允许接口声明式编程
* OpenFeign 是 Feign的升级,允许与SpringMvc在相关注解的联合使用
* */
@Configuration
@EnableFeignClients(basePackages = "com.jiamin.service")
public class OpenFeignConfig {
}
