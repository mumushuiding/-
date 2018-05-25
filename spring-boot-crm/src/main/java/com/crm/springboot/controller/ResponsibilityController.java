package com.crm.springboot.controller;
import static org.assertj.core.api.Assertions.useRepresentation;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.hibernate.loader.custom.ScalarResultColumnProcessor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.crm.springboot.config.SchedulingConfig;
import com.crm.springboot.pojos.Dictionary;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.DictionaryService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.EncodingTool;
import com.crm.springboot.utils.JsonUtils;
import com.crm.springboot.utils.RegexUtils;
import com.github.pagehelper.PageHelper;

import javassist.compiler.ast.NewExpr;
import net.sf.jsqlparser.statement.select.Select;

@Controller
@RequestMapping("/responsibility")
public class ResponsibilityController {
	
	private static final Log log=LogFactory.getLog(ResponsibilityController.class);
	@Autowired
	private ResponsibilityService responsibilityService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private SysPowerService sysPowerService;
	@Autowired
	private ActivitiService activitiService;
	@Autowired
	private UserService userService;
	@RequestMapping("/{location}")
	public String location(@PathVariable String location,Model model,
			@RequestParam(required=false) String startDate,
			@RequestParam(required=false) String endDate,
			@RequestParam(required=false) String userId,
			HttpServletRequest request){
		if("projectList".equals(location)){
			
			
			
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
			model.addAttribute("userId", userId);
		
		}

		return "/user/responsibility/"+location;
	}
	@RequestMapping("/{location}/{location1}")
	public String location1(@PathVariable String location,@PathVariable String location1,Model model,HttpServletRequest request,HttpSession session,@RequestParam(required=false) String msg,
			@RequestParam(required=false) String group,
			@RequestParam(required=false) String postNameLike) throws UnsupportedEncodingException{
		if(msg!=null && !"".equals(msg)) {
			model.addAttribute("msg", URLDecoder.decode(msg, "UTF-8"));
		};
		//考核表list
        if("assessmentList".equals(location)){
    		if("month".equals(location1)||//月度考核
    				"halfYear".equals(location1)||//半年度考核
    				"fullYear".equals(location1)||//全年考核
    				"all".equals(location1)||//全部考核
    				"res".equals(location1)){//日常工作一线干部考核情况督查登记表
    			
    			
    			model.addAttribute("taskType", location1);
    			
    			return "/user/responsibility/"+location;
    		}
        }
        //首页显示排名
        if("home".equals(location)){
        	if("months".equals(location1)||//月度考核
    				"halfYear".equals(location1)||//半年度考核
    				"fullYear".equals(location1)//全年考核
    				){
        		      //日常工作一线干部考核情况督查登记表
        		
                model.addAttribute("taskType", location1);
    			
    			return "/"+location;
        	}
        
        }
        //显示历史排名数据
		if ("rankList".equals(location)) {
			
			model.addAttribute("taskType", location1);
			
			List<String> groups=dictionaryService.selectDistinctNameWithType("考核组");
			model.addAttribute("startDate",DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(new Date())));
			model.addAttribute("endDate", DateUtil.formatDefaultDate(new Date()));
			model.addAttribute("groups", groups);

			List<Post> posts=userService.selectAllPost();
			model.addAttribute("posts", posts);
			
			
			
			model.addAttribute("group",group);
			model.addAttribute("postNameLike", postNameLike);
			
			return "/user/responsibility/"+location;
			
		}
		
		 
		return "/user/responsibility/"+location+"/"+location1;
	}
	@RequestMapping("/{location}/{location1}/{location2}")
	public String location1(@PathVariable String location,@PathVariable String location1,@PathVariable String location2,Model model,HttpServletRequest request,HttpSession session){
		
		if("project".equals(location)){
			if("projectContent".equals(location1)){
				
			}
		}

		return "/user/responsibility/"+location+"/"+location1;
	}
	@RequestMapping("/{location}/{location1}/{location2}/{location3}")
	public String locate2(@RequestParam(required=false) String processInstanceId,@PathVariable String location,@PathVariable String location1,@PathVariable String location2,@PathVariable String location3,Model model,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException{
		String referer=request.getHeader("referer");
		session.setAttribute("referer", referer);
		
		User user=(User) session.getAttribute("sysuser");
		//如果申请已经提交的话，就跳转
//		String redirect=processService.isUserHasTheCandidateGroup(String.valueOf(user.getId()), processInstanceId, referer);
//		if(redirect!=null){
//			return redirect;
//		}

		if("projectForm".equals(location1)){
			
			Project project=null;
			String action="";
			if ("update".equals(location2)) {
				project=responsibilityService.selectProjectWithProjectId(location3);
				action="/responsibility/updateProject";
			}else {
				project=new Project();
				project.setStartDate(DateUtil.parseWesternDate2(location2));
				project.setEndDate(DateUtil.parseWesternDate2(location3));
				action="/responsibility/saveProject";
			}
			model.addAttribute("project", project);
			model.addAttribute("action", action);
		}
		return "/user/responsibility/"+location+"/"+location1;
	}
	@RequestMapping("/{location}/{location1}/{location2}/{location3}/{location4}")
	public String locate3(@PathVariable String location,@PathVariable String location1,@PathVariable String location2,@PathVariable String location3,@PathVariable String location4,Model model,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException{
		String referer=request.getHeader("referer");
		if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));
		session.setAttribute("referer", referer);
		User user=(User) session.getAttribute("sysuser");
		//location4为processInstanceId
		if ("markForm".equals(location1)) {
			List<String> accordingList=dictionaryService.selectAllDictionaryWithName("评分依据");
		    model.addAttribute("accordingList",accordingList);
			Mark mark=null;
			//如果申请已经提交的话，就跳转
			String redirect=processService.isUserHasTheCandidateGroup(String.valueOf(user.getId()), location4, referer);
			if(redirect!=null){
				return redirect;
			}
		    if("add".equals(location2)){
				
				mark=new Mark();
				mark.setProjectId(Integer.valueOf(location3));
				model.addAttribute("action", "/responsibility/saveMark");
		    }
			if("update".equals(location2)){
				
				
				mark=responsibilityService.selectMarkByMarkId(location3);
				model.addAttribute("action", "/responsibility/updateMark");
			}
			model.addAttribute("mark",mark);
			
		}

		return "/user/responsibility/"+location+"/"+location1;
	}
	//***************************************************用户项目 project*****************************************
	@RequestMapping("/saveProject")
	public String saveProject(Project project,HttpSession session){
		User user=(User) session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		project.setUserId(String.valueOf(user.getId()));
		responsibilityService.saveProject(project);
		return "redirect:"+session.getAttribute("referer");
	}
   @RequestMapping("/deleteProject/{projectId}")
   public String deleteProject(Model model,@RequestParam(required=false)String msg,@RequestParam(required = false) String processInstanceId,@PathVariable String projectId,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException{
	   String referer=request.getHeader("referer");
	   if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));
	   User user=(User) session.getAttribute("sysuser");
		//如果申请已经提交的话，就跳转
		String redirect=processService.isUserHasTheCandidateGroup(String.valueOf(user.getId()), processInstanceId, referer);
		if(redirect!=null){
			
			
			return redirect;
		}
		
	   responsibilityService.deleteProject(projectId);
	   return "redirect:"+referer;
   }
   @RequestMapping("/updateProject")
   public String updateProject(Project project,HttpSession session){
	   responsibilityService.updateProject(project);
	   return "redirect:"+session.getAttribute("referer");
   }
   @RequestMapping("/selectAllProject")
   @ResponseBody
   public String selectAllProject(Integer pageIndex,Integer pageSize,HttpSession session
		    ,@RequestParam(required=false) String startDate
			,@RequestParam(required=false) String endDate
			,@RequestParam(required=false) String userId){
	   
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        
        params.put("orderByClause","createTime desc");
        log.info("userId="+userId+",startDate="+startDate+",endDate="+endDate);
        
		JsonUtils.startPageHelper(pageIndex, pageSize);

	    List<Project> projects=responsibilityService.selectAllProject(params);
        
	   
	    
	    return JsonUtils.formatListForPagination(projects, pageIndex, pageSize);
	    
	   
   }
 //***************************************************用户自评 *****************************************	
  @RequestMapping("/saveMark")
  public String saveMark(HttpSession session,Mark mark){
	  
	  Project p=responsibilityService.selectProjectWithProjectId(String.valueOf(mark.getProjectId()));
	  
	  mark.setStartDate(p.getStartDate());
	  
	  mark.setEndDate(p.getEndDate());
	  
	  mark.setUserId(p.getUserId());
	  
	  responsibilityService.saveMark(mark);
	  
	  
	  Project project=new Project();
	  project.setSelfEvaluated("1");
	  project.setProjectId(mark.getProjectId());
	  responsibilityService.updateProject(project);
	  return "redirect:"+session.getAttribute("referer");
  }
  @RequestMapping("/deleteMark/{markId}")
  public String deleteMark(@RequestParam(required = false) String processInstanceId,@PathVariable String markId,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException{
	  String referer=request.getHeader("referer");
	  if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));
	   User user=(User) session.getAttribute("sysuser");
		//如果申请已经提交的话，就跳转
		String redirect=processService.isUserHasTheCandidateGroup(String.valueOf(user.getId()), processInstanceId, referer);
		if(redirect!=null){
			return redirect;
		}
	  responsibilityService.deleteMark(markId);
	  
	  return "redirect:"+referer;
  }
  @RequestMapping("/updateMark")
  public String updateMark(Mark mark,HttpSession session){
	  responsibilityService.updateMark(mark);
	  return "redirect:"+session.getAttribute("referer");
  }
  @RequestMapping("/selectProjectWithProjectId/{id}/{processInstanceId}")
  public String selectMarkWithProjectId(@RequestParam(required=false) String msg,@PathVariable String id,@PathVariable String processInstanceId,Model model,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException{
      String referer=request.getHeader("referer");
      if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));
     
      model.addAttribute("processInstanceId", processInstanceId);
     // System.out.println("*(******************referer="+referer);
      //evaluationHistory保存当前考核表跳转到查看项目之前的路径
      if( referer.contains("selectEvaluation")||referer.contains("perform")||referer.contains("toStartEvaluation")) session.setAttribute("evaluationHistory", referer);
      
	  Project project=responsibilityService.selectProjectWithProjectId(id);
	  
	 
	  model.addAttribute("project", project);
	  
	  if(msg!=null){
		  msg=URLDecoder.decode(msg, "UTF-8");
		  model.addAttribute("msg",msg);
	  }
	  
	  
	  return "/user/responsibility/project/projectContent";
  }

