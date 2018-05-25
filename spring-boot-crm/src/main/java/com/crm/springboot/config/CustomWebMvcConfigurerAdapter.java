package com.crm.springboot.config;

import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.crm.springboot.interceptor.AuthorizationInterceptor;
import com.crm.springboot.interceptor.PowerInterceptor;

@Configuration
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter{
	/**
	 * 文件上传临时路径
	 */
	@Bean
	MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory=new MultipartConfigFactory();
		factory.setLocation(AvailableSettings.TEMP_UPLOAD_LOCATION);
		return factory.createMultipartConfig();
	}
	
	@Bean
	PowerInterceptor powerInterceptor(){          

		return new PowerInterceptor();
	}
	 @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	       registry.addInterceptor(powerInterceptor()).addPathPatterns("/**");  //对来自/user/** 这个链接来的请求进行拦截
	       //registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/**");
	    }

}
