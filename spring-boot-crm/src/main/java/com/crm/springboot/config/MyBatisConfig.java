package com.crm.springboot.config;
import javax.sql.DataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.alibaba.druid.pool.DruidDataSource;


@Configuration
@MapperScan("com.crm.springboot.mapper")
public class MyBatisConfig {
    @Bean  
    @ConfigurationProperties(prefix = "spring.datasource")  
    public DataSource readDataSource() {  
        return new DruidDataSource();  
    } 

}
