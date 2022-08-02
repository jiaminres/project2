package com.jiamin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class DaoServerZkApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DaoServerZkApplication.class, args);
    }
}
