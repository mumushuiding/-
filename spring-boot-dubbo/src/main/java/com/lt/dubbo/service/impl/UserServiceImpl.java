package com.lt.dubbo.service.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.lt.dubbo.domain.User;
import com.lt.dubbo.service.UserService;

@Service(
        version = "${demo.service.version}"
)
@Component
public class UserServiceImpl implements UserService{

	@Override
	public User saveUser(User user) {
		System.out.println("*************保存用户："+user.toString());
		return user;
		
	}


}
