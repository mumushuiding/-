package com.crm.springboot.config;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import com.alibaba.druid.pool.DruidDataSource;


@Configuration
@MapperScan("com.crm.springboot.mapper")
public class MyBatisConfig {


}
