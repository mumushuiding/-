package com.crm.springboot.config;

import java.sql.SQLException;  
import javax.sql.DataSource;  
import org.springframework.beans.factory.annotation.Value;  
import org.springframework.boot.web.servlet.FilterRegistrationBean;  
import org.springframework.boot.web.servlet.ServletRegistrationBean;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
import org.springframework.context.annotation.Primary;  
import com.alibaba.druid.pool.DruidDataSource;  
import com.alibaba.druid.support.http.StatViewServlet;  
import com.alibaba.druid.support.http.WebStatFilter;  

@Configuration  
public class DruidConfig {  
      
    @Value("${spring.datasource.url}")  
    private String dbUrl;  
  
    @Value("${spring.datasource.username}")  
    private String username;  
  
    @Value("${spring.datasource.password}")  
    private String password;  
  
    @Value("${spring.datasource.driverClassName}")  
    private String driverClassName;  
  
    @Value("${spring.datasource.druid.initial-size}")  
    private int initialSize;  
  
    @Value("${spring.datasource.druid.min-idle}")  
    private int minIdle;  
  
    @Value("${spring.datasource.druid.max-active}")  
    private int maxActive;  
  
    @Value("${spring.datasource.druid.max-wait}")  
    private int maxWait;  
  
    @Value("${spring.datasource.druid.time-between-eviction-runs-millis}")  
    private int timeBetweenEvictionRunsMillis;  
  
    @Value("${spring.datasource.druid.min-evictable-idle-time-millis}")  
    private int minEvictableIdleTimeMillis;  
  
    @Value("${spring.datasource.druid.validation-query}")  
    private String validationQuery;  
  
    @Value("${spring.datasource.druid.test-while-idle}")  
    private boolean testWhileIdle;  
  
    @Value("${spring.datasource.druid.test-on-borrow}")  
    private boolean testOnBorrow;  
  
    @Value("${spring.datasource.druid.test-on-return}")  
    private boolean testOnReturn;  
  
    @Value("${spring.datasource.druid.pool-prepared-statements}")  
    private boolean poolPreparedStatements;  
  
    @Value("${spring.datasource.druid.filters}")  
    private String filters;  
      
    @Value("${spring.datasource.druid.logSlowSql}")  
    private String logSlowSql;  
      
    @Bean  
    @Primary  
    public DataSource dataSource(){  
        //@Primary 注解作用是当程序选择dataSource时选择被注解的这个  
        DruidDataSource datasource = new DruidDataSource();  
        datasource.setUrl(dbUrl);  
        datasource.setUsername(username);  
        datasource.setPassword(password);  
        datasource.setDriverClassName(driverClassName);  
        datasource.setInitialSize(initialSize);  
        datasource.setMinIdle(minIdle);  
        datasource.setMaxActive(maxActive);  
        datasource.setMaxWait(maxWait);  
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);  
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);  
        datasource.setValidationQuery(validationQuery);  
        datasource.setTestWhileIdle(testWhileIdle);  
        datasource.setTestOnBorrow(testOnBorrow);  
        datasource.setTestOnReturn(testOnReturn);  
        datasource.setPoolPreparedStatements(poolPreparedStatements);   
        try {  
            datasource.setFilters(filters);  
        } catch (SQLException e) {  
           
            e.printStackTrace();  
        }  
        return datasource;  
    }  
      
      @Bean  
        public ServletRegistrationBean druidServlet() {  
            ServletRegistrationBean reg = new ServletRegistrationBean();  
            reg.setServlet(new StatViewServlet());  
            reg.addUrlMappings("/druid/*");  
            reg.addInitParameter("loginUsername", username);  
            reg.addInitParameter("loginPassword", password);  
           
            reg.addInitParameter("logSlowSql", logSlowSql);  
            return reg;  
            
        }  
  
        @Bean  
        public FilterRegistrationBean filterRegistrationBean() {  
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();  
            filterRegistrationBean.setFilter(new WebStatFilter());  
            filterRegistrationBean.addUrlPatterns("/*");  
            filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");  
            filterRegistrationBean.addInitParameter("profileEnable", "true");  
            return filterRegistrationBean;  
        }  
}
