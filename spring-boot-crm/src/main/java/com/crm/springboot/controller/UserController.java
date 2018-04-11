package com.crm.springboot.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.crm.springboot.pojos.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.UserService;

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
		if("loginForm".equals(location)){
			
			session.setAttribute("referer", request.getHeader("referer"));
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
	@RequestMapping(value="/{location}/{id}")
	public String locate(@PathVariable String location,@PathVariable Serializable id,Model model){
		User user=new User();
		if("updateForm".equals(location)){
			if(id!=null) user=userService.getById(id);
		}
		model.addAttribute("user", user);
		return "user/"+location;
	}

//***************************************************用户考核 *****************************************
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
	/**
	 * 个人责任清单
	 * @return
	 */
	@RequestMapping(value="/res/{res}")
	public String restList(@PathVariable String res,Model model){
		String msg="";
		String data="[{\"name\":\"督查组点评\",\"url\":\"/user/res/"+res+"/Overseer\"},"
				   + "{\"name\":\"领导点评\",\"url\":\"/user/res/"+res+"/leaderForm\"},"
				   + "{\"name\":\"已经点评\",\"url\":\"/user/res/"+res+"/evaluatedList\"}]";
		
		if("resList".equals(res)){
		
			msg="责任清单";
			model.addAttribute("msg", msg);
			
			
		}
		
		if("month".equals(res)){
			msg="月度考核";
			model.addAttribute("msg", msg);
			model.addAttribute("data", data);
		}
		data="[{\"name\":\"领导点评\",\"url\":\"/user/res/"+res+"/leaderForm\"},"
				   + "{\"name\":\"群众点评\",\"url\":\"/user/res/"+res+"/publicForm\"},"
				   + "{\"name\":\"组织点评\",\"url\":\"/user/res/"+res+"/orgForm\"},"
				   + "{\"name\":\"已经点评\",\"url\":\"/user/res/"+res+"/evaluatedList\"}]";
		if("halfYear".equals(res)){
			msg="半年考核";
			model.addAttribute("msg", msg);
			model.addAttribute("data", data);
		}
		if("fullYear".equals(res)){
			msg="年度考核";
			model.addAttribute("msg", msg);
			model.addAttribute("data", data);
		}
		return "user/resList";
	}
	@RequestMapping(value="/res/{res}/{role}")
	public String assessForm(@PathVariable String res,@PathVariable String role){
		return "user/assessForm";
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
	public String login(@ModelAttribute("user") User user,Model model,HttpSession session){
		
		String referer=(String) session.getAttribute("referer");
		
		String msg="";
		User u = null;
		if(userService.getBySomething(user).size()>0) u=userService.getBySomething(user).get(0);
		
	    if(u!=null){
	    	msg="登录成功";
	    	model.addAttribute("msg",msg);
	    	model.addAttribute("sysuser", u);
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
		
		
		return "redirect:/index";
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
		
		User user=userService.getById(id);
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
		userService.save(user);

		return "user/loginForm";
	}
	/**
	 * 更新
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public String update(@ModelAttribute("user") User user,HttpServletRequest request){
		
		String referer = request.getHeader("Referer");
		System.out.println(referer);
		userService.update(user);
		  
		 
		return "redirect:/system/power/userList";
	}
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id){
		userService.deleteById(id);
		activitiService.deleteUser(id);
		return "redirect:/system/power/userList";
	}
}
