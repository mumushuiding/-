package com.crm.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crm.springboot.pojos.User;

import javassist.expr.NewArray;
@Controller
public class WelcomController {
	@RequestMapping(value={"/","index"})
	public String welcome(Model model){
		model.addAttribute("user",new User());
		return "index";
	}
	@RequestMapping(value="main")
	public String main(){
		return "layout/center";
	}
	@RequestMapping(value="system")
	public String system(){
		return "system/system";
	}
}
