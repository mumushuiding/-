package com.crm.springboot.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.crm.springboot.pojos.User;


@RestController
@RequestMapping(value="/demo")
public class DemoController{
	@RequestMapping(value="/getDemo")
	@ResponseBody
	public User getDemo(){
		User user=new User();
		user.setId(1);
		user.setLoginname("sssss林");
		Date birDate=new Date();
		user.setBirthday(birDate);
		return user;
	}
	
	
}
