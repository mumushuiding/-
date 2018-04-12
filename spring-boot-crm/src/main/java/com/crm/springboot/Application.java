package com.crm.springboot;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 该注解指定项目为springboot,由此类当作程序入口自动装备web依赖环境
 * @author Administrator
 *
 */
@EnableCaching
@SpringBootApplication
@EnableTransactionManagement
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


	//以下为测试用
//	 @Bean
//	    InitializingBean usersAndGroupsInitializer() {
//	        return new InitializingBean() {
//	            @Autowired
//	            private RuntimeService runtimeService;
//	            @Autowired
//	            IdentityService identityService;
//	            public void afterPropertiesSet() throws Exception {
//	                Group group = identityService.newGroup("user7");
//	                group.setName("users");
//	                group.setType("security-role");
//	                identityService.saveGroup(group);
//	                User admin = identityService.newUser("kl7");
//	                admin.setPassword("kl2");
//	                identityService.saveUser(admin);
//	                Map<String, Object> variables = new HashMap<String, Object>();
//	                variables.put("kl","ckl");
//	                
//	                ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("hireProcessWithJpa", variables);
//	            }
//	        };
//	    }

}
