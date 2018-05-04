package com.crm.springboot.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arjuna.ats.arjuna.common.recoveryPropertyManager;
import com.arjuna.ats.internal.arjuna.objectstore.jdbc.drivers.postgres_driver;
import com.crm.springboot.pojos.CommentVO;
import com.crm.springboot.pojos.FormField;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.TaskVO;
import com.crm.springboot.pojos.Vacation;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.ResponsibilityService;
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
	private VacationService vacationService;
	
    @Autowired
    private UserService userService;
	
	
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
				model.addAttribute("taskType", TaskVO.CANDIDATE);
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

		if("all".equals(taskType)){
			processBeans=processService.selectAllProcess(params);
		}
		
		
		params.put("userId", String.valueOf(user.getId()));
		if("month".equals(taskType)){
			params.put("businessType", "月度考核");
			processBeans=processService.selectAllProcess(params);
			
		}
		
		
		System.out.println(processBeans!=null?JsonUtils.formatListForPagination(processBeans, pageIndex, pageSize):null);
		return processBeans!=null?JsonUtils.formatListForPagination(processBeans, pageIndex, pageSize):null;
	
	
		
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
			
			List<String> processInstanceIds=processService.selectAllProcessInstanceIds(params);
	
			if(TaskVO.CANDIDATE.equals(taskType)){
				if(processInstanceIds!=null){
			
					pageInfo=activitiService.listCandidatePageInfo(String.valueOf(user.getId()),processInstanceIds, pageIndex, pageSize);
				}
				
			}
			if(TaskVO.ASSIGNEE.equals(taskType)){
				pageInfo=activitiService.listAssigneePageInfo(String.valueOf(user.getId()), pageIndex, pageSize);
			}else{
				if(processInstanceIds!=null){
					pageInfo=activitiService.listCandidatePageInfo(String.valueOf(user.getId()),processInstanceIds, pageIndex, pageSize);
				}
				model.addAttribute("taskType", TaskVO.CANDIDATE);
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
        	if(!responsibilityService.canIStartEvaluationProcess(user)){
        		System.out.println("referer="+request.getHeader("referer"));
        		session.setAttribute("msg", "你的职级，无法启动这个流程");
        		return "redirect:"+request.getHeader("referer");
        	}
			
			if("EvaluationProcess".equals(processType)){
				
				if("monthForm".equals(taskType)){
					Evaluation evaluation=new Evaluation();
				    ProcessBean processBean=new ProcessBean();
				    processBean.setBusinessKey(processType);
					evaluation.setProcessBean(processBean);
                  
					
					model.addAttribute("taskTitle",responsibilityService.selectEvaluationBusinessType(ProcessType.EVALUCATION_MONTH));
					
				    model.addAttribute("evaluation", evaluation);
					
					return "/system/activiti/"+processType+"/"+taskType;
				}
			}
			return null;
        }
		@RequestMapping("/toStartEvaluation/{taskType}")
		public String toStartEvaluation(Model model,Evaluation evaluation,HttpSession session,@PathVariable String taskType){
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
			//processBean.setTitle(user.getUsername()+"-"+responsibilityService.getTitle(evaluation.getStartDate(), evaluation.getEndDate()));
			processBean.setTitle(user.getUsername()+"-"+evaluation.getSparation());
			processBean.setBusinessType(responsibilityService.getBusinessType(evaluation.getStartDate(), evaluation.getEndDate()));
			processBean.setRequestedDate(DateUtil.formatTimesTampDate(new Date()));
			processBean.setProcessInstanceId(pInstance.getId());
			
			evaluation.setCreateTime(new Date());
			//查询第一任务
			Task task=this.activitiService.getFirstTask(pInstance.getId());
			
			processBean.setExecutionId(task.getExecutionId());
			
			evaluation.setProcessBean(processBean);
			processService.save(processBean);
			responsibilityService.saveEvaluation(evaluation);
			//设置任务代理人
			this.activitiService.setAssignee(task.getId(), String.valueOf(user.getId()));
			//任务参数初始化
			HashMap<String, Object> variables=new HashMap<String, Object>();
			variables.put("arg", processBean);
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
	@RequestMapping("/claim/{taskId}")
	public String claim(@PathVariable String taskId,HttpSession session){
		User user=(User)session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		activitiService.claim(taskId, String.valueOf(user.getId()));
		return "redirect:/system/activiti/taskList/"+TaskVO.ASSIGNEE;
	}
    @RequestMapping("/perform/{taskId}")
    public String perform(@PathVariable String taskId,Model model){
    
    	ProcessInstance pInstance=activitiService.getProcessInstance(taskId);
    	
        
    	if(ProcessType.PROCESS_EVALUATION.equals(pInstance.getProcessDefinitionKey())){
    		Evaluation evaluation=responsibilityService.selectEvaluationWithProcessInstanceId(pInstance.getId());
    		
    		HashMap<String, Object> params = new HashMap<String,Object>();
			params.put("userId",evaluation.getProcessBean().getUser().getId());
			params.put("startDate",DateUtil.formatTimesTampDate(evaluation.getStartDate()));
			params.put("endDate", DateUtil.formatTimesTampDate(evaluation.getEndDate()));

			List<Project> projects=responsibilityService.selectAllProject(params);
			model.addAttribute("projects", projects);
    		model.addAttribute("evaluation", evaluation);
    		model.addAttribute("candidateGroupList", activitiService.selectCandidateGroup(taskId));
    		return "/system/activiti/"+ProcessType.PROCESS_EVALUATION+"/audit";
    	}

    	return "/system/activiti/auditForm";
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
        	evaluation.setCommitted("0");
        	ProcessBean processBean=evaluation.getProcessBean();
        	ProcessInstance pi=activitiService.selectProcessInstance(evaluation.getProcessBean().getProcessInstanceId());
        	Task task=this.activitiService.getFirstTask(pi.getId());
        	processBean.setFirstTaskId(task.getId());
        	processService.update(processBean);
        	responsibilityService.updateEvaluation(evaluation);
        }
    	this.activitiService.complete(taskId,varsHashMap);
    	
		return "redirect:/system/activiti/taskList/assignee";
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
