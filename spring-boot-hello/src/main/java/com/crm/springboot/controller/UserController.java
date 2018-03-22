package com.crm.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
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
		
		User user=userService.getUserById(id);
		
		return user;
	}
}