//***************************************************Evaluation*****************************************	
  @RequestMapping("/selectEvaluation/{processInstanceId}/{taskType}")
  public String selectEvaluation(@RequestParam(required=false) String msg,@PathVariable String processInstanceId,Model model,@PathVariable String taskType,HttpSession session) throws UnsupportedEncodingException{
	  User user=(User) session.getAttribute("sysuser");
	  
	  HashMap<String, Object> params=new HashMap<String, Object>();
	  params.put("processInstanceId", processInstanceId);
	  
	  //月度考核
	  if("month".equals(taskType)){
		  Evaluation evaluation=responsibilityService.selectSingleEvaluation(params);
		  
		  //获取当月所有项目并计算总分
		  HashMap<String, Object> params2 = new HashMap<String,Object>();
		  params2.put("userId",evaluation.getProcessBean().getUser().getId());
		  params2.put("startDate",DateUtil.formatTimesTampDate(evaluation.getStartDate()));
		  params2.put("endDate", DateUtil.formatTimesTampDate(evaluation.getEndDate()));
		  List<Project> projects=responsibilityService.selectAllProject(params2);
		  int totalMark =responsibilityService.getTotalMarkOfProjects(projects);
		  evaluation.setTotalMark(String.valueOf(totalMark));
		  //获取当前候选组，传递给页面，使对应的内容可以编辑
		  String identity=responsibilityService.getIdentity(evaluation, user);
		  model.addAttribute("identity", identity);
		
		  
		  model.addAttribute("projects", projects);
		  model.addAttribute("evaluation", evaluation);
	  }
      //责任清单
	  if("res".equals(taskType)){
		  Responsibility responsibility=responsibilityService.selectSingleResponsibility(params);
		  model.addAttribute("evaluation", responsibility);
		  
		 //查询所有不同的二级部门
		 List<Dept> depts=userService.selectDistinctSecondLevelDept();
		 model.addAttribute("depts", depts);
	  }
	  model.addAttribute("processInstanceId", processInstanceId);
	  
	  if("all".equals(taskType)){
		  return "/system/activiti/EvaluationProcess/startmonth";
	  }
	  if(msg!=null) model.addAttribute("msg", URLDecoder.decode(msg, "UTF-8"));
	  
	  return "/system/activiti/EvaluationProcess/start"+taskType;
  }
