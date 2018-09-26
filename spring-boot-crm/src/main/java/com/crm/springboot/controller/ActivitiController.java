package com.crm.springboot.controller;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.crm.springboot.pojos.Dictionary;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;

import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.DeptType;
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
import com.crm.springboot.utils.JsonUtils;
import com.github.pagehelper.PageInfo;




@Controller
@SessionAttributes(value="sysuser")
@RequestMapping(value="/system/activiti")
public class ActivitiController {
	
	@Autowired
	private ResponsibilityService responsibilityService;
	@Autowired
	private ProcessService processService;
	@Autowired
	
	private ActivitiService activitiService;
    @Autowired
    private UserService userService;
	@Autowired
	private SysPowerService sysPowerService;
    @Autowired
    private DictionaryService dictionaryService;
	
//=========================location========================
    @RequestMapping("/{location}")
    public String loacate(@PathVariable String location){
    	return "system/activiti/"+location;
    }
    @RequestMapping("/{location}/{sub}")
    public String loacate1(@RequestParam(required=false) String msg,@PathVariable String location,@PathVariable String sub,Model model,HttpSession session,
    		HttpServletRequest request) throws UnsupportedEncodingException{
    	User user=(User) session.getAttribute("sysuser");
		//用户待办任务列表
		if("taskList".equals(location)){
			
			List<Post> posts=userService.selectAllPost();
			model.addAttribute("posts", posts);
			
			List<Dept> depts=userService.selectDistinctSecondLevelDept();
			model.addAttribute("depts", depts);
			
			
			if("all".equals(sub)){
				model.addAttribute("taskType", sub);
				return "system/activiti/allTaskList";
			}
			if(sub!=null){
				model.addAttribute("taskType", sub);
			}else {
				model.addAttribute("taskType", ProcessType.CANDIDATE);
			}
		
			if(msg!=null && !"".equals(msg)) {
				model.addAttribute("msg", URLDecoder.decode(msg, "UTF-8"));
			};
			
			return "system/activiti/"+location;
		}
		//用户申请任务列表 
		if("now".equals(sub)||"history".equals(sub)){
			model.addAttribute("taskType", sub);
			return "system/activiti/"+location;
		}
    	return "system/activiti/"+location+"/"+sub;
    }

   
	/**
	 * ********************************Group管理********************************
	 */
	@RequestMapping("/groupInfo")
	@ResponseBody
	public String getGroupInfo(Integer pageIndex,Integer pageSize){
		pageIndex=pageIndex==null?1:pageIndex;
		pageSize=pageSize==null?10:pageSize;
	    List<Group> gList=activitiService.selectGroups(pageIndex,pageSize);
		return JsonUtils.formatDataForPagination(
				JsonUtils.getGson().toJson(gList),
				activitiService.GroupsCount(), 
				pageIndex, pageSize);
	}
	/**
	 * ********************************流程存储与查询*************************************
	 */
	/**
	 * 部署文件查询
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/processDefInfo")
	@ResponseBody
	public String selectAllProcessDefinition(Integer pageIndex,Integer pageSize){
		pageIndex=pageIndex==null?1:pageIndex;
		pageSize=pageSize==null?10:pageSize;
		List<ProcessDefinition> pList=activitiService.selectAllProcessDefinition(pageIndex, pageSize);
		
		return JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pList), activitiService.processDefinitionCount(), pageIndex, pageSize);
	}
	/**
	 * 流程实例查询
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/listProcessInstance/{taskType}")
	@ResponseBody
	public String listProcessInstance(HttpSession session,Model model,
			@PathVariable String taskType,
			@RequestParam(required=false) String msg,
			Integer pageIndex,
			Integer pageSize,
			String title,
			String firstDept,
			String second,
			String assessment) throws UnsupportedEncodingException{
	
		if(msg!=null&&!"".equals(msg))model.addAttribute("msg",URLDecoder.decode("utf-8", msg));
		
		User user=(User) session.getAttribute("sysuser");
		model.addAttribute("taskType", taskType);
		JsonUtils.startPageHelper(pageIndex, pageSize);
		List<ProcessBean> processBeans=null;
		HashMap<String, Object> params = new HashMap<String,Object>();
		//查询组织考评为assessment的考核表
		params.put("assessment", assessment);
		
		params.put("title", title);
		params.put("userId", String.valueOf(user.getId()));
		//月度考核表
		if("month".equals(taskType)){
			params.put("businessType", ProcessType.EVALUCATION_MONTH);
		}
		//责任清单表
		if("res".equals(taskType)){
			params.put("businessType",ProcessType.RESPONSIBILITY_TYPE);
		}
		//查询所有
		if("all".equals(taskType)){
			params.remove("userId");
		}
		if("fullYear".equals(taskType)){
			params.put("businessType",ProcessType.EVALUCATION_FULLYEAR);
		}
		if("halfYear".equals(taskType)){
			params.put("businessType",ProcessType.EVALUCATION_HALFYEAR);
		}
		if(ProcessType.BUSINESSTYPE_OTHERS.equals(taskType)){
			params.put("userId",user.getId());
		}
		//一级部门
        if(firstDept!=null && firstDept!="")params.put("deptNames", firstDept.split(","));
        
		processBeans=processService.selectAllProcessWithUserNameAndPhone(params);
		//if(processBeans==null || processBeans.size()==0) return null;
       
		return JsonUtils.formatListForPagination(processBeans, pageIndex, pageSize);
	
	
		
	}
	@RequestMapping("/deleteProcessInstance/{id}")
	public String deleteProcessInstance(@PathVariable String id,HttpServletRequest request){
		String referer=request.getHeader("referer");
		if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));
		
		
	    this.activitiService.deleteProcessInstance(id, "废弃");
		
		
		
		return "redirect:"+referer;
	}
	
	@RequestMapping("/viewProcessImage")
	public void viewProcessImage(HttpServletResponse response,HttpServletRequest request){
		    String param=request.getParameter("param");
		    String[] params=param.split(",");
		
		    
            ServletOutputStream out = null;
			try {
				out = response.getOutputStream();
				activitiService.viewProcessImage(params[0], params[1],out);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
					try {
						if(out!=null) out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}	
	}

/**
 * *****************************任务******************************************
 */

