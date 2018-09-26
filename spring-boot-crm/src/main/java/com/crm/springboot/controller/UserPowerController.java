package com.crm.springboot.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.JsonUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;

@Controller
@RequestMapping("/system/power")
public class UserPowerController {
	
	@Autowired
	private UserService userService;
	
    @RequestMapping("/{location}")
    public String loacate(Model model,@PathVariable String location,String username){
    	if("userList".equals(location)){
    		
    	}
    	
    	return "system/power/"+location;
    }
	@RequestMapping("/userInfo")
	@ResponseBody
	public String getUserInfo(Integer pageIndex,Integer pageSize,String username){
		
		JsonUtils.startPageHelper(pageIndex, pageSize);
		HashMap<String, Object> params = new HashMap<String,Object>();
		
		params.put("username", username);
		List<User> uList=userService.selectAllUserWithHashMap(params);

		return JsonUtils.formatListForPagination(uList, pageIndex, pageSize);
	}
	
}
