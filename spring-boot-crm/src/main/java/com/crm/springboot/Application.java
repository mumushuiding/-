package com.crm.springboot;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.Group;
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

import com.crm.springboot.mapper.SysPowerMapper;
import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.GroupTable;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;


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


	//以下为测试用,启动application会运行一次
	 @Bean
	   InitializingBean deploymentInitializer(){
		return new InitializingBean() {
			@Autowired
			private ActivitiService activitiService;
			@Override
			public void afterPropertiesSet() throws Exception {
				
				activitiService.deleteLowerVersionProcessDefinitions();
				
			}
		};
		 
	 }
//	
	 @Bean
	    InitializingBean usersAndGroupsInitializer() {
	        return new InitializingBean() {
	            @Autowired
	            private SysPowerService sysPowerService;
	            @Autowired
	            private UserService userService;
	            @Autowired
	            
	            private ActivitiService activitiService;
	            public void afterPropertiesSet() throws Exception {
	            	
	            	//用户自己的表格同activiti表格内容同步
	                List<User> users=userService.selectAllUser();
	                List<GroupTable> groupTables=sysPowerService.selectAllGroups();
	                List<GroupManager> groupManagers=sysPowerService.selectAllGroupManagers();
	                //同步表格user同act_id_user内容 
	                for(User u:users){
	                	org.activiti.engine.identity.User au=activitiService.selectUser(String.valueOf(u.getId()));
	                	if(au==null){
	                		activitiService.saveUser(String.valueOf(u.getId()));
	                	}
	                }
	                //同步用户组
	                for(GroupTable groupTable:groupTables){
	                	Group group=activitiService.selectGroupById(groupTable.getGroupid());
	                	if(group==null){
	                		activitiService.saveGroup(groupTable);
	                	}
	                }
	                //同步act_id_membership和groupmanager表格的内容
	                for(GroupManager gm:groupManagers){
	                	activitiService.asnyMembership(gm);
	                }
	            }
	        };
	    }

}
