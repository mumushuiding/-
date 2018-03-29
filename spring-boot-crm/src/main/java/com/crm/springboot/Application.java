package com.crm.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 该注解指定项目为springboot,由此类当作程序入口自动装备web依赖环境
 * @author Administrator
 *
 */
@EnableCaching
@SpringBootApplication
@MapperScan("com.crm.springboot.mapper")
@RestController
public class Application {
	//@RestController返回json字符串的数据，直接可以编写RESTFul的接口
    @RequestMapping("/helloworld")
    String home(){
    	
    	return "hello world!!!!!!!!!!!!23!";
    }
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

}