	//删除已经启动的任务
	@RequestMapping("/delete/{taskId}")
	public String DeleteTask(@PathVariable String taskId,HttpServletRequest request){
		String referer=request.getHeader("referer");
		if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));
		activitiService.deleteTaskById(taskId);
		return "redirect:"+referer;
	}
	//查看待办的任务列表
		@RequestMapping("/listTask/{taskType}")
		@ResponseBody
		public String listTask(@PathVariable String taskType,HttpSession session,HttpServletRequest request,
				Integer pageIndex,Integer pageSize,
				String taskName,Model model,
				String posts,
				String firstDept,
				String totalMarkNeZero,
				@RequestParam(required=false) String second){
			User user=(User) session.getAttribute("sysuser");
			if(user==null) return "redirect:/user/loginForm";
			PageInfo pageInfo = null;

			model.addAttribute("taskType", taskType);
			
			if (second==null) {
				session.getAttribute("second");
			}else {
				session.setAttribute("second", second);
			}
			if ("".equals(posts)) {
				session.getAttribute("posts");
			}else {
				session.setAttribute("posts", posts);
			}
			if (firstDept==null) {
				session.getAttribute("firstDept");
			}else {
				session.setAttribute("firstDept", firstDept);
			}

            if (totalMarkNeZero==null) {
				session.getAttribute("totalMarkNeZero");
			}else {
				session.setAttribute("totalMarkNeZero", totalMarkNeZero);
			}
         
			//查询参数
			HashMap<String , Object> params=new HashMap<String, Object>();
			if(posts!=null && posts!="")params.put("postNames", posts.split(","));
			
			params.put("title",taskName);
			//检索未完结的流程
			params.put("completed","0");
			//总分不为0
			params.put("totalMarkNeZero", totalMarkNeZero);
			
			if (ProcessType.TASK_Leader.equals(taskType)) {
				
                List<String> candidateGroups=sysPowerService.getGroupsOfLeaderWithUserId(String.valueOf(user.getId()));
                
				if(candidateGroups==null||candidateGroups.size()==0) return null;
				params.put("candidateGroups",candidateGroups);
				String[] deptNames=userService.getDeptNames(user);
				if(deptNames==null)return null;
				params.put("deptNames",deptNames);
				if(firstDept!=null && firstDept!="")params.put("deptNames", firstDept.split(","));
				
				
				JsonUtils.startPageHelper(pageIndex, pageSize);
			    List<ProcessBean> processBeans=processService.selectProcessFromActRuTask(params);
			    
            	return JsonUtils.formatListForPagination(processBeans, pageIndex, pageSize);

			}
            if (ProcessType.TASK_ASSESSGROUP.equals(taskType)) {
                
            	List<String> candidateGroups=sysPowerService.getGroupsOfOvereerWithUserId(String.valueOf(user.getId()));
            	if(candidateGroups==null||candidateGroups.size()==0) return null;
            	params.put("candidateGroups",candidateGroups);
            	//一级部门
    	        if(firstDept!=null && firstDept!="")params.put("deptNames", firstDept.split(","));
    			
    			
    			JsonUtils.startPageHelper(pageIndex, pageSize);
			    List<ProcessBean> processBeans=processService.selectProcessFromActRuTask(params);
			    
            	return JsonUtils.formatListForPagination(processBeans, pageIndex, pageSize);
			}

			if(ProcessType.HISTORY_ASSIGNEE.equals(taskType)){
				    params.remove("completed");
				    if(firstDept!=null && firstDept!="")params.put("deptNames", firstDept.split(","));
				    params.put("assignee", user.getId());
				    JsonUtils.startPageHelper(pageIndex, pageSize);
				    List<ProcessBean> processBeans=processService.selectHistoryProcessFromActHiTaskInst(params);
				    
                	return JsonUtils.formatListForPagination(processBeans, pageIndex, pageSize);
        		
			}
            if (ProcessType.HISTORY_PASS.equals(taskType)) {
				pageInfo=activitiService.listAllHistoricTaskInstancesPageInfoByUserIdWithPassValue(String.valueOf(user.getId()), pageIndex, pageSize, "pass");
			}
            if (ProcessType.HISTORY_REJECT.equals(taskType)) {
            	pageInfo=activitiService.listAllHistoricTaskInstancesPageInfoByUserIdWithPassValue(String.valueOf(user.getId()), pageIndex, pageSize, "reject");
			}
			if(pageInfo==null) return null;
            return JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pageInfo.getList()), pageInfo.getTotal(), pageIndex, pageSize);
		}
