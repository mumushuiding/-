package com.crm.springboot.controller;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;


import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.UserService;

import com.crm.springboot.utils.JsonUtils;




@Controller
@RequestMapping(value="/user")
@SessionAttributes(value="sysuser")
public class UserController {
	private static final Log log=LogFactory.getLog(UserController.class);
	@Autowired
	private UserMapper userMapper;
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	private ActivitiService activitiService;
	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String sender;
	
	@RequestMapping(value="/{location}")
	public String locate(@PathVariable String location,Model model,HttpSession session,HttpServletRequest request,
			@RequestParam(required=false) String msg) throws UnsupportedEncodingException{
		model.addAttribute("user", new User());
	
		if(msg!=null && !"".equals(msg)) {
			
			model.addAttribute("msg", URLDecoder.decode(msg, "UTF-8"));
		};
		String referer=request.getHeader("referer");
		if(referer!=null&&referer.indexOf("?msg")!=-1){
			referer=referer.substring(0, referer.indexOf("?msg"));
		}
		session.setAttribute("referer", request.getHeader("referer"));
       
		if("loginForm".equals(location)){
			
			
			if(referer==null||referer.contains("/user/updateForm")||referer.contains("/user/forgotPasswordForm")){
				session.setAttribute("referer", "/home");
			}

			
		}
       if("registerForm".equals(location)){
    	   
			List<Dept> depts=userService.selectDistinctSecondLevelDept();
			model.addAttribute("depts", depts);
			List<Post> posts=userService.selectAllPost();
			
			model.addAttribute("posts", posts);
		}
       if("updateForm".equals(location)){
			User user=(User) session.getAttribute("sysuser");
			List<Dept> depts=userService.selectDistinctSecondLevelDept();
			model.addAttribute("depts", depts);
			List<Post> posts=userService.selectAllPost();
			model.addAttribute("posts", posts);
			model.addAttribute("user", user);
		}
       if("forgotPasswordForm".equals(location)){
			
			model.addAttribute("user", new User());
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
	public String locate(@PathVariable String location,@PathVariable String parameter,Model model,HttpSession session,HttpServletRequest request){
         
		if("updateForm".equals(location)){
			
			User user=new User();
			//修改别人
			if(parameter!=null){
				user=userService.getById(String.valueOf(parameter));
			}else {
				user=(User) session.getAttribute("sysuser");
			}
			String deptIds="";
			for (UserLinkDept userLinkDept :user.getUserLinkDepts()) {
				deptIds+=userLinkDept.getFirstLevel().getDid()+","+userLinkDept.getSecondLevel().getDid();
			}
			
			
			List<Post> posts=userService.selectAllPost();
			List<Dept> depts=userService.selectAllDept();
			model.addAttribute("deptIds", deptIds);
			model.addAttribute("depts", depts);
			model.addAttribute("posts", posts);
			model.addAttribute("user", user);
		}
        if("inChargeForm".equals(location)){
        	
        	String referer=request.getHeader("referer");
     		
     		session.setAttribute("referer", request.getHeader("referer"));
        	
    		List<Dept> depts=userService.selectDistinctSecondLevelDept();

    		UserLinkDept userLinkDept=new UserLinkDept();
    		userLinkDept.setUserId(Integer.valueOf(parameter));
    		model.addAttribute("depts", depts);
    		model.addAttribute("userLinkDept", userLinkDept);
    	}
		return "user/"+location;
	}
	/**
	 * ******************************部门************************************************
	 */
	@RequestMapping("/selectAllDepts")
	@ResponseBody
	public String selectAllDepts(
			@RequestParam(required=false) String level){
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("level", level);
		List<Dept> depts=userService.selectAllDepts(params);
		
		return JsonUtils.getGson().toJson(depts);
		
	}
	@RequestMapping("/selectDistinctSecondLevelDept")
	@ResponseBody
	public String selectDistinctSecondLevelDept(){
        List<Dept> depts=userService.selectDistinctSecondLevelDept();
		
		return JsonUtils.getGson().toJson(depts);
	}
	@RequestMapping("/selectFirstLevelDept/{secondLevelId}")
	@ResponseBody
	public String selectFirstLevelDept(@PathVariable String secondLevelId,
			@RequestParam(required=false) String param){
		StringBuffer result=new StringBuffer();
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("secondLevelId", secondLevelId);
		
		List<Dept> fdepts=userService.selectDistinctFirstLevelDept(params);
		if("name".equals(param)){
			for (Dept dept : fdepts) {
				result.append("<option value=\""+dept.getName()+"\">"+dept.getName()+"</option>");
		    }
		}else {
			for (Dept dept : fdepts) {
				result.append("<option value=\""+dept.getDid()+"\">"+dept.getName()+"</option>");
		    }
		}
        
		
		return result.toString();
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
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/login")
	public String login(@ModelAttribute("user") User user,Model model,HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException{
		
		String referer=(String) session.getAttribute("referer");
        String[] toIndex={"save","login","loginForm"};
		

		String msg="";
		User u = null;
		
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("phone", user.getPhone());
		if(userService.selectSingleUserWithHashMap(params)==null){
			return "redirect:"+referer+"?msg="+URLEncoder.encode("用户帐号不存在，请先注册 ", "utf-8");
		}
			
		if(userService.getBySomething(user).size()>0) u=userService.getBySomething(user).get(0);
		
	    if(u!=null){
	    	
	    	
	    	session.setAttribute("sysuser", u);
	    	session.setMaxInactiveInterval(36000);
	    	if(Arrays.asList(toIndex).contains(referer.split("/")[referer.split("/").length-1])){
	    		
	    		return "/index";
	    	}
	    	if(referer!=null)return "redirect:"+referer;
	    }else{
	    	msg="帐号或者密码错误";
	    	model.addAttribute("msg",msg);
	    }
	    
		return "/user/loginForm";
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
		
		userService.save(user);
		
		return "user/loginForm";
	}
	/**
	 * 更新
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public String update(@ModelAttribute("user") User user,HttpSession session,Model model){
        User u=(User) session.getAttribute("sysuser");
		userService.update(user);
		userService.saveAndUpdateUserLinkDeptWithUser(user);
		log.info("用户["+u.getId()+"]["+u.getUsername()+"]更新了 员工["+user.getId()+"]["+user.getUsername()+"]的资料");
		
		if(String.valueOf(u.getId()).equals(String.valueOf(user.getId()))){
			session.removeAttribute("sysuser");
	        model.addAttribute("sysuser", userService.getById(String.valueOf(user.getId())));
	        return "redirect:/responsibility/assessmentList/month";
		}
		return "redirect:"+session.getAttribute("referer");
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
	@RequestMapping("/selectUserLinkDeptsWithUserId/{userId}")
	@ResponseBody
	public String selectUserLinkDeptsWithUserId(@PathVariable String userId){
		
		List<UserLinkDept> userLinkDepts=userService.selectUserLinkDeptWithUserId(userId);
		System.out.println(JsonUtils.getGson().toJson(userLinkDepts));
		return JsonUtils.getGson().toJson(userLinkDepts);
	}
	@RequestMapping("/deleteUserLinkDept/{id}")
	public String deleteUserLinkDept(@PathVariable String id,HttpServletRequest request,@RequestParam(required=false) String username) throws UnsupportedEncodingException{
		String referer=request.getHeader("referer");
		System.out.println("refere:"+referer+"?username="+username);
		String[] ids=new String[1];
		ids[0]=id;
		userService.deleteUserLinkDeptByIds(ids);

		return "redirect:"+referer+"?username="+URLEncoder.encode(username, "utf-8");
	}
	@RequestMapping("/updateUserLinkDepts")
	public String updateUserLinkDepts(UserLinkDept userLinkDept,HttpSession session){
		String referer=(String) session.getAttribute("referer");
		
		
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("userId", userLinkDept.getUserId());
		
		List<UserLinkDept> userLinkDepts=userService.selectUserLinkDeptWithResutltType(params);
		
		params.remove("userId");
		
		//先删除全部旧有数据
//		for (UserLinkDept userLinkDept2 : userLinkDepts) {
//			params.put("id", userLinkDept2.getId());
//			userService.deleteUserLinkDept(params);
//		}
		
		params.remove("id");
		//添加新数据
		params.put("firstLevelIds",userLinkDept.getFirstLevelIds().split(","));
		params.put("userLinkDept", userLinkDept);
		userService.saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(params);
		
		return "redirect:"+referer;
		
	}
    @RequestMapping("/findPassword")
   
    public String findPassword(HttpServletRequest request,
    		Model model,
    		User user,
    		@RequestParam(required=false) String msg) throws UnsupportedEncodingException{
    	String referer=request.getHeader("referer");
		if(referer!=null&&referer.indexOf("?msg")!=-1){
			referer=referer.substring(0, referer.indexOf("?msg"));
		}
    	//首先查询 phone和email用户是否存在
		String email=user.getEmail();
    	user=userService.selectSingleUserAsResultType(user.getPhone(), user.getEmail());
    	if(user==null) {
    		return "redirect:"+referer+"?msg="+URLEncoder.encode("输入的邮箱与在《个人中心》设置邮箱不一致", "UTF-8");
    	}
    	//设置新密码
    	Random random=new Random();
    	String s="";
    	for (int i=0;i<6;i++)
    	{
    		s+=random.nextInt(10);
    	}
    	
    	
    	//通过邮箱发送新密码
    	
    	try {
    		
    		SimpleMailMessage message=new SimpleMailMessage();
        	message.setFrom(sender);
        	message.setTo(email);
        	message.setSubject("一线考核系统密码修改");
        	
        	message.setText("新密码："+s);
        	
        	javaMailSender.send(message);
        	
        	//重置密码
        	user.setPassword(s);
        	
        	userService.update(user);
        	
        	
        	return "redirect:"+referer+"?msg="+URLEncoder.encode("新密码已经发送到你的邮箱,也有可能在垃圾邮箱，请注意查收", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:"+referer+"?msg="+URLEncoder.encode("邮件发送失败", "UTF-8");
		}
    }
    
    
    @RequestMapping("/findUser/{phone}")
    @ResponseBody
    public String findUserByPhone(@PathVariable String phone){
    	User user=userService.selectSingleUserWithPhone(phone);
    	
    	return JsonUtils.getGson().toJson(user);
    }
	
}
