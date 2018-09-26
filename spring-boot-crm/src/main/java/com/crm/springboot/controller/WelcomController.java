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
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.DictionaryService;
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
	private DictionaryService dictionaryService;
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
			  
			  //查询考核组信息
			  List<String> groupList=dictionaryService.selectDistinctNameWithType("考核组");
			  if(groupList.size()==0) throw new RuntimeException("数据库info_dict表，不存在type为【考核组】的条目");
			  model.addAttribute("assessGroups", groupList.toArray(new String[groupList.size()]));
              //当今年的月度考核还未开始时，先显示去年的排名
			  HashMap<String, Object> params=new HashMap<String, Object>();
		      Date date=new Date();

		      int year=DateUtil.getYear(date);
		      params.put("checked", "1");
		      Date startDateOfYear=DateUtil.getFirstDayOfYear(date);
		      Date endDateOfYear=date;
			  params.put("startDate", DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(date)));
			  params.put("endDate",DateUtil.formatDefaultDate(date));
			  if(responsibilityService.countTotalMarkAndUser(params)==0) {
				  year=year-1;
				  date=DateUtil.addYears(date, -1);
				  startDateOfYear=DateUtil.getFirstDayOfYear(date);
				  endDateOfYear=DateUtil.getLastDayOfYear(date);
			  }
			  
			  model.addAttribute("year", year);
			  //查询个人具体得分时会用到
			  model.addAttribute("startDateOfYear", DateUtil.formatDefaultDate(startDateOfYear));
			  model.addAttribute("endDateOfYear", DateUtil.formatDefaultDate(endDateOfYear));
			 
			  //上月的开始日期和结束日期，为了查询上月月度考核排名前10的员工
			  Date lastMonth=DateUtil.addMonths(new Date(), -1);
			  int yearOfLastMonth=DateUtil.getYear(lastMonth);
			  int monthOfLastMonth=DateUtil.getMonth(lastMonth); 
			  model.addAttribute("startDateOfLastMonth", DateUtil.formatDefaultDate(DateUtil.getFirstDayOfMonth(yearOfLastMonth, monthOfLastMonth)));
			  model.addAttribute("endDateOfLastMonth", DateUtil.formatDefaultDate(DateUtil.getLastDayOfMonth(yearOfLastMonth, monthOfLastMonth)));
			
			  
			  //半年考核和年度考核需要部门信息
			  List<Dept> depts=userService.selectDistinctSecondLevelDept();
			  model.addAttribute("depts", depts);
			  
		}
		return location;
	}
	

}
