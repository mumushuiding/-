package com.crm.springboot.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/activiti")
public class ActivitiController {
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RuntimeService runtimeService;
	@RequestMapping("/test")
	@ResponseBody
	public void test(){
		System.out.println("-----------taskService="+taskService);
		System.out.println("-----------processEngine="+processEngine);
	}
	@RequestMapping("/start")
	@ResponseBody
	public void startProcess(){
		System.out.println("启动流程前-------------");
		runtimeService.startProcessInstanceById("test");
	}
}