/**
 * *****************************流程
 * @throws UnsupportedEncodingException ******************************************
 */
		//启动流程
		
		
        @RequestMapping("/preStart/{processType}/{taskType}")
        public String preStart(Model model,@PathVariable String processType,@PathVariable String taskType,HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException{
        	User user=(User)session.getAttribute("sysuser");
        	String referer=request.getHeader("referer");
    		if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));
  
			if(user==null)return "redirect:/user/loginForm";
			

			
			if("EvaluationProcess".equals(processType)){
				
			
			    ProcessBean processBean=new ProcessBean();
			    processBean.setBusinessKey(processType);

				List<String> taskTitle=null;
				List<String> types=null;
				
				String action="";
				if("".equals(user.getEmail())){
                    String msg="邮箱为空，先去 [个人中心 ] 填写邮箱";
					return "redirect:"+referer+"?msg="+URLEncoder.encode(msg,"utf-8");
				}
				if(user.getUserLinkDepts()==null||user.getUserLinkDepts().size()==0){
                    String msg="部门为空，先去 [个人中心 ] 修改部门，再填写";
					return "redirect:"+referer+"?msg="+URLEncoder.encode(msg,"utf-8");
				}
				if(user.getPosition()==null||user.getPosition().equals("")){
					String msg="职位为空，先去 [个人中心 ] 修改《职务》，再填写";
					
					return "redirect:"+referer+"?msg="+URLEncoder.encode(msg,"utf-8");
				}

				//月度考核表
				if("monthForm".equals(taskType)){
					//判断当前时间，是否已经过了交表时限
//					if(responsibilityService.isOverTheDeadLineForSubmittedMonthProcess()){
//						String msg1=URLEncoder.encode("已经过了交表的最后时限，无法再提交", "utf-8");
//						
//						return "redirect:/responsibility/assessmentList/month?msg="+msg1;
//					}
					//判断能否启动当前流程
		        	if(!responsibilityService.canIStartEvaluationProcess(user)){

		        		return "redirect:"+referer;
		        	}
					action="/system/activiti/toStartEvaluation/startmonth";
					types=responsibilityService.selectEvaluationType(ProcessType.EVALUCATION_MONTH);
					taskTitle=responsibilityService.selectEvaluationBusinessType(ProcessType.EVALUCATION_MONTH);
					Evaluation evaluation=new Evaluation();
					evaluation.setProcessBean(processBean);
					model.addAttribute("evaluation", evaluation);

				}
				//半年考核表
				if("halfYearForm".equals(taskType)){
					action="/system/activiti/toStartEvaluation/starthalfYear";
					types=responsibilityService.selectEvaluationType(ProcessType.EVALUCATION_HALFYEAR);
					taskTitle=responsibilityService.selectEvaluationBusinessType(ProcessType.EVALUCATION_HALFYEAR);
					Evaluation evaluation=new Evaluation();
					evaluation.setProcessBean(processBean);
					model.addAttribute("evaluation", evaluation);
				}
				//全年考核表
				if ("fullYearForm".equals(taskType)) {
					action="/system/activiti/toStartEvaluation/startfullYear";
					types=responsibilityService.selectEvaluationType(ProcessType.EVALUCATION_FULLYEAR);
					taskTitle=responsibilityService.selectEvaluationBusinessType(ProcessType.EVALUCATION_FULLYEAR);
					Evaluation evaluation=new Evaluation();
					evaluation.setProcessBean(processBean);
					model.addAttribute("evaluation", evaluation);
				}
				if("resForm".equals(taskType)){
					
					action="/system/activiti/toStartResponsibility/startres";
					types=responsibilityService.selectEvaluationType(ProcessType.RESPONSIBILITY_TYPE);
					taskTitle=responsibilityService.selectEvaluationBusinessType(ProcessType.RESPONSIBILITY_TYPE);
					Responsibility responsibility=new Responsibility();
					responsibility.setProcessBean(processBean);
					
					model.addAttribute("evaluation", responsibility);
					
					
					
				}
				
				model.addAttribute("action", action);
				
				model.addAttribute("types", types);
				
				model.addAttribute("taskTitle",taskTitle);
				
			    
				
				return "/system/activiti/"+processType+"/monthForm";
			}
			return null;
        }
