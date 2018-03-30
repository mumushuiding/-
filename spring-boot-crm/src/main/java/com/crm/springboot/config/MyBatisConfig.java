package com.crm.springboot.config;
import javax.sql.DataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.alibaba.druid.pool.DruidDataSource;


@Configuration
@MapperScan("com.crm.springboot.mapper")
//@ImportResource(locations={"classpath:activiti-cfg.xml"})
public class MyBatisConfig {
//    @Bean  //会自动生成bean
//    @ConfigurationProperties(prefix = "spring.datasource")  
//    public DataSource dataSource() {  
//        return new DruidDataSource();  
//    } 
}
