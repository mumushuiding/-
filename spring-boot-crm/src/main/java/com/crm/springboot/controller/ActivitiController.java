package com.crm.springboot.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.cmd.DeleteTaskCmd;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arjuna.ats.arjuna.common.recoveryPropertyManager;
import com.arjuna.ats.internal.arjuna.objectstore.jdbc.drivers.postgres_driver;
import com.crm.springboot.pojos.CommentVO;
import com.crm.springboot.pojos.FormField;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.Vacation;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.service.VacationService;
import com.crm.springboot.service.impl.VacationServiceImpl;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.JsonUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;

import javassist.expr.NewArray;



@Controller
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
	
//=========================location========================
    @RequestMapping("/{location}")
    public String loacate(@PathVariable String location){
    	return "system/activiti/"+location;
    }
    @RequestMapping("/{location}/{sub}")
    public String loacate1(@PathVariable String location,@PathVariable String sub,Model model){
		//用户待办任务列表
		if("taskList".equals(location)){
			if("all".equals(sub)){
				model.addAttribute("taskType", sub);
				return "system/activiti/allTaskList";
			}
			if(sub!=null){
				model.addAttribute("taskType", sub);
			}else {
				model.addAttribute("taskType", ProcessType.CANDIDATE);
			}
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
		System.out.println(JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pList), activitiService.processDefinitionCount(), pageIndex, pageSize));
		return JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pList), activitiService.processDefinitionCount(), pageIndex, pageSize);
	}
	/**
	 * 流程实例查询
	 */
	@RequestMapping("/listProcessInstance/{taskType}")
	@ResponseBody
	public String listProcessInstance(HttpSession session,Model model,Integer pageIndex,Integer pageSize,String title,@PathVariable String taskType){
		User user=(User) session.getAttribute("sysuser");
		model.addAttribute("taskType", taskType);
		JsonUtils.startPageHelper(pageIndex, pageSize);
		List<ProcessBean> processBeans=null;
		HashMap<String, Object> params = new HashMap<String,Object>();
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
		processBeans=processService.selectAllProcess(params);

		if(processBeans==null || processBeans.size()==0) return null;
		System.out.println(JsonUtils.formatListForPagination(processBeans, pageIndex, pageSize));
		return JsonUtils.formatListForPagination(processBeans, pageIndex, pageSize);
	
	
		
	}
	@RequestMapping("/deleteProcessInstance/{id}")
	public String deleteProcessInstance(@PathVariable String id,HttpServletRequest request){
		String referer=request.getHeader("referer");
		
		
		
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
		activitiService.deleteTaskById(taskId);
		return "redirect:"+referer;
	}
	//查看待办的任务列表
		@RequestMapping("/listTask/{taskType}")
		@ResponseBody
		public String listTask(@PathVariable String taskType,HttpSession session,Integer pageIndex,Integer pageSize,String taskName,Model model){
			User user=(User) session.getAttribute("sysuser");
			if(user==null) return "redirect:/user/loginForm";
			PageInfo pageInfo = null;
			
			model.addAttribute("taskType", taskType);
			HashMap<String , Object> params=new HashMap<String, Object>();
			params.put("taskName", taskName);
			
			params.put("deptNames", userService.getDeptNames(user));
			
			List<String> processInstanceIds=new ArrayList<String>();
			
			processInstanceIds.addAll(processService.selectAllProcessInstanceIds(params));

			List<String> candidateGroups=activitiService.candidateGroups(String.valueOf(user.getId()));
			List<String> list=sysPowerService.selectAllGroupIds("考查组");
			List<String> assessGroups=new ArrayList<String>();
			//添加候选组
		    for (String string : list) {
				if(candidateGroups.contains(string)){
					assessGroups.add(string);
				}
			}
            if(assessGroups!=null&&assessGroups.size()>0){
            	HashMap<String, Object> paramMap=new HashMap<String, Object>();
            	paramMap.put("candidateGroups",assessGroups.toArray(new String[assessGroups.size()]));
            	processInstanceIds.addAll(processService.selectAllProcessInstanceIds(paramMap));
            }
            
            
            
            if(ProcessType.CANDIDATE.equals(taskType)){
            	if(processInstanceIds!=null && processInstanceIds.size()>0){
                    
            		pageInfo=activitiService.listCandidatePageInfo(String.valueOf(user.getId()), processInstanceIds, pageIndex, pageSize);
            	}
            }
			if(ProcessType.ASSIGNEE.equals(taskType)){
				pageInfo=activitiService.listAssigneePageInfo(String.valueOf(user.getId()), pageIndex, pageSize);
			}else{
				if(processInstanceIds!=null){
					pageInfo=activitiService.listCandidatePageInfo(String.valueOf(user.getId()),processInstanceIds, pageIndex, pageSize);
				}
				model.addAttribute("taskType", ProcessType.CANDIDATE);
			}
			if(pageInfo==null) return null;
            System.out.println(JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pageInfo.getList()), pageInfo.getTotal(), pageIndex, pageSize));
			return JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pageInfo.getList()), pageInfo.getTotal(), pageIndex, pageSize);
		}
