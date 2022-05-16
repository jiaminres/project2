package com.jiamin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class DaoServerApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(DaoServerApplication.class, args);


    }

}