/*****************************************责任清单***************************************************************
 */
        @RequestMapping("/toStartResponsibility/{taskType}")
        public String toStartResponsibility(@RequestParam(required=false)String msg,@PathVariable String taskType,Model model,Responsibility responsibility,HttpSession session){
        	User user=(User)session.getAttribute("sysuser");
			if(user==null)return "redirect:/user/loginForm";
			
			ProcessBean processBean=responsibility.getProcessBean();

			
			//获取流程部署ID
			String deploymentId=activitiService.getDeploymentIdByBusinessKey(responsibility.getProcessBean().getBusinessKey());
			processBean.setDeploymentId(deploymentId);
			processBean.setUser(user);
			
			//将开始时间startDate和endDate放入params
			
			HashMap<String, Object> params =responsibilityService.getStartDateStrAndEndDateDateStr(responsibility.getSparation());
			responsibility.setOldPosition(user.getPosition());
			responsibility.setStartDate(DateUtil.parseDefaultDate(String.valueOf(params.get("startDate"))));
			responsibility.setEndDate(DateUtil.parseDefaultDate(String.valueOf(params.get("endDate"))));

			Date createTime=new Date();
			responsibility.setProcessBean(processBean);
			responsibility.setCreateTime(createTime);
			model.addAttribute("evaluation", responsibility);
			
			//查询所有不同的二级部门
			List<Dept> depts=userService.selectDistinctSecondLevelDept();
			model.addAttribute("depts", depts);
			//获取当前候选组，传递给页面，使对应的内容可以编辑
			
			 model.addAttribute("identity", "employee");
			
			return "/system/activiti/"+processBean.getBusinessKey()+"/"+taskType;
        }
        @RequestMapping("/startResponsibility/{actionType}")
        public String startResponsibility(Model model,HttpSession session,@PathVariable String actionType,Responsibility responsibility){
    		User user=(User)session.getAttribute("sysuser");
    		if(user==null)return "redirect:/user/loginForm";
    		ProcessBean processBean=responsibility.getProcessBean();
            
    		//设置变量
    		HashMap<String, Object> variables=new HashMap<String, Object>();
			//调整日期
			variables.put("ajustDate",responsibility.getAjustDate());
			//调整的部门
			UserLinkDept userLinkDept=new UserLinkDept();
			userLinkDept.setUserId(user.getId());
			userLinkDept.setFirstLevelIds(responsibility.getFirstLevelIds());
			Dept dept=new Dept();
			
			if(responsibility.getSecondLevelId()!=null&&responsibility.getSecondLevelId().trim()!="")dept.setDid(Integer.parseInt(responsibility.getSecondLevelId()));
			userLinkDept.setSecondLevel(dept);
			
			Dept dept2=new Dept();
			dept2.setDid(100);
			userLinkDept.setThirdLevel(dept2);
			variables.put("userLinkDept",userLinkDept);
			//调整的职位
			variables.put("position",responsibility.getPosition());
			
			
    		ProcessInstance pInstance=activitiService.startProcess(processBean.getBusinessKey(), processBean.getDeploymentId());
    		//查询第一任务
			Task task=this.activitiService.getFirstTask(pInstance.getId());
			task.setAssignee(String.valueOf(user.getId()));
    		if(responsibility.getId()==null){
    			

    			processBean.setUser(user);
    			processBean.setTitle(user.getUsername()+"-"+responsibility.getSparation());
    			processBean.setBusinessType(ProcessType.RESPONSIBILITY_TYPE);
    			processBean.setRequestedDate(DateUtil.formatTimesTampDate(new Date()));
    			processBean.setProcessInstanceId(pInstance.getId());
    			
    			processBean.setExecutionId(task.getExecutionId());
    			
    			responsibility.setProcessBean(processBean);
    			responsibility.setCreateTime(new Date());
    			processService.save(processBean);
    			responsibilityService.saveResponsibility(responsibility);
    			//设置任务代理人
    			this.activitiService.setAssignee(task.getId(), String.valueOf(user.getId()));
    			//任务参数初始化
    			
    			variables.put("arg", processBean);
    			//将部门信息存入
    			UserLinkDept dept1=userService.getSingleUserLinkDept(user, responsibility.getProcessBean().getDeptName());
    			variables.put("dept",dept1);
    			
    			activitiService.setVariable(task, variables);
    		}else {
    			
    			activitiService.setVariable(task, variables);
    			responsibilityService.updateResponsibility(responsibility);
    		}
    		if("complete".equals(actionType)){
    			
    			
    			this.activitiService.setAssignee(task.getId(), String.valueOf(user.getId()));
    			activitiService.setVariable(task, variables);
    			processBean.setCommitted("1");
    			processService.updateProcess(processBean);
    			responsibilityService.updateResponsibility(responsibility);
    			this.activitiService.complete(this.activitiService.getFirstTask(responsibility.getProcessBean().getProcessInstanceId()).getId());
    		}

    		
    		return "redirect:/responsibility/assessmentList/res";
        }
        
        
      

