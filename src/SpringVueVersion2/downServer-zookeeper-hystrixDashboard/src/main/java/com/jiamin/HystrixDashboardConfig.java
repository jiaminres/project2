package com.jiamin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashboardConfig {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardConfig.class, args);
    }
}
