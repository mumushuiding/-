package com.lt.dubbo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lt.dubbo.service.CityDubboConsumerService;

@RestController
public class UserController {
	@Autowired
	private CityDubboConsumerService service;
	@RequestMapping("/save")
	public Object saveUser(){
		System.out.println("************保存用户***********");
		return service.saveUser();
	}
}