/*****************************************月度考核或者年度考核
 * @throws JAXBException ***************************************************************
 */
		@RequestMapping("/toStartEvaluation/{taskType}")
		public String toStartEvaluation(@RequestParam(required=false)String msg,Model model,Evaluation evaluation,HttpServletRequest request,HttpSession session,@PathVariable String taskType) throws UnsupportedEncodingException, JAXBException{
			User user=(User)session.getAttribute("sysuser");
			if(user==null)return "redirect:/user/loginForm";
			
             String referer=request.getHeader("referer");

             if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));

			//判断当前用户是否已经提交过考核申请表，防止重复添加
			String title=user.getUsername()+"-"+evaluation.getSparation();
			if (user.getUsername().matches("[0-9]+")) {
				String msg1=URLEncoder.encode("您的姓名为【"+user.getUsername()+"】,这显然不正确，请至【个人中心】修改之后再填写", "utf-8");
				return "redirect:/responsibility/assessmentList/month?msg="+msg1;
			}
			if(processService.isProcessExists(String.valueOf(user.getId()), title)){
                String msg1=URLEncoder.encode("该考核表已经提交过了，请不要重复提交", "utf-8");
				return "redirect:/responsibility/assessmentList/month?msg="+msg1;
			}
			
			//将evaluation保存到session里面，忘记什么原因了
			if (evaluation.getProcessBean()==null) {
				evaluation=(Evaluation) session.getAttribute("evaluation");
			}
			session.setAttribute("evaluation", evaluation);
			
			
			ProcessBean processBean=evaluation.getProcessBean();
			Date createTime=new Date();
			String deploymentId=activitiService.getDeploymentIdByBusinessKey(evaluation.getProcessBean().getBusinessKey());
		
			//根据流程title名获取时间段，并将开始时间startDate和endDate放入params
			HashMap<String, Object> params =responsibilityService.getStartDateStrAndEndDateDateStr(evaluation.getSparation());
			evaluation.setStartDate(DateUtil.parseDefaultDate(String.valueOf(params.get("startDate"))));
			evaluation.setEndDate(DateUtil.parseDefaultDate(String.valueOf(params.get("endDate"))));
			
			
			evaluation.setCreateTime(createTime);
			processBean.setDeploymentId(deploymentId);
			processBean.setUser(user);
			evaluation.setProcessBean(processBean);

			if("startmonth".equals(taskType)){
				//查询相关项目
				params.put("userId",user.getId());
				List<Project> projects=responsibilityService.selectAllProject(params);
				
				//计算项目总分
			    Double totalMark =responsibilityService.getTotalMarkOfProjects(projects);
			    evaluation.setTotalMark(String.valueOf(totalMark));

				evaluation.setAttendance("旷工()天，请假()天");
				model.addAttribute("projects", projects);
				
			   
			}

			if("starthalfYear".equals(taskType)||"startfullYear".equals(taskType)){
				
				
				
				DeptType deptType=userService.selectSingleDeptType(evaluation.getProcessBean().getDeptName());
		
				if(deptType==null){
					String msg1=URLEncoder.encode("你的部门【"+evaluation.getProcessBean().getDeptName()+"】目前还未分类（采编经营，行政后勤等），无法填写申请，", "utf-8");
					
					return "redirect:/responsibility/assessmentList/"+taskType.substring(5)+"?msg="+msg1;
				}


			 //计算总分
				Double totalMark =responsibilityService.selectTotalMark(evaluation.getProcessBean().getUser().getId(), 
						  "1", DateUtil.formatTimesTampDate(evaluation.getStartDate()),
						  DateUtil.formatTimesTampDate(evaluation.getEndDate()));
				totalMark=totalMark==null?0.0:totalMark;
		        Double baseMark=responsibilityService.getBaseMarkOfAssessment();

				evaluation.setMarks(String.valueOf(totalMark+baseMark));
				
				taskType="starthalfYear";
			}

			if(msg!=null && !"".equals(msg)) {
				model.addAttribute("msg", URLDecoder.decode(msg, "UTF-8"));
			}
			model.addAttribute("evaluation", evaluation);
			 //获取当前候选组，传递给页面，使对应的内容可以编辑
			 String identity=responsibilityService.getIdentity(evaluation, user);
			 model.addAttribute("identity", identity);
			 
           
			return "/system/activiti/"+processBean.getBusinessKey()+"/"+taskType;
		}
	
	@RequestMapping("/startEvaluation/{actionType}")
	public String startEvaluation(Evaluation evaluation,Model model,HttpSession session,@PathVariable String actionType
			,HttpServletRequest request){
		User user=(User)session.getAttribute("sysuser");
		if(user==null)return "redirect:/user/loginForm";

		ProcessBean processBean=evaluation.getProcessBean();

		if(evaluation.geteId()==null){
			ProcessInstance pInstance=activitiService.startProcess(processBean.getBusinessKey(), processBean.getDeploymentId());
			processBean.setUser(user);
			processBean.setTitle(user.getUsername()+"-"+evaluation.getSparation());
			processBean.setBusinessType(responsibilityService.getBusinessType(evaluation.getStartDate(), evaluation.getEndDate()));
			processBean.setRequestedDate(DateUtil.formatTimesTampDate(new Date()));
			processBean.setProcessInstanceId(pInstance.getId());
			
			evaluation.setCreateTime(new Date());
			//查询第一任务
			Task task=this.activitiService.getFirstTask(pInstance.getId());
			task.setAssignee(String.valueOf(user.getId()));
			processBean.setExecutionId(task.getExecutionId());
			
			evaluation.setProcessBean(processBean);
			
			processService.save(processBean);
			
			responsibilityService.saveEvaluation(evaluation);
			//设置任务代理人
			this.activitiService.setAssignee(task.getId(), String.valueOf(user.getId()));
			//任务参数初始化
			HashMap<String, Object> variables=new HashMap<String, Object>();
			variables.put("arg", processBean);
			//将部门信息存入
			UserLinkDept dept1=userService.getSingleUserLinkDept(user, evaluation.getProcessBean().getDeptName());
			variables.put("dept",dept1);
			activitiService.setVariable(task, variables);
		}else {
			
			responsibilityService.updateEvaluation(evaluation);
		}
		
		if("complete".equals(actionType)){
			
			
			try {
				Task task=this.activitiService.getFirstTask(processBean.getProcessInstanceId());
				
				//必须要设置任务代理人 ，否则无法撤回
				this.activitiService.setAssignee(task.getId(), String.valueOf(user.getId()));
		
				this.activitiService.complete(this.activitiService.getFirstTask(evaluation.getProcessBean().getProcessInstanceId()).getId());

				//设置任务状态为1（已提交）
				processBean.setCommitted("1");
				processService.updateProcess(processBean);
				responsibilityService.updateEvaluation(evaluation);
			
			} catch (Exception e) {
				
//				processBean.setCommitted("0");
//				processService.updateProcess(processBean);
//				responsibilityService.updateEvaluation(evaluation);
				throw e;
			}
			
		}
        String referer=request.getHeader("referer");
        int index=referer.indexOf("?msg=");
        if(index!=-1) referer=referer.substring(0, index);
        

        if (referer.indexOf("start")!=-1) {
			return "redirect:/responsibility/assessmentList/"+referer.substring(referer.indexOf("start")+5);
		}
        
	 
		return "redirect:/responsibility/assessmentList/"+referer.split("/")[referer.split("/").length-1];
	}

	  

    @RequestMapping("/perform/{taskId}")
    public String perform(@PathVariable String taskId,Model model,
    		@RequestParam String buisinessType,
    		@RequestParam String processInstanceId,
    		HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException{
    	User user=(User)session.getAttribute("sysuser");

    	String referer=request.getHeader("referer");
    	if(referer.contains("taskList"))  session.setAttribute("taskList_referer", referer);

		
    	model.addAttribute("actionType", "perform");
    	ProcessInstance pInstance=activitiService.getProcessInstance(taskId);
    	model.addAttribute("candidateGroupList", activitiService.selectCandidateGroup(taskId));

        //考核审批流程
    	if(ProcessType.PROCESS_EVALUATION.equals(pInstance.getProcessDefinitionKey())){
    		HashMap<String, Object> params = new HashMap<String,Object>();
    		params.put("processInstanceId", pInstance.getId());
    		//半年考核和年度考核
    		if(ProcessType.EVALUCATION_HALFYEAR.equals(buisinessType)||ProcessType.EVALUCATION_FULLYEAR.equals(buisinessType)){
    			
    			//获取当前候选组，传递给页面，使对应的内容可以编辑
    			Evaluation evaluation=responsibilityService.selectSingleEvaluation(params);
    			
 			    String identity=responsibilityService.getIdentity(evaluation, user);
 			    model.addAttribute("identity", identity);
 			    
 			    model.addAttribute("evaluation", evaluation);
 			    
 			    System.out.println("******************全年考核");
			    
 			    //基本定格
				List<Dictionary> remarks=dictionaryService.selectAllDics("基本定格", "基本定格");
				model.addAttribute("remarks", remarks);
				//基本定格对应得分
				List<Dictionary> markOfAssessments=dictionaryService.selectAllDics("基本定格对应得分");
				model.addAttribute("markOfAssessments", markOfAssessments);
				//员工对应各项得分占比
				Dept dept=userService.selectSingleDept(evaluation.getProcessBean().getDeptName());
				DeptType deptType=userService.selectSingleDeptType(dept.getDid());
                model.addAttribute("monthsAssessShare", dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "考核量化分占比"));
                model.addAttribute("leaderShare", dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "领导点评评分占比"));
                model.addAttribute("overseerShare", dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "组织点评评分占比"));
                model.addAttribute("publicShare", dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "群众点评评分占比"));
                
                
        		return "/system/activiti/"+ProcessType.PROCESS_EVALUATION+"/starthalfYear";
    		}
    		//月度考核
    		if(ProcessType.EVALUCATION_MONTH.equals(buisinessType)){
    			//Evaluation evaluation=responsibilityService.selectEvaluationWithProcessInstanceId(pInstance.getId());
    			Evaluation evaluation=responsibilityService.selectSingleEvaluation(params);
    			//查询该申请人当月所有做过的项目
    			params.put("userId",evaluation.getProcessBean().getUser().getId());
    			params.put("startDate",DateUtil.formatTimesTampDate(evaluation.getStartDate()));
    			params.put("endDate", DateUtil.formatTimesTampDate(evaluation.getEndDate()));
    			List<Project> projects=responsibilityService.selectAllProject(params);
    			Double totalMark =responsibilityService.getTotalMarkOfProjects(projects);
    		    evaluation.setTotalMark(String.valueOf(totalMark));
    		    model.addAttribute("projects", projects);
        		model.addAttribute("evaluation", evaluation);
        		
        		//获取当前候选组，传递给页面，使对应的内容可以编辑
 			   
 			    String identity=responsibilityService.getIdentity(evaluation, user);
 			    model.addAttribute("identity", identity);
 			    System.out.println("**********************"+identity);
        		return "/system/activiti/"+ProcessType.PROCESS_EVALUATION+"/audit";
    		    
    		}
    		//责任清单
    		if(ProcessType.RESPONSIBILITY_TYPE.equals(buisinessType)){
    			Responsibility responsibility=responsibilityService.selectSingleResponsibility(params);
    			String identity=responsibilityService.getIdentity(responsibility.getProcessBean(), user);
    			
 			    model.addAttribute("identity", identity);
    			//如果审批时，发现生效时间已经过时，自动更新为当下
    			
    			if(responsibility.getAjustDate()!=null&&responsibility.getAjustDate().getTime()<new Date().getTime()) responsibility.setAjustDate(new Date());

    			model.addAttribute("evaluation", responsibility);
    			
    			List<Dept> depts=userService.selectDistinctSecondLevelDept();
    			model.addAttribute("depts", depts);
    			
    			return "/system/activiti/"+ProcessType.PROCESS_EVALUATION+"/startres";
    		}

    		
    	}
        
    	return null;
    }

    @RequestMapping("/complete/{taskId}/{isPass}")
    public String complete(@PathVariable String taskId,@PathVariable String isPass,Evaluation evaluation,HttpSession session,Model model,HttpServletRequest request){
    	User user=(User)session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		
        session.removeAttribute("evaluationHistory");
      
		responsibilityService.updateEvaluation(evaluation);
        HashMap<String , Object> varsHashMap=new HashMap<String, Object>();
        varsHashMap.put("pass", isPass);
        
        //必须要设置任务代理人 ，否则无法撤回
        activitiService.claim(taskId, String.valueOf(user.getId()));
        
    	this.activitiService.complete(taskId,varsHashMap);
    	
       
	
    	return "redirect:"+session.getAttribute("taskList_referer");
    }
    @RequestMapping("/complete/responsibility/{taskId}/{isPass}")
    public String completeResponsibility(@PathVariable String taskId,@PathVariable String isPass,Responsibility evaluation,HttpSession session,Model model){
    	User user=(User)session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		responsibilityService.updateResponsibility(evaluation);
		HashMap<String , Object> varsHashMap=new HashMap<String, Object>();
	    varsHashMap.put("pass", isPass);
	    if(evaluation.getAjustDate()==null||evaluation.getAjustDate().equals("")) varsHashMap.put("ajustDate",new Date());
	    varsHashMap.put("ajustDate",DateUtil.addDays(evaluation.getAjustDate(), 2));
	  //必须要设置任务代理人 ，否则无法撤回
        activitiService.claim(taskId, String.valueOf(user.getId()));
        
    	this.activitiService.complete(taskId,varsHashMap);
    
    	return "redirect:"+session.getAttribute("taskList_referer");
    }

    @RequestMapping("/withdrawTask/{processInstanceId}")
    public String withdrawTask(@PathVariable String processInstanceId,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException{
    	User user=(User)session.getAttribute("sysuser");
    	String referer=request.getHeader("referer");
    	
    	if(referer.indexOf("?msg")!=-1) referer=referer.substring(0, referer.indexOf("?msg"));
    	if(activitiService.canIwithdrawTask(processInstanceId, String.valueOf(user.getId()))){
    		activitiService.withdrawTask(processInstanceId,String.valueOf(user.getId()));
    		return "redirect:"+referer;
    	}else {
    		return "redirect:"+referer+"?msg="+URLEncoder.encode("你的申请已经被审批或无法撤回", "UTF-8");
		}
    	
		
    }
/**
 * ***********************************查看流程图*****************************
 */
	@RequestMapping("/getDiagram")
	public String getDiagram(HttpServletResponse response,HttpServletRequest request){
		String param=request.getParameter("params");
		String[] params=param.split(",");
		String processInstanceId=params[0];
		String executionId=params[1];
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			activitiService.getDiagram(processInstanceId, executionId, out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out!=null)out.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		return null;
		
	}
/**
 * *********************************历史任务查询******************************
 */
	@RequestMapping("/selectAVGDration/{deptName}/{group}")
	@ResponseBody
	public String selectAVGDration(
			@PathVariable String deptName,
			@PathVariable String group){
		
		//查询前12个月的平均耗时2018年6月
		List<String> labelList=activitiService.generateMonthLabels(12);
	
		//根据部门名，查询考核组负责人id和名字(可能有多个)
		List<User> users=userService.selectUserWithDeptNameAndGroupName(deptName, group);
		
		if(users==null) throw new RuntimeException("当前部门【"+deptName+"】未设置【"+group+"】");
		List<String> assignees=new ArrayList<String>();
		List<String> names=new ArrayList<String>();
		List<List<Integer>> datasetList=new ArrayList<List<Integer>>();
		for (User user : users) {
			assignees.add(String.valueOf(user.getId()));
			names.add(user.getUsername());
			datasetList.add(new ArrayList<Integer>());
		}
		datasetList.add(new ArrayList<Integer>());
		names.add("报社平均值");
		//查询平均值
		HashMap<String, Object> params=new HashMap<String, Object>();
		HashMap<String, Object> parMap=new HashMap<String, Object>();
		for(String titleLike:labelList){
			//根据部门和titleLike查询出processInstanceIds
			parMap.put("deptName", deptName);
			parMap.put("title", titleLike);
			List<String> processInstanceIds=processService.selectAllProcessInstanceIds(parMap);
			if(processInstanceIds==null||processInstanceIds.size()==0){
				 
                 for (List<Integer> list : datasetList) {
					list.add(0);
				 }
				 continue;
			}

			params.put("processInstanceIds", processInstanceIds);
			//计算单个部门,每个审批人的平均值
			for (int i=0;i<assignees.size();i++) {
				params.put("assignee", assignees.get(i));
				int avg=Integer.parseInt(activitiService.selectAVGDration(params));
				if(!"0".equals(avg)) avg=avg/1000/3600;
				datasetList.get(i).add(avg);
			}
			
			//计算所有部门每个月的平均值
			parMap.remove("deptName");
			List<String> pinstaces=processService.selectAllProcessInstanceIds(parMap);
			params.put("processInstanceIds", pinstaces);
			params.remove("assignee");
			int avg=Integer.parseInt(activitiService.selectAVGDration(params));
			if(!"0".equals(avg)) avg=avg/1000/3600;
			datasetList.get(datasetList.size()-1).add(avg);
			
		}
		List<Integer[]> dataset=new ArrayList<Integer[]>();
		for(List<Integer> data:datasetList){
			dataset.add(data.toArray(new Integer[data.size()]));
		}
		StringBuffer buffer=new StringBuffer("{}");
		buffer.insert(1,"\"labels\":"+JsonUtils.getGson().toJson(labelList.toArray(new String[labelList.size()]))+",\"dataset\":"+JsonUtils.getGson().toJson(dataset)+",\"names\":"+JsonUtils.getGson().toJson(names.toArray(new String[names.size()])));
		
		return buffer.toString();
	}
}
