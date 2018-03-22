package com.crm.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class WelcomController {
	@RequestMapping("index")
	public String welcome(){
		return "index";
	}
}
