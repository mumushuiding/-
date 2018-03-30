package com.crm.springboot.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.druid.sql.visitor.functions.Locate;
import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.User;
import com.crm.springboot.service.UserService;

@Controller
@RequestMapping(value="/user")
@SessionAttributes(value="sysuser")
public class UserController {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	

	/**
	 * 根据浏览器输入的location值，进行跳转
	 * @param location
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{location}/{id}")
	public String locate(@PathVariable String location,@PathVariable Integer id,Model model){
		User user=new User();
		if(id!=null) user=userService.getById(id);
		 model.addAttribute("user", user);
		return "user/"+location;
	}
	@RequestMapping(value="/{location}")
	public String locate(@PathVariable String location,Model model){
		User user=new User();
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
	public String login(@ModelAttribute("user") User user,Model model,HttpServletRequest request){
		String msg="";
		user.setPhone("1234");
	    if(userService.checkLogin(user)){
	    	msg="登录成功";
	    	model.addAttribute("msg",msg);
	    	request.setAttribute("msg",msg);
	    	
	    	return "redirect:/index";
	    }else{
	    	msg="帐号或者密码错误";
	    	model.addAttribute("msg",msg);
	    }
	    
		return "javascript:history.go(-1)";
	}
//*****************************************增删改查***********************************************	
	/**
	 * 查询
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/get/{id}")
	public String get(@PathVariable("id") Integer id,Model model){
		
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
	@ResponseBody
	public User save(@ModelAttribute("user") User user,Model model){
		
		userService.save(user);

		return user;
	}
	/**
	 * 更新
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public User update(@ModelAttribute("user") User user){
		
		System.out.println("update:"+user.toString());
		userService.update(user);
		
		return user;
	}
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping("/delete/{id}")
	public void delete(@PathVariable("id") Integer id){
		userService.deleteById(id);
	}
}
