package com.crm.springboot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crm.springboot.service.ProcessService;

@Controller
@RequestMapping("/process")
public class ProcessController {
	@Autowired
	private ProcessService processService;
	@RequestMapping("/deleteProcess/{ids}")
	public String deleteProcess(@PathVariable String ids,HttpServletRequest request){
		String referer=request.getHeader("referer");
		processService.deleteProcessByProcessInstanceIds(ids.split(","));
		
		return "redirect:"+referer;
	}
	
}
