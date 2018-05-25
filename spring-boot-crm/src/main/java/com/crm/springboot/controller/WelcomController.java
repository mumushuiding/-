package com.crm.springboot.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.JsonUtils;

import javassist.expr.NewArray;
@Controller
public class WelcomController {
	private static Log log=LogFactory.getLog(WelcomController.class);
	@Autowired
	private ActivitiService activitiService;
	@Autowired
	private ProcessService processService;
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private ResponsibilityService responsibilityService;
	@RequestMapping(value={"/","index"})
	public String welcome(Model model,HttpSession session){
		User user=(User) session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		
		//查询当前员工，他所在的部门有哪些任务
		HashMap<String , Object> params=new HashMap<String, Object>();
		params.put("deptNames", userService.getDeptNames(user));
		List<String> processInstanceIds=processService.selectAllProcessInstanceIds(params);
		long candidateTaskNumber=activitiService.countListCandidateTasks(String.valueOf(user.getId()), processInstanceIds);
		model.addAttribute("candidateTaskNumber", candidateTaskNumber);
		
		//查询
		long assigneeTaskNumber=activitiService.countListAssigneeTasks(String.valueOf(user.getId()));
		model.addAttribute("assigneeTaskNumber", assigneeTaskNumber);
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
	@RequestMapping("/{location}")
	public String info(@PathVariable String location,Model model){
		if("home".equals(location)){

			  model.addAttribute("startDateOfYear", DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(new Date())));
			  log.info(DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(new Date())));
			  model.addAttribute("endDateOfYear", DateUtil.formatDefaultDate(new Date()));
			  
		}
		return location;
	}
	

}
