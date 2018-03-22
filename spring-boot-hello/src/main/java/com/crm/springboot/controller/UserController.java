package com.crm.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.User;
import com.crm.springboot.service.UserService;

@RestController
@RequestMapping(value="/user")
public class UserController {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@RequestMapping("/getUserById")
	public User getUserById(Integer id){
		
		User user=userService.getById(id);
		
		return user;
	}
	@RequestMapping("/save")
	@ResponseBody
	public User save(){
		User user=new User();
		user.setId(10010);
		user.setLoginname("efg");
		user.setPassword("123");
		user=userService.save(user);
		return user;
	}
	@RequestMapping("/update")
	@ResponseBody
	public User update(){
		
		User user=getUserById(10010);
		user.setLoginname("我被更新了");
		userService.update(user);
		return getUserById(10010);
	}
	@RequestMapping("/delete/{id}")
	public void delete(@PathVariable("id") Integer id){
		userService.deleteById(id);
	}
}
