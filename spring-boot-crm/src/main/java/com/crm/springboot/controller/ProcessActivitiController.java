package com.crm.springboot.controller;

import java.io.UnsupportedEncodingException;

import java.util.Date;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crm.springboot.factory.FactoryForIProcessPoJoFactory;
import com.crm.springboot.factory.IFactoryForIProcessPojoFactory;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessType;

import com.crm.springboot.pojos.process.AbstractProcessPojo;


import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.TokenProccessor;



@Controller
@RequestMapping("/process/activiti")
public class ProcessActivitiController {
	@Autowired
	private ActivitiService activitiService;
	@Autowired
	private ResponsibilityService responsibilityService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private SysPowerService sysPowerService;
	@Autowired
	private UserService userService;
	private IFactoryForIProcessPojoFactory iFactoryForIProcessPojoFactory=FactoryForIProcessPoJoFactory.getInstance();
	@RequestMapping("/toStart/others/{processType}/{taskType}")
    public String toStartOther(Model model,@PathVariable String processType,@PathVariable String taskType,HttpSession session){
			
			User user=(User) session.getAttribute("sysuser");
			
		    AbstractProcessPojo abstractProcessPojo=iFactoryForIProcessPojoFactory.getProcessPojoFactory(taskType).getPojo();
		    
		    
			ProcessBean processBean=new ProcessBean();
		    processBean.setBusinessKey(processType);
		    processBean.setUser(user);
			processBean.setRequestedDate(DateUtil.formatTimesTampDate(new Date()));
			processBean.setDeploymentId(activitiService.getDeploymentIdByBusinessKey(processType));
			processBean.setTitle(user.getUsername()+"-"+abstractProcessPojo.getTitle());
		    processBean.setBusinessType(abstractProcessPojo.getBusinessType());
		    
		    abstractProcessPojo.setProcessBean(processBean);
			
		   
		    model.addAttribute("pojo", abstractProcessPojo);
		    
            //传递参数到前台
		    this.setAttribute(model, taskType,abstractProcessPojo);
		    
		    
		    
		    String identity=responsibilityService.getIdentity(abstractProcessPojo.getProcessBean(), user);
			model.addAttribute("identity", identity);
			
			//生成token 防止重复提交
		  	String token = TokenProccessor.getInstance().makeToken();//创建令牌
	        session.setAttribute("token", token);  //在服务器使用session保存token(令牌)
	        System.out.println("************生成令牌："+token);
			return "/system/activiti/"+processType+"/"+taskType+"Form";
			
		}	
	@RequestMapping("/start/others/{actionType}")
	public String startOthers(@PathVariable String actionType,
			Model model,
			HttpServletRequest request,
			
			@RequestParam String businessType){
		
		boolean b = isRepeatSubmit(request);//判断用户是否是重复提交
        if(b==true){
            System.out.println("请不要重复提交");
            return "redirect:/responsibility/assessmentList/all";
        }
        request.getSession().removeAttribute("token");//移除session中的token
        System.out.println("处理用户提交请求！！");

		
		AbstractProcessPojo pojo=iFactoryForIProcessPojoFactory.getProcessPojoFactory(businessType).getParamByBean(request);
		
		ProcessBean processBean=pojo.getProcessBean();
		

	    
		String parameterName="pojo";
		if ("".equals(processBean.getProcessInstanceId())||processBean.getProcessInstanceId()==null) {
			ProcessInstance pInstance=activitiService.startProcess(processBean.getBusinessKey(), processBean.getDeploymentId());
			
			//查询任务参数和候选人
			Task task=this.activitiService.getFirstTask(pInstance.getId());
			task.setAssignee(String.valueOf(processBean.getUser().getId()));

			 //添加评论
	    	if(pojo.getComment()!=null&&!"".equals(pojo.getComment().trim())) {
	    		this.activitiService.addComment(task.getId(), pojo.getComment(), String.valueOf(pojo.getProcessBean().getUser().getUsername()));
	    		pojo.setComment("");
	    	}

			processBean.setProcessInstanceId(pInstance.getProcessInstanceId());
			processBean.setExecutionId(task.getExecutionId());//查看进度图时会用到
			pojo.setProcessBean(processBean);
			HashMap<String, Object> variable=new HashMap<String, Object>();
			variable.put("pojo",pojo);
			variable.put("postId", processBean.getUser().getPost().getpId());
			
			variable.put("route", pojo.getRoute());
			activitiService.setVariable(task, variable);
			processService.save(processBean);
			
			
			
		}else {
			
			activitiService.setVariable(processBean,pojo,parameterName);
		}
		if(ProcessType.PROCESS_COMPLETE.equals(actionType)){
			
				this.activitiService.complete(processBean);
		}
		return "redirect:/responsibility/assessmentList/all";
	}
	 @RequestMapping("/perform/{taskId}")
	    public String perform(@PathVariable String taskId,Model model,
	    		@RequestParam String buisinessType,
	    		@RequestParam String processInstanceId,
	    		HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException{
	    	User user=(User)session.getAttribute("sysuser");
            //设置审批后跳转回任务列表 
	    	String referer=request.getHeader("referer");
	    	if(referer.contains("taskList"))  session.setAttribute("taskList_referer", referer);
	    	
	    	model.addAttribute("actionType", "perform");
	    	
			AbstractProcessPojo pojo=(AbstractProcessPojo) activitiService.getVariableFromProcessInstance(processInstanceId,"pojo");
		
            model.addAttribute("pojo", pojo);
           
            String identity=responsibilityService.getIdentity(pojo.getProcessBean().getProcessInstanceId(), user);
		    model.addAttribute("identity", identity);
		    
		    //设置传递给页面的属性
		    setAttribute(model, buisinessType, pojo);
		  //显示评论
		  	List<Comment> comments=activitiService.getComments(pojo.getProcessBean().getProcessInstanceId());
		  	model.addAttribute("comments", comments);
		  	//生成token 防止重复提交
		  	String token = TokenProccessor.getInstance().makeToken();//创建令牌
	        request.getSession().setAttribute("token", token);  //在服务器使用session保存token(令牌)
	        System.out.println("************生成令牌："+token);
	    	return "/system/activiti/Process/"+buisinessType+"Form";

	    }
	    @RequestMapping("/complete/{businessType}/{taskId}/{isPass}")
	    public String complete1(@PathVariable String taskId,
	    		@PathVariable String businessType,
	    		@PathVariable String isPass,
	    		HttpSession session,
	    		HttpServletRequest request){
	    	
	    	boolean b = isRepeatSubmit(request);//判断用户是否是重复提交
            if(b==true){
                System.out.println("请不要重复提交");
                return "redirect:"+session.getAttribute("taskList_referer");
            }
            request.getSession().removeAttribute("token");//移除session中的token
            System.out.println("处理用户提交请求！！");
            
	    	User user=(User)session.getAttribute("sysuser");
	    	AbstractProcessPojo pojo=iFactoryForIProcessPojoFactory.getProcessPojoFactory(businessType).getParamByBean(request);
	        
	    	try {
	    		//添加评论
		    	if(pojo.getComment()!=null&&!"".equals(pojo.getComment())) {
		    		this.activitiService.addComment(taskId, pojo.getComment(), String.valueOf(user.getUsername()));
		    		pojo.setComment("");
		    	}	
		    	//保存流程对象
		    	
		    	this.activitiService.setVariable(pojo.getProcessBean(), pojo, "pojo");
		    	
		    	this.activitiService.completeWithCommentAndAudit(taskId, String.valueOf(user.getId()), isPass, "");
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	
	    	
	    	return "redirect:"+session.getAttribute("taskList_referer");
	    	
	    }
	    @RequestMapping("/selectOthers/{processInstanceId}/{taskType}")
	    public String selectOthers(@PathVariable String processInstanceId,@PathVariable String taskType,Model model,HttpSession session){
	  	
	  	User user=(User) session.getAttribute("sysuser");
	  	  
	  	AbstractProcessPojo pojo=(AbstractProcessPojo) this.activitiService.getVariableFromHistoryWithProcessInstanceId(processInstanceId, "pojo");
        
	  	model.addAttribute("pojo", pojo);
	  	
	  	this.setAttribute(model, taskType, pojo);
	  	
	  //显示评论
	  	List<Comment> comments=activitiService.getComments(pojo.getProcessBean().getProcessInstanceId());
	  	model.addAttribute("comments", comments);
	  	
//	  	String identity=responsibilityService.getIdentity(pojo.getProcessBean(), user);
//		model.addAttribute("identity", identity);
		//生成token 防止重复提交
	  	String token = TokenProccessor.getInstance().makeToken();//创建令牌
        session.setAttribute("token", token);  //在服务器使用session保存token(令牌)
        System.out.println("************生成令牌："+token);
	  	return "/system/activiti/Process/"+taskType+"Form";
	  	  
	    }
	    
	    private void setAttribute(Model model,String taskType,AbstractProcessPojo pojo){
	    	  switch (taskType) {
				case ProcessType.BUSINESSTYPE_GROUPMANAGER:
					model.addAttribute("groups", sysPowerService.selectAllGroups());
					break;
				case ProcessType.BUSINESSTYPE_DELETEMARKSBYOVERTIME:
					
					model.addAttribute("months", responsibilityService.selectAllMonthWhichDelaySubmitMonthAssessApply(pojo.getProcessBean().getUser().getId(), 12));
					
					break;
				case ProcessType.BUSINESSTYPE_ADDMARKS:
					model.addAttribute("months",responsibilityService.selectEvaluationBusinessType(taskType));
				case ProcessType.BUSINESSTYPE_ADDDEPARTMENT:
					model.addAttribute("depts",userService.selectAllDept());
				default:
					break;
				}
	    }
	    /**
         * 判断客户端提交上来的令牌和服务器端生成的令牌是否一致
         * @param request
         * @return 
         *         true 用户重复提交了表单 
         *         false 用户没有重复提交表单
         */
        private boolean isRepeatSubmit(HttpServletRequest request) {
            String client_token = request.getParameter("token");
            System.out.println("*****************页面传递令牌："+client_token);
            //1、如果用户提交的表单数据中没有token，则用户是重复提交了表单
            if(client_token==null){
                return true;
            }
            //取出存储在Session中的token
            String server_token = (String) request.getSession().getAttribute("token");
            //2、如果当前用户的Session中不存在Token(令牌)，则用户是重复提交了表单
            if(server_token==null){
                return true;
            }
            //3、存储在Session中的Token(令牌)与表单提交的Token(令牌)不同，则用户是重复提交了表单
            if(!client_token.equals(server_token)){
                return true;
            }
            
            return false;
        }
}
