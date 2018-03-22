package com.crm.springboot.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.crm.springboot.pojos.User;

/**
 * @Description 单数据源配置
 * @author Administrator
 *
 */
@Configuration
@MapperScan(basePackages="com.crm.springboot.mapper")
public class DataSourceConfiguration {
	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource readDataSource(){
		
		return new DruidDataSource();
	}
}
