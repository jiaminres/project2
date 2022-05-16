package com.jiamin.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;


public class DruidConfig extends UnpooledDataSourceFactory {
    public DruidConfig() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/jiamin?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
        dataSource.setPassword("nvzhuangdalao1");
        dataSource.setUsername("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.dataSource = dataSource;
    }
}
