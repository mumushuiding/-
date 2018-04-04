package com.crm.springboot.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.service.ActivitiService;
@Service
public class ActivitiServiceImpl implements ActivitiService{
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Override
	public void startProcess(String processDefinitionKey, String businessKey) {
		System.out.println("开始流程："+processDefinitionKey+","+businessKey);
		runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
		
	}

	@Override
	public List<Task> findTaskByUserId(String userIdForCandidateAndAssignee) {
		
		return taskService.createTaskQuery().taskCandidateOrAssigned(userIdForCandidateAndAssignee).list();
	}

	@Override
	public ProcessInstance getProcessInstance(Task task) {
		
		return runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
	}

	@Override
	public void completeTaskByUser(String taskId, String userId, String audit) {
		Map<String, Object> map=new HashMap<String, Object>();
		//认领任务
		taskService.claim(taskId, userId);
		//完成任务
		map.put("audit",audit);
		taskService.complete(taskId,map);
	}



	


}
