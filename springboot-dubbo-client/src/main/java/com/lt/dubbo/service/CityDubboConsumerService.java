package com.lt.dubbo.service;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lt.dubbo.domain.City;
import com.lt.dubbo.domain.User;

@Component
public class CityDubboConsumerService {

	@Reference
	UserService userService;
//	@Reference
//	CityDubboService cityDubboService;
//	public void printCity(){
//		
//		String cityName="广州";
//		City city=cityDubboService.findCityByName(cityName);
//		System.out.println(city.toString());
//	}
	public User saveUser(){
		System.out.println(userService.toString());
		User user=new User();
		user.setUsername("lt");
		user.setLoginnam("linting");
		return userService.saveUser(user);
		
	}
}
