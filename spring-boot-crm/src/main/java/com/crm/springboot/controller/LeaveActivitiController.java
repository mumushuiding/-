package com.crm.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.springboot.service.LeaveInfoService;

@RestController
public class LeaveActivitiController {
	@Autowired
	private LeaveInfoService leaveInfoService;
	
	@RequestMapping("/addLeaveInfo")
	public String addLeaveInfo(String msg){
		leaveInfoService.saveLeaveInfo(msg);
		return "新增成功..";
	}
}
