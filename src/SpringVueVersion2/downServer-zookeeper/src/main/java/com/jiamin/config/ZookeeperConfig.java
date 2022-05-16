package com.jiamin.config;

import com.out.cofig.IRuleConfig;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;


/*
* spring框架提供的RestTemplate远程访问工具，如果标注@LoadedBalanced注解，则会采用 IRule接口实现类的负载均衡
* 算法，默认是轮询。
*
* 所谓的Ribbon其实只是RestTemplate + 负载均衡
* Ribbon的依赖包在Zookeeper,Eureka的依赖包下已经被导入
* 我们可以通过RibbonClient注解指定某个微服务的负载均衡算法，通过传入微服务名和标注@Configure的类
*    格外注意，该@Configure应该独立于Springboot扫描包之外，不然RestTemplate访问任意微服务均将替换为该负载均衡算法，达不到
*    个性化配置
*
*
* */
//@Configuration
//@EnableDiscoveryClient
/*访问daoServer服务，采用随机负载均衡*/
//@RibbonClient(name="daoServer",configuration = IRuleConfig.class)
public class ZookeeperConfig {

}
