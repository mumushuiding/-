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

import com.crm.springboot.pojos.CommentVO;
import com.crm.springboot.pojos.FormField;
import com.crm.springboot.pojos.TaskVO;
import com.crm.springboot.pojos.User;
import com.crm.springboot.pojos.Vacation;
import com.crm.springboot.service.ActivitiService;
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
	private ActivitiService activitiService;
	@Autowired 
	private VacationService vacationService;
	

	
	
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
	@RequestMapping("/listProcessInstance")
	@ResponseBody
	public String listProcessInstance(HttpSession session,Model model,Integer pageIndex,Integer pageSize){
		User user=(User) session.getAttribute("sysuser");
		PageInfo pageInfo=activitiService.selectAllProcessInstancesPageInfo(pageIndex, pageSize);
		System.out.println(JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pageInfo.getList()), pageInfo.getTotal(), pageIndex, pageSize));
		return JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pageInfo.getList()), pageInfo.getTotal(), pageIndex, pageSize);
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
		public String listTask(@PathVariable String taskType,HttpSession session,Integer pageIndex,Integer pageSize,Model model){
			User user=(User) session.getAttribute("sysuser");
			if(user==null) return "redirect:/user/loginForm";
			PageInfo pageInfo = null;
			
			model.addAttribute("taskType", taskType);
			if(TaskVO.CANDIDATE.equals(taskType)){
				pageInfo=activitiService.listCandidatePageInfo(String.valueOf(user.getId()), pageIndex, pageSize);
			}
			if(TaskVO.ASSIGNEE.equals(taskType)){
				pageInfo=activitiService.listAssigneePageInfo(String.valueOf(user.getId()), pageIndex, pageSize);
			}else{
				pageInfo=activitiService.listCandidatePageInfo(String.valueOf(user.getId()), pageIndex, pageSize);
				model.addAttribute("taskType", TaskVO.CANDIDATE);
			}
			System.out.println(JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pageInfo.getList()), pageInfo.getTotal(), pageIndex, pageSize));
			return JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pageInfo.getList()), pageInfo.getTotal(), pageIndex, pageSize);
		}
/**
 * *****************************流程******************************************
 */
		//启动流程
		/**
		 * params[0]对应processDefinitonKey,params[1]对应deploymentId
		 * @param model
		 * @param request
		 * @return
		 */
		@RequestMapping("/toStart")
		public String toStart(Model model,HttpServletRequest request){

			String[] params=request.getParameter("param").split(",");
			if("Vacation".equals(params[0])){
				Vacation vacation=new Vacation();
				vacation.setDeploymentId(params[1]);
	            vacation.setBusinessKey(params[0]);
				model.addAttribute("vacation",vacation);
			}
			return "/activiti/"+params[0]+"/start";
		}
	
	@RequestMapping("/startVacation")
	public String startVacation(Vacation vacation,Model model,HttpSession session){
		User user=(User)session.getAttribute("sysuser");
		if(user==null)return "redirect:/user/loginForm";
		// 启动流程
		ProcessInstance pi=activitiService.startProcess(vacation.getBusinessKey(), vacation.getDeploymentId());
		
		// 记录请假数据
		
		vacation.setUser(user);
		vacation.setTitle(user.getUsername()+" 的请假申请");
		vacation.setBusinessType("请假申请");
		vacation.setRequestedDate(DateUtil.formatTimesTampDate(new Date()));
		vacation.setProcessInstanceId(pi.getId());
		vacation.setEmailTo(user.getEmail());
		vacation.setEmailFrom(user.getEmail());
		
		// 初始化任务参数
		HashMap<String, Object> variables=new HashMap<String, Object>();
		variables.put("arg",vacation);
		variables.put("html","你的申请已经通过");
		//查询第一任务
		Task fTask=this.activitiService.getFirstTask(pi.getId());
		vacation.setExecutionId(fTask.getExecutionId());
		vacationService.save(vacation);
		//设置任务代理人
		this.activitiService.setAssignee(fTask.getId(), String.valueOf(vacation.getUser().getId()));
		//完成 任务
		this.activitiService.complete(fTask.getId(), variables);
		return "/activiti/vacation/list";
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
    	List<Comment> comments=activitiService.getComments(taskId);
    	List<FormField> formFields=activitiService.getFormFields(taskId);
 
    	model.addAttribute("comments", comments);
    	model.addAttribute("formFields", formFields);
    	model.addAttribute("commentVO", new CommentVO());
    	model.addAttribute("taskId", taskId);
    	System.out.println(JsonUtils.getGson().toJson(comments));
    	System.out.println(JsonUtils.getGson().toJson(formFields));
    	return "/system/activiti/auditForm";
    }
    @RequestMapping("/complete/{taskId}")
    public String complete(@PathVariable String taskId,CommentVO commentVO,HttpSession session){
    	User user=(User)session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		System.out.println("comment:"+commentVO.getComment()+",taskId:"+taskId+",audit="+commentVO.isAudit());
		activitiService.completeWithCommentAndAudit(taskId, String.valueOf(user.getId()), commentVO.isAudit(), commentVO.getComment());
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