/**
 * *****************************流程******************************************
 */
		//启动流程
        @RequestMapping("/preStart/{processType}/{taskType}")
        public String preStart(Model model,@PathVariable String processType,@PathVariable String taskType,HttpSession session,HttpServletRequest request){
        	User user=(User)session.getAttribute("sysuser");
			if(user==null)return "redirect:/user/loginForm";
			
			//判断能否启动当前流程
        	if(!responsibilityService.canIStartEvaluationProcess(user)){
        		System.out.println("referer="+request.getHeader("referer"));
        		session.setAttribute("msg", "你的职级，无法启动这个流程");
        		return "redirect:"+request.getHeader("referer");
        	}
			
			if("EvaluationProcess".equals(processType)){
				
			
			    ProcessBean processBean=new ProcessBean();
			    processBean.setBusinessKey(processType);

				List<String> taskTitle=null;
				List<String> types=null;
				
				String action="";
				
				if("monthForm".equals(taskType)){
					action="/system/activiti/toStartEvaluation/startmonth";
					types=responsibilityService.selectEvaluationType(ProcessType.EVALUCATION_MONTH);
					taskTitle=responsibilityService.selectEvaluationBusinessType(ProcessType.EVALUCATION_MONTH);
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
			if("EvaluationProcess".equals(responsibility.getProcessBean().getBusinessKey())){
				//将开始时间startDate和endDate放入params
				
				HashMap<String, Object> params =responsibilityService.getStartDateStrAndEndDateDateStr(responsibility.getSparation());
				
				responsibility.setStartDate(DateUtil.parseDefaultDate(String.valueOf(params.get("startDate"))));
				responsibility.setEndDate(DateUtil.parseDefaultDate(String.valueOf(params.get("endDate"))));

				Date createTime=new Date();
				responsibility.setProcessBean(processBean);
				responsibility.setCreateTime(createTime);
				model.addAttribute("evaluation", responsibility);
				
				//查询所有不同的二级部门
				List<Dept> depts=userService.selectDistinctSecondLevelDept();
				model.addAttribute("depts", depts);
			}
			
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
			dept.setDid(Integer.parseInt(responsibility.getSecondLevelId()));
			userLinkDept.setSecondLevel(dept);
			
			Dept dept2=new Dept();
			dept2.setDid(100);
			userLinkDept.setThirdLevel(dept2);
			variables.put("userLinkDept",userLinkDept);
			//调整的职位
			
			user.setPosition(responsibility.getPosition());
			
			variables.put("user",user);
			
			
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
    			processBean.setCommitted("1");
    			processService.updateProcess(processBean);
    			responsibilityService.updateResponsibility(responsibility);
    			this.activitiService.complete(this.activitiService.getFirstTask(responsibility.getProcessBean().getProcessInstanceId()).getId());
    		}

    		
    		return "redirect:/responsibility/assessmentList/res";
        }
/*****************************************月度考核或者年度考核***************************************************************
 */
		@RequestMapping("/toStartEvaluation/{taskType}")
		public String toStartEvaluation(@RequestParam(required=false)String msg,Model model,Evaluation evaluation,HttpSession session,@PathVariable String taskType) throws UnsupportedEncodingException{
			User user=(User)session.getAttribute("sysuser");
			if(user==null)return "redirect:/user/loginForm";
			
			
			if (evaluation.getProcessBean()==null) {
				evaluation=(Evaluation) session.getAttribute("evaluation");
			}
			
			session.setAttribute("evaluation", evaluation);
			ProcessBean processBean=evaluation.getProcessBean();
			Date createTime=new Date();
			String deploymentId=activitiService.getDeploymentIdByBusinessKey(evaluation.getProcessBean().getBusinessKey());
			if("EvaluationProcess".equals(evaluation.getProcessBean().getBusinessKey())){
				//将开始时间startDate和endDate放入params
				HashMap<String, Object> params =responsibilityService.getStartDateStrAndEndDateDateStr(evaluation.getSparation());
				
				evaluation.setStartDate(DateUtil.parseDefaultDate(String.valueOf(params.get("startDate"))));
				evaluation.setEndDate(DateUtil.parseDefaultDate(String.valueOf(params.get("endDate"))));
				
				params.put("userId",user.getId());
         

				List<Project> projects=responsibilityService.selectAllProject(params);
			    int totalMark =responsibilityService.getTotalMarkOfProjects(projects);
			    
			 
			    
			    evaluation.setTotalMark(String.valueOf(totalMark));
			   
				if("startmonth".equals(taskType)){
					evaluation.setCreateTime(createTime);
					processBean.setDeploymentId(deploymentId);
					
					processBean.setUser(user);
					evaluation.setProcessBean(processBean);
					evaluation.setAttendance("旷工()天，请假()天");
					model.addAttribute("projects", projects);
					model.addAttribute("evaluation", evaluation);
				}
			}
			if(msg!=null) model.addAttribute("msg", URLDecoder.decode(msg, "UTF-8"));
			return "/system/activiti/"+processBean.getBusinessKey()+"/"+taskType;
		}
	@RequestMapping("/startEvaluation/{actionType}")
	public String startEvaluation(Evaluation evaluation,Model model,HttpSession session,@PathVariable String actionType){
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
			processBean.setCommitted("1");
			processService.updateProcess(processBean);
			responsibilityService.updateEvaluation(evaluation);
			this.activitiService.complete(this.activitiService.getFirstTask(evaluation.getProcessBean().getProcessInstanceId()).getId());
		}

		
		return "redirect:/responsibility/assessmentList/month";
	}

	
	//办理任务
	@RequestMapping("/claim/{taskId}/{buisinessType}")
	public String claim(@PathVariable String taskId,HttpSession session,@PathVariable String buisinessType) throws UnsupportedEncodingException{
		User user=(User)session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		activitiService.claim(taskId, String.valueOf(user.getId()));
		
		
		return "redirect:/system/activiti/perform/"+taskId+"/?buisinessType="+URLEncoder.encode(buisinessType, "UTF-8");
	}
    @RequestMapping("/perform/{taskId}")
    public String perform(@PathVariable String taskId,Model model,@RequestParam String buisinessType) throws UnsupportedEncodingException{
    	String x=URLDecoder.decode(buisinessType, "UTF-8");
    	buisinessType=x;
    	model.addAttribute("actionType", "perform");
    	ProcessInstance pInstance=activitiService.getProcessInstance(taskId);
    	model.addAttribute("candidateGroupList", activitiService.selectCandidateGroup(taskId));
    	System.out.println("buisinessType="+buisinessType);
        //考核审批流程
    	if(ProcessType.PROCESS_EVALUATION.equals(pInstance.getProcessDefinitionKey())){
    		HashMap<String, Object> params = new HashMap<String,Object>();
    		params.put("processInstanceId", pInstance.getId());
    		//月度考核
    		if(ProcessType.EVALUCATION_MONTH.equals(buisinessType)){
    			//Evaluation evaluation=responsibilityService.selectEvaluationWithProcessInstanceId(pInstance.getId());
    			Evaluation evaluation=responsibilityService.selectSingleEvaluation(params);
    			//查询该申请人当月所有做过的项目
    			params.put("userId",evaluation.getProcessBean().getUser().getId());
    			params.put("startDate",DateUtil.formatTimesTampDate(evaluation.getStartDate()));
    			params.put("endDate", DateUtil.formatTimesTampDate(evaluation.getEndDate()));
    			List<Project> projects=responsibilityService.selectAllProject(params);
    			int totalMark =responsibilityService.getTotalMarkOfProjects(projects);
    		    evaluation.setTotalMark(String.valueOf(totalMark));
    		    model.addAttribute("projects", projects);
        		model.addAttribute("evaluation", evaluation);
        		return "/system/activiti/"+ProcessType.PROCESS_EVALUATION+"/audit";
    		    
    		}
    		if(ProcessType.RESPONSIBILITY_TYPE.equals(buisinessType)){
    			Responsibility responsibility=responsibilityService.selectSingleResponsibility(params);
    			//如果审批时，发现生效时间已经过时，自动更新为当下
    			if(responsibility.getAjustDate().getTime()<new Date().getTime()) responsibility.setAjustDate(new Date());
    			
//    			ProcessBean processBean=(ProcessBean) activitiService.getVariableFromProcessInstance(pInstance.getId(),"arg");
//    			
//    			responsibility.setProcessBean(processBean);
    			model.addAttribute("evaluation", responsibility);
    			
    			List<Dept> depts=userService.selectDistinctSecondLevelDept();
    			model.addAttribute("depts", depts);
    			
    			return "/system/activiti/"+ProcessType.PROCESS_EVALUATION+"/startres";
    		}
    		
    		
    	}
        
    	return null;
    }
    @RequestMapping("/complete/responsibility/{taskId}/{isPass}")
    public String completeResponsibility(@PathVariable String taskId,@PathVariable String isPass,Responsibility evaluation,HttpSession session,Model model){
    	User user=(User)session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		responsibilityService.updateResponsibility(evaluation);
		HashMap<String , Object> varsHashMap=new HashMap<String, Object>();
	    varsHashMap.put("pass", isPass);
        if("false".equals(isPass)){
        	
        	ProcessBean processBean=evaluation.getProcessBean();
        	ProcessInstance pi=activitiService.selectProcessInstance(evaluation.getProcessBean().getProcessInstanceId());
        	Task task=this.activitiService.getFirstTask(pi.getId());
        	processBean.setFirstTaskId(task.getId());
        	processService.update(processBean);
        	responsibilityService.updateResponsibility(evaluation);
        }
    	this.activitiService.complete(taskId,varsHashMap);
    	
		return "redirect:/system/activiti/taskList/"+ProcessType.ASSIGNEE;
    }
    @RequestMapping("/complete/{taskId}/{isPass}")
    public String complete(@PathVariable String taskId,@PathVariable String isPass,Evaluation evaluation,HttpSession session,Model model){
    	User user=(User)session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		
        session.removeAttribute("evaluationHistory");
        
		responsibilityService.updateEvaluation(evaluation);
        HashMap<String , Object> varsHashMap=new HashMap<String, Object>();
        varsHashMap.put("pass", isPass);
        if("false".equals(isPass)){
        	
        	ProcessBean processBean=evaluation.getProcessBean();
        	ProcessInstance pi=activitiService.selectProcessInstance(evaluation.getProcessBean().getProcessInstanceId());
        	Task task=this.activitiService.getFirstTask(pi.getId());
        	processBean.setFirstTaskId(task.getId());
        	processService.update(processBean);
        	responsibilityService.updateEvaluation(evaluation);
        }
    	this.activitiService.complete(taskId,varsHashMap);
    	
		return "redirect:/system/activiti/taskList/"+ProcessType.ASSIGNEE;
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
}
