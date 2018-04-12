package com.crm.springboot.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crm.springboot.pojos.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.utils.JsonUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;


@Controller
@RequestMapping(value="/system/activiti")
public class ActivitiController {
	
	@Autowired
	private ActivitiService activitiService;
	
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RuntimeService runtimeService;
	
	
//=========================location========================
    @RequestMapping("/{location}")
    public String loacate(@PathVariable String location){
    	return "system/activiti/"+location;
    }
    
    
    
	@RequestMapping("/test")
	@ResponseBody
	public void test(){
		System.out.println("-----------taskService="+taskService);
		System.out.println("-----------processEngine="+processEngine);
	}
	@RequestMapping("/start")
	@ResponseBody
	public void startProcess(){
		System.out.println("启动流程前-------------");
		runtimeService.startProcessInstanceById("test");
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
	@RequestMapping("/processDefInfo")
	@ResponseBody
	public String selectAllProcessDefinition(Integer pageIndex,Integer pageSize){
		pageIndex=pageIndex==null?1:pageIndex;
		pageSize=pageSize==null?10:pageSize;
		List<ProcessDefinition> pList=activitiService.selectAllProcessDefinition(pageIndex, pageSize);
		System.out.println(JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pList), activitiService.processDefinitionCount(), pageIndex, pageSize));
		return JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(pList), activitiService.processDefinitionCount(), pageIndex, pageSize);
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
	//启动流程
	@RequestMapping("/startProcess/{processDefinitionKey}")
	public String startProcess(@PathVariable String processDefinitionKey){
		
		return ;
	}
}
