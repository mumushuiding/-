package com.crm.springboot.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.xml.bind.JAXBException;

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
import com.crm.springboot.factory.FactoryForIProcessPoJoFactory;
import com.crm.springboot.factory.IFactoryForIProcessPojoFactory;
import com.crm.springboot.pojos.Dictionary;
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessEntity;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.MonthAssessCfgPojo;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;
import com.crm.springboot.pojos.process.AbstractProcessPojo;
import com.crm.springboot.pojos.process.GroupManagerForProcess;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;

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
    				"res".equals(location1)||
    				"others".equals(location1)){//日常工作一线干部考核情况督查登记表
    			
    			List<Dept> depts=userService.selectDistinctSecondLevelDept();
    			model.addAttribute("depts", depts);
    			model.addAttribute("taskType", location1);
    			
    			List<String> assessments=dictionaryService.selectAllDictionaryWithName("基本定格");
    			model.addAttribute("assessments", assessments);
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
        //显示历史排名数据
		if ("unsubmittedList".equals(location)) {
			
			model.addAttribute("taskType", location1);
			List<Dept> depts=userService.selectDistinctSecondLevelDept();
			model.addAttribute("depts", depts);

			
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
	public String locate2(@RequestParam(required=false) String processInstanceId,@PathVariable String location,@PathVariable String location1,@PathVariable String location2,@PathVariable String location3,Model model,HttpServletRequest request,HttpSession session,
			@RequestParam(required=false) String userId) throws UnsupportedEncodingException{
		String referer=request.getHeader("referer");
		session.setAttribute("referer", referer);
		if(referer.indexOf("?msg=")!=-1) referer=referer.substring(0, referer.indexOf("?msg="));
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
				if(!responsibilityService.canIDeleteProject(location3)) return "redirect:"+referer+"?msg="+URLEncoder.encode("项目为系统导入或项目已经被删除，无法执行该操作","utf-8");
				project=responsibilityService.selectProjectWithProjectId(location3);
				action="/responsibility/updateProject";
			}else {
				project=new Project();
				project.setUserId(userId);
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
//		User user=(User) session.getAttribute("sysuser");
//		if(user==null) return "redirect:/user/loginForm";
//		project.setUserId(String.valueOf(user.getId()));
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
	   if(!responsibilityService.canIDeleteProject(projectId)) return "redirect:"+referer+"?msg="+URLEncoder.encode("项目为系统导入或项目已经被删除，无法执行该操作","utf-8");
	   responsibilityService.deleteProject(projectId);
	   return "redirect:"+referer;
   }
   @RequestMapping("/updateProject")
   public String updateProject(Project project,HttpSession session){
	   
	   responsibilityService.updateProject(project);
	   return "redirect:"+session.getAttribute("referer");
   }
   @RequestMapping("/selectProjectWithProjectId/{projectId}")
   @ResponseBody
   public String selectProjectWithProjectId(@PathVariable String projectId){
	   Project project=responsibilityService.selectProjectWithProjectId(projectId);
	   return JsonUtils.getGson().toJson(project);
   }
   @RequestMapping("/selectAllProject")
   @ResponseBody
   public String selectAllProject(Integer pageIndex,Integer pageSize,HttpSession session
		    ,@RequestParam(required=false) String startDate
			,@RequestParam(required=false) String endDate
			,@RequestParam(required=false) String userId
			,@RequestParam(required=false) Boolean markNeZero){
	   
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        markNeZero=true;
        if (markNeZero!=null&&markNeZero==true) {
        	params.put("markNeZero",markNeZero);
		}
        params.put("orderByClause","createTime desc");
        
        
		JsonUtils.startPageHelper(pageIndex, pageSize);

	    List<Project> projects=responsibilityService.selectAllProject(params);
        
	   
	    
	    return JsonUtils.formatListForPagination(projects, pageIndex, pageSize);
	    
	   
   }
   
   /**
    * //根据前N个月项目内容纪录，自动生成重复出现的项目
    * @param request
    * @param historicalNumbers
    * @return
 * @throws UnsupportedEncodingException 
    */
   @RequestMapping("/autoGenerateProject/{businessType}/{repeatTimes}")
   public String autoGenerateProject(HttpServletRequest request,
		   HttpSession session,
		   @PathVariable Integer repeatTimes,
		   @PathVariable String businessType) throws UnsupportedEncodingException{
	   User user=(User) session.getAttribute("sysuser");

	   String referer=request.getHeader("referer");

        if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));
	   //查询前N个月重复出现的项目
       int month=DateUtil.getMonth();

       int year=DateUtil.getYear();
     
	   if (RegexUtils.isMatch(businessType, ProcessType.EVALUCATION_MONTH_PATTER)) {
		   year=Integer.valueOf(RegexUtils.getIndexGroup(1, businessType, ProcessType.EVALUCATION_MONTH_PATTER));
		   month=Integer.valueOf(RegexUtils.getIndexGroup(2, businessType, ProcessType.EVALUCATION_MONTH_PATTER));
		   
	   }

       Date startDate=DateUtil.getFirstDayOfMonth(year, month-repeatTimes);
       Date endDate=DateUtil.getLastDayOfMonth(year, month-1);
	   
	   String table="select distinct projectContent,progress,count(projectContent) as repeatTimes from res_project where projectContent!='系统导入' and startDate>='"+DateUtil.formatDefaultDate(startDate)+"' and endDate<='"+DateUtil.formatDefaultDate(endDate)+"' and userId='"+user.getId()+"' group by projectContent";
	   HashMap<String, Object> params=new HashMap<String, Object>();
	   params.put("columns","projectContent,progress");
	   params.put("table", table);
	   params.put("repeatTimes", repeatTimes);
	   
	   List<Project> projects=responsibilityService.selectProject(params);
       if (projects==null||projects.size()==0) {
    	
    	   return "redirect:"+referer+"?msg="+URLEncoder.encode("无常用项目，添加失败", "utf-8");
	   }
	   //生成项目
	   Date start=DateUtil.getFirstDayOfMonth(year, month);
	   Date end=DateUtil.parseDefaultDate(DateUtil.formatDefaultDate(DateUtil.getLastDayOfMonth(year, month)));
	   String userId=String.valueOf(user.getId());
	   for (Project project : projects) {
		   project.setEndDate(end);
		   project.setStartDate(start);
		   project.setUserId(userId);
		   project.setCompleted("0");
		   responsibilityService.saveProject(project);
	   }
	  return "redirect:"+referer;
	  
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
  @RequestMapping("/selectMark/{markId}")
  @ResponseBody
  public String selectMarkWithMarkId(@PathVariable String markId){
	  Mark mark=responsibilityService.selectMarkByMarkId(markId);
	  return JsonUtils.getGson().toJson(mark);
  }
  @RequestMapping("/selectProjectWithProjectId/{id}/{processInstanceId}")
  public String selectMarkWithProjectId(@RequestParam(required=false) String msg,@PathVariable String id,@PathVariable String processInstanceId,Model model,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException{
      String referer=request.getHeader("referer");
      if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));
      //判断是否是系统导入的
      if(!responsibilityService.canIDeleteProject(id)) return "redirect:"+referer+"?msg="+URLEncoder.encode("项目为系统导入或项目已经被删除，无法执行该操作","utf-8");
      model.addAttribute("processInstanceId", processInstanceId);

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
  @RequestMapping("/selectAllEvaluation/{taskType}")
  @ResponseBody
  
  public String selectAllEvaluation(@PathVariable String taskType,
		  Model model,
		  Integer pageSize,
		  Integer pageIndex,
		  String secondDeptId,
		  String times,
		  String assessGroup,
		  HttpSession session,
		  String username){//指示时间，eg:2018年上半年,2018年
	  String businessType=responsibilityService.getBusinessType(taskType);
	  String titleLike=responsibilityService.getTitleLikeForYear(taskType, times);
	//获取考核组下的所有二级部门和一级部门id,并将它们以字符串数组形式存入哈希表
      HashMap<String, Object> params=sysPowerService.getDepartmentsWithGroupname(assessGroup, "考核组");
	  if(params==null) params=new HashMap<String, Object>();
	  params.put("businessType", businessType);
	  params.put("titleLike", titleLike);

	  User user=(User) session.getAttribute("sysuser");
	  //查询就否有超过90%的流程已经结束并且用户不包含考核组，是就查询，否则就返回空
	  //System.out.println("任务是否超过0.9："+responsibilityService.isOverRate(params, 0.9)+",是否包含考核组："+userService.isContainsGroupLike(user, "考核组"));
	  boolean isAssessMember=userService.isContainsGroupLike(user, "考核");//是否考核组和考核办成员 
	  if(!responsibilityService.isOverRate(params, 0.9)&&!isAssessMember) {
		  
          return  "{\"total\":0,\"page\":1,\"pageSize\":10,\"rows\":[]}";
	  }
	  if(!isAssessMember) params.put("result", "优秀");//非考核组成员只能看到优秀的
	 
	  //根据二级部门查询
	  params.put("secondDeptId", secondDeptId);
	  JsonUtils.startPageHelper(pageIndex, pageSize);
	  //List<ProcessEntity> processEntities=processService.selectAllEvaluationFromProcess(params);
	  List<ProcessEntity> processEntities=responsibilityService.selectEvaluationWithUser(params);
	 
	  
	  return JsonUtils.formatListForPagination(processEntities, pageIndex, pageSize);
	  
  }
 
  /**
   * 根据流程类型和流程id,查询考核表
   * @param msg
   * @param processInstanceId
   * @param model
   * @param taskType
   * @param session
   * @return
   * @throws UnsupportedEncodingException
   */
  @RequestMapping("/selectEvaluation/{processInstanceId}/{taskType}")
  public String selectEvaluation(@RequestParam(required=false) String msg,@PathVariable String processInstanceId,Model model,@PathVariable String taskType,HttpSession session) throws UnsupportedEncodingException{
	  User user=(User) session.getAttribute("sysuser");
	  
	  HashMap<String, Object> params=new HashMap<String, Object>();
	  params.put("processInstanceId", processInstanceId);
	  
	  //月度考核
	  if("month".equals(taskType)){
		  Evaluation evaluation=responsibilityService.selectSingleEvaluation(params);

		  //获取当月所有项目
		  HashMap<String, Object> params2 = new HashMap<String,Object>();
		  params2.put("userId",evaluation.getProcessBean().getUser().getId());
		  params2.put("startDate",DateUtil.formatTimesTampDate(evaluation.getStartDate()));
		  params2.put("endDate", DateUtil.formatTimesTampDate(evaluation.getEndDate()));
		  List<Project> projects=responsibilityService.selectAllProject(params2);
		  model.addAttribute("projects", projects);
		 
		  //计算总分
		  Double totalMark =responsibilityService.getTotalMarkOfProjects(projects);
		  evaluation.setTotalMark(String.valueOf(totalMark));

		  model.addAttribute("evaluation", evaluation);
		  
		  //获取当前候选组，传递给页面，使对应的内容可以编辑
		  String identity=responsibilityService.getIdentity(evaluation, user);
		  model.addAttribute("identity", identity);
	  }
	  //半年考核
	  if("halfYear".equals(taskType)||"fullYear".equals(taskType)){
		
		  Evaluation evaluation=responsibilityService.selectSingleEvaluation(params);
		 
		//基本定格
		 List<Dictionary> remarks=dictionaryService.selectAllDics("基本定格", "基本定格");
		 model.addAttribute("remarks", remarks);
		//基本定格对应得分
		 List<Dictionary> markOfAssessments=dictionaryService.selectAllDics("基本定格对应得分");
		 model.addAttribute("markOfAssessments", markOfAssessments);
      
		 //计算总分
		 Double totalMark =responsibilityService.selectTotalMark(evaluation.getProcessBean().getUser().getId(), 
				  "1", DateUtil.formatTimesTampDate(evaluation.getStartDate()),
				  DateUtil.formatTimesTampDate(evaluation.getEndDate()));
		 totalMark=totalMark==null?0.0:totalMark;
          Double baseMark=responsibilityService.getBaseMarkOfAssessment();
		  model.addAttribute("total", totalMark+baseMark);
		  
		//获取当前候选组，传递给页面，使对应的内容可以编辑
		  String identity=responsibilityService.getIdentity(evaluation, user);
		  model.addAttribute("identity", identity);
		  
		  
		  model.addAttribute("evaluation", evaluation);
	  }
	  
      //责任清单
	  if("res".equals(taskType)){
		  Responsibility responsibility=responsibilityService.selectSingleResponsibility(params);
		  model.addAttribute("evaluation", responsibility);
		//获取当前候选组，传递给页面，使对应的内容可以编辑
		  String identity=responsibilityService.getIdentity(responsibility.getProcessBean(), user);
		  model.addAttribute("identity", identity);
		 //查询所有不同的二级部门
		 List<Dept> depts=userService.selectDistinctSecondLevelDept();
		 model.addAttribute("depts", depts);
	  }
	  model.addAttribute("processInstanceId", processInstanceId);
	  
	  if("all".equals(taskType)){
		  return "/system/activiti/EvaluationProcess/startmonth";
	  }
		if(msg!=null && !"".equals(msg)) {
			model.addAttribute("msg", URLDecoder.decode(msg, "UTF-8"));
		};
	  
	  return "/system/activiti/EvaluationProcess/start"+taskType;
  }
//***************************************************Mark***************************************** 
  @RequestMapping("/selectAllMark")
  @ResponseBody 
  public String selectAllMark(
		     @RequestParam(required=false) String startDate,
			 @RequestParam(required=false) String endDate,
			 @RequestParam(required=false) String userId,
			 @RequestParam(required=false) String checked,
			 @RequestParam(required=false) String limit){
	  HashMap<String, Object> params=new HashMap<String, Object>();
//	  params.put("startDate", DateUtil.formatDefaultDate(DateUtil.parseEnglishDate(startDate)));
//	  params.put("endDate", DateUtil.formatDefaultDate(DateUtil.parseEnglishDate(endDate)));
	  params.put("startDate", startDate);
	  params.put("endDate",endDate);
	  params.put("userId", userId);
	  params.put("checked", checked);
	  params.put("orderByClause", "createTime desc");
	  if(limit!=null) params.put("limitClause", limit);
	  List<Mark> marks=responsibilityService.selectAllMarkAsType(params);
	
	  return JsonUtils.getGson().toJson(marks);
	  
  }
  
  @RequestMapping("/selectTotalMark")
  @ResponseBody
  public String selectTotalMark(HttpSession session
		    ,@RequestParam(required=false) String processInstanceId
		    ,@RequestParam(required=false) String startDate
			,@RequestParam(required=false) String endDate
			,@RequestParam(required=false) String userId
			,@RequestParam(required=false) String checked){

	  HashMap<String,Object> params=new HashMap<String, Object>();
	  params.put("userId", userId);
	  params.put("processInstanceId", processInstanceId);
	  Double totalMark=responsibilityService.selectTotalMark(params);
	  return String.valueOf(totalMark);
	  
  }
  
  
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
  public String slectTotalMark1(@PathVariable String group,@PathVariable String postNameLike,@PathVariable String total,Model model){
      //获取考核组下的所有二级部门和一级部门id,并将它们以字符串数组形式存入哈希表
      HashMap<String, Object> params=sysPowerService.getDepartmentsWithGroupname(group, "考核组");
      //
      Date date=new Date();
     
	  params.put("startDate", DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(date)));
	  params.put("endDate",DateUtil.formatDefaultDate(date));
	  
	  if("项目舞台负责人".equals(postNameLike)){
		  params.put("xmwt", group.split(","));
		  System.out.println("*****************xmwt="+group.split(","));
	  }else {
		  params.put("postNameLike", postNameLike);
	  }
	  
	  params.put("limitClause", "0,"+total);
	  params.put("checked", "1");
	  // List<Mark> marks=responsibilityService.selectTotalMarkAndUser(params);
	  System.out.println("*******************总分**********");
	  List<Mark> marks=responsibilityService.selectTotalMarkWithAllUser(params);
	  if(marks.size()==0){
		  date=DateUtil.addYears(date, -1);
		
		  params.put("startDate", DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(date)));
		  params.put("endDate",DateUtil.formatDefaultDate(DateUtil.getLastDayOfYear(date)));
		  //marks=responsibilityService.selectTotalMarkAndUser(params);
		  marks=responsibilityService.selectTotalMarkWithAllUser(params);
	  }
	 
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
		  Integer pageIndex,
		  @RequestParam(required=false) String total){
	  
	  model.addAttribute("taskType", taskType);

	  //时间段
	  Date date=new Date();
	
	  startDate=startDate==null?DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(date)):startDate;
	  endDate=endDate==null?DateUtil.formatDefaultDate(DateUtil.getLastDayOfYear(date)):endDate;
      
	  //获取考核组下的所有二级部门和一级部门id,并将它们以字符串数组形式存入哈希表
	  HashMap<String, Object> params=null;
	 
	  if (groups!=null&&!"".equals(groups)) {
		  params=sysPowerService.getDepartmentsWithGroupname(groups, "考核组");
	  }else {
		  params=new HashMap<String, Object>();
	 }
      
      params.put("startDate", startDate);
	  params.put("endDate",endDate);
	  if("项目舞台负责人".equals(posts)){
		 params.put("xmwt", groups.split(","));
	  }else {
		 if(posts!=null && posts!="")params.put("postNames", posts.split(","));
	  }

	 
	  params.put("checked", "1");
	  List<Mark> marks=null;
	  
	  
	  if(total!=null&&!"".equals(total)){
		  params.put("limitClause", "0,"+total);
		  marks=responsibilityService.selectTotalMarkAndUser(params);
		  
		  
		  return JsonUtils.getGson().toJson(marks);
	  }else {
		//分页查询
		  JsonUtils.startPageHelper(pageIndex, pageSize);
	      
		  //marks=responsibilityService.selectTotalMarkAndUser(params);
		  marks=responsibilityService.selectTotalMarkWithAllUser(params);
		  
		  return JsonUtils.formatListForPagination(marks, pageIndex, pageSize);
	  }
	  
	  
	 
  }
  
  @RequestMapping("/selectAllUsersWhoUnsubmittedProcess/{taskType}")
  @ResponseBody
  public String selectAllUsersWhoUnsubmittedProcess(@PathVariable String taskType,Model model,
		  @RequestParam(required=false) String total,
		  Integer pageIndex,
		  Integer pageSize,
		  String first,
		  @RequestParam(required=false) String second) throws JAXBException{
	       if("month".equals(taskType)){
		   
	        String titleLike=DateUtil.formatDateByFormat(DateUtil.addMonths(new Date(),-1), "yyyy年M月份")+"-月度考核";
	        String postNames=dictionaryService.selectSingleValueOfDic("考核对象", "月度考核");
	       
	        HashMap<String, Object> params=new HashMap<String, Object>();
	        params.put("titleLike", titleLike);
	        params.put("postNames",postNames.split(","));
	        List<User> users=null;
	        //total不为空，为首页查询前30条
	        if(total!=null){
	        	params.put("limitClause","0,"+total);
	        	users=processService.selectAllUsersWhoUnsubmittedProcess(params);
	            String degreeOfUrgency=responsibilityService.getDegreeOfUrgencyWithUnsubmittedAssessForm();
	            if(users==null) return null;
	   		    StringBuffer stringBuffer=new StringBuffer("{}");
	   		    String titleLike1="";

	   		    if("month".equals(taskType)) titleLike1="月度考核未交清单";
	   		 
	   		    stringBuffer.insert(1,"\"titleLike\":\""+titleLike1+"\",\"degreeOfUrgency\":\""+degreeOfUrgency+"\",\"users\":"+JsonUtils.getGson().toJson(users));
	   	      
	   		   return JsonUtils.getGson().toJson(stringBuffer);
	        }
	        
	        JsonUtils.startPageHelper(pageIndex, pageSize);
	        //一级部门
	        if(first!=null && first!="")params.put("firstLevelDeptIds", first.split(","));
	       
	        params.put("secondLevelId", second);
	        users=processService.selectAllUsersWhoUnsubmittedProcess(params);
	        
	        if(users==null) return null;
	      
	        return JsonUtils.formatListForPagination(users, pageIndex, pageSize);

		 
	  }
	return null;
  }
}

	
	
	
	
	