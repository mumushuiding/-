package com.crm.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/advice")
public class AdviceController {
	
	@RequestMapping("/var1")
	public String adviceLocate(@PathVariable String var1){
        
		return "user/advice/"+var1;
	}

}
