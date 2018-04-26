package com.crm.springboot.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.alibaba.druid.sql.visitor.functions.Locate;
import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.TaskVO;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javassist.expr.NewArray;


@Controller
@RequestMapping(value="/user")
@SessionAttributes(value="sysuser")
public class UserController {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	private ActivitiService activitiService;
	
	
	
	@RequestMapping(value="/{location}")
	public String locate(@PathVariable String location,Model model,HttpSession session,HttpServletRequest request){
		model.addAttribute("user", new User());
		String referer=request.getHeader("referer");
		session.setAttribute("referer", request.getHeader("referer"));
		if("loginForm".equals(location)){
			
			if(referer.contains("/user/updateForm")){
				session.setAttribute("referer", "/index");
			}
			
		}
       if("registerForm".equals(location)){
			List<Dept> depts=userService.selectAllDept();
			List<Post> posts=userService.selectAllPost();
			model.addAttribute("depts", depts);
			model.addAttribute("posts", posts);
		}
		//用户申请的任务列表
		if("applyList".equals(location)){
			return "user/activiti/"+location;
		}

		return "user/"+location;
	}
	/**
	 * 根据浏览器输入的location值，进行跳转
	 * @param location
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{location}/{parameter}")
	public String locate(@PathVariable String location,@PathVariable Serializable parameter,Model model){
		
		if("updateForm".equals(location)){
			User user=new User();
			if(parameter!=null) user=userService.getById(String.valueOf(parameter));
			List<Dept> depts=userService.selectAllDept();
			List<Post> posts=userService.selectAllPost();
			model.addAttribute("depts", depts);
			model.addAttribute("posts", posts);
			model.addAttribute("user", user);
		}

		return "user/"+location;
	}
	

/**
 * **********************************************流程************************************************
 */
	/**
	 * 个人中心
	 * @return
	 */
	@RequestMapping(value="/user")
	public String userCenter(){
		return "user/user";
	}
	/**
	 * 个人中心：初始化加载页面
	 * @return
	 */
	@RequestMapping(value="/ini")
	public String ini(){
		return "user/ini";
	}

	
//************************************************登录验证************************************************
	/**
	 * 登录
	 * @param user
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String login(@ModelAttribute("user") User user,Model model,HttpSession session,HttpServletRequest request){
		
		String referer=(String) session.getAttribute("referer");
        String[] toIndex={"save","login"};
		

		String msg="";
		User u = null;
		if(userService.getBySomething(user).size()>0) u=userService.getBySomething(user).get(0);
		
	    if(u!=null){
	    	msg="登录成功";
	    	model.addAttribute("msg",msg);
	    	model.addAttribute("sysuser", u);
	    	String servletPath=request.getServletPath();
	    	System.out.println("servletPath="+servletPath);
	    	System.out.println(referer.split("/")[referer.split("/").length-1]);
	    	if(Arrays.asList(toIndex).contains(referer.split("/")[referer.split("/").length-1])){
	    		return "/index";
	    	}
			
	    	return "redirect:"+referer;
	    }else{
	    	msg="帐号或者密码错误";
	    	model.addAttribute("msg",msg);
	    }
	    
		return "user/loginForm";
	}
	/**
	 * 登出
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(Model model,HttpSession session,SessionStatus sessionStatus){
		session.invalidate();
		sessionStatus.setComplete();
		
		
		return "redirect:/user/loginForm";
	}
//*****************************************增删改查***********************************************	
	/**
	 * 查询
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/get/{id}")
	public String get(@PathVariable("id") Serializable id,Model model){
		
		User user=userService.getById(String.valueOf(id));
		model.addAttribute("user",user);
		
		return "user/resList";
	}
	
	/**
	 * 保存
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String save(@ModelAttribute("user") User user,Model model){
		String msg="";
		if(userService.getBySomething(user).size()>0){
			msg="用户已经存在";
			model.addAttribute("msg", msg);
			return "user/registerForm";
		}
		userService.saveUserWithPostIdsAndDeptIds(user);
		return "user/loginForm";
	}
	/**
	 * 更新
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public String update(@ModelAttribute("user") User user){
		
		
		userService.update(user);
		
		return "redirect:/user/loginForm";
	}
	/**
	 * 批量删除
	 * @param id
	 */
	@RequestMapping("/delete/{ids}")
	public String delete(@PathVariable("ids") String ids){
		//userService.deleteById(id);
		userService.deleteUsersByUserIds(ids.split(","));
		for (String  id : ids.split(",")) {
			activitiService.deleteUser(id);
		}
		return "redirect:/system/power/userList";
	}
	
}