//***************************************************Mark***************************************** 

  
  @RequestMapping("/selectTotalMark/{taskType}")
  @ResponseBody
  public String selectTotalMark(@PathVariable String taskType,Integer pageIndex,Integer pageSize){
	  JsonUtils.startPageHelper(pageIndex, pageSize);
	  
	  if("months".equals(taskType)){
		  HashMap<String, Object> params=new HashMap<String, Object>();
		  params.put("startDate", DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(new Date())));
		  params.put("endDate",DateUtil.formatDefaultDate(new Date()));
		  List<Mark> marks=responsibilityService.selectTotalMarkAndUser(params);
		  
		  return JsonUtils.formatListForPagination(marks, pageIndex, pageSize);
	  }
	return taskType;
	  
  }
  @RequestMapping("/selectTotalMarkForEachGroup/{group}/{postNameLike}/{total}")
  @ResponseBody
  public String slectTotalMark1(@PathVariable String group,@PathVariable String postNameLike,@PathVariable String total){
      //获取考核组下的所有二级部门和一级部门id,并将它们以字符串数组形式存入哈希表
      HashMap<String, Object> params=sysPowerService.getDepartmentsWithGroupname(group, "考核组");

	  params.put("startDate", DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(new Date())));
	  params.put("endDate",DateUtil.formatDefaultDate(new Date()));
	  params.put("postNameLike", postNameLike);
	  params.put("limitClause", "0,"+total);
	  params.put("checked", "1");
	  List<Mark> marks=responsibilityService.selectTotalMarkAndUser(params);
	  return JsonUtils.getGson().toJson(marks);
  }
  
  @RequestMapping("/selectHistoryTotalMark/{taskType}")
  @ResponseBody
  public String selectHistoryTotalMark(Model model, @PathVariable String taskType,
		  String startDate,
		  String endDate,
		  String posts,
		  String groups,
		  Integer pageSize,
		  Integer pageIndex){
	  
	  model.addAttribute("taskType", taskType);

	  //时间段
	  startDate=startDate==null?DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(new Date())):startDate;
	  endDate=endDate==null?DateUtil.formatDefaultDate(new Date()):endDate;

	  //获取考核组下的所有二级部门和一级部门id,并将它们以字符串数组形式存入哈希表
      HashMap<String, Object> params=sysPowerService.getDepartmentsWithGroupname(groups, "考核组");
      params.put("startDate", startDate);
	  params.put("endDate",endDate);
	  if(posts!=null && posts!="")params.put("postNames", posts.split(","));
	  params.put("checked", "1");
	 
	  //分页查询
	  JsonUtils.startPageHelper(pageIndex, pageSize);
      
	  List<Mark> marks=responsibilityService.selectTotalMarkAndUser(params);
	  
	  return JsonUtils.formatListForPagination(marks, pageIndex, pageSize);
  }
  
  @RequestMapping("/selectAllUsersWhoUnsubmittedProcess/{taskType}")
  @ResponseBody
  public String selectAllUsersWhoUnsubmittedProcess(@PathVariable String taskType){
	  if("month".equals(taskType)){
		  List<User> users=new ArrayList<User>();
		  for (int i = 0; i < 20; i++) {
			 User user=new User();
			 user.setUsername("王丽红");
			 users.add(user);
			 
		  }
		  
		  return JsonUtils.getGson().toJson(users);
	  }
	return null;
  }
}

	
	
	
	
	