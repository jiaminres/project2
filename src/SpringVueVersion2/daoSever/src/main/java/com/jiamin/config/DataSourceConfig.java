package com.jiamin.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/*
* 引入mybatis-spring-boot-starter
* 引入druid-spring-boot-starter
*   其实已经自动往容器注入数据源以及数据库框架
*
* 但其实也可以在自己往容器注入数据源以及数据库框架实例，用在
* 程序中存在多个数据源的情况，例如让一个sqlSessionFactory管理一个dataSource
* */

/*
* 数据库框架组件依赖关系
* 一个库对应一个sqlSessionFactory
* 一个sqlSessionFactory管理一个DataSource
* 一个sqlSessionTemplate管理一个sqlSessionFactory
* 一个TransactionManager管理一个DataSource
*
* */

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.student")
    public DataSource datasourceOfStudent(){
        return DruidDataSourceBuilder.create().build();
    }
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource datasourceOfRoot(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryOfStudent(@Qualifier("datasourceOfStudent") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryOfRoot(@Qualifier("datasourceOfRoot") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        /*
        * 因为mybatis是用户全手动配置，所以并没有借用mybatis-spring-boot-starter中通过绑定配置文件而达成的便利化操作，
        * 若想配置全局文件配置，只能手动声明
        * */
        //配置全局配置文件
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("/mybatisConfig.xml", this.getClass()));
        return sqlSessionFactoryBean.getObject();
    }

    /*
    * sqlSessionTemplate是一个线程安全类
    * 用于安全操作sqlSession
    * */
    @Bean
    public SqlSessionTemplate sqlSessionTemplateOfStudent(@Qualifier("sqlSessionFactoryOfStudent") SqlSessionFactory sqlSessionFactory){
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateOfRoot(@Qualifier("sqlSessionFactoryOfRoot") SqlSessionFactory sqlSessionFactory){
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

    @Configuration
    @MapperScan(basePackages = "com.jiamin.mappers",sqlSessionFactoryRef = "sqlSessionFactoryOfRoot")
    public static class RootMapperConfig{}

}
