package com.crm.springboot.service;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public interface BaseActivitiService {
	/**
	 * 启动流程
	 * @param processId
	 * @param bizKey
	 */
	void startProcess(String processDefinitionKey, String businessKey);
	/**
	 * 根据审批人id查询需要审批的任务
	 * @param userId
	 * @return
	 */
	List<Task> findTaskByUserId(String userIdForCandidateAndAssignee);
	/**
	 * 获得业务流程实例
	 * @param task
	 * @return
	 */
	ProcessInstance getProcessInstance(Task task);
	/**
	 * 审批
	 * @param taskId 审批的任务id
	 * @param userId 审批人的id
	 * @param audit  审批意见：通过（pass）or驳回（reject）
	 */
	void completeTaskByUser(String taskId,String userId,String audit);
}
