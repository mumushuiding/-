package com.crm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.crm.pojos.User;
import com.crm.services.UserService;

@Controller
@RequestMapping(value="/user")
@SessionAttributes("user")
public class UserController {
	@Autowired
	
	private UserService UserService;
	@ModelAttribute
	public void userMode(String loginname,String password,ModelMap modelMap){
		User user=new User();
		user.setLoginname(loginname);
		user.setPassword(password);
		modelMap.addAttribute("user", user);
	}
	@RequestMapping(value="/registerForm")
	public String registerForm(Model model){
		User user=new User();
		
		//性别
		Map<Integer, String> sexMap=new HashMap<Integer, String>();
		sexMap.put(1, "男");
		sexMap.put(2,"女");
		user.setSex(1);
		model.addAttribute("sexMap", sexMap);
		
		//部门
		Map<Integer, String> deptMap=new HashMap<Integer, String>();
		deptMap.put(1,"财务部");
		deptMap.put(2, "开发部");
		user.getDept().setId(2);;
		model.addAttribute("deptMap", deptMap);
		user.setUsername("jack");
		
		
		model.addAttribute("user",user);
		
		return "user/register";
	}
	@RequestMapping(value="/loginForm")
	public String loginForm(ModelMap modelMap){
		User user=(User)modelMap.get("user");
		System.out.println(user);
		return "user/login";
	}
	@RequestMapping(value="/login")
	public String login(ModelMap modelMap){
		User user=(User)modelMap.get("user");
		
		return null;
		
	}
}
