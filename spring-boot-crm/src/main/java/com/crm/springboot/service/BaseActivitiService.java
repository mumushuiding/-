package com.crm.springboot.service;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.crm.springboot.pojos.GroupTable;

public interface BaseActivitiService {
	/**
	 * ********************************用户组管理*************************************
	 */
	
	List<Group> selectGroups(int nowPage,int pageSize);
	List<Group> selectGroups();
	long GroupsCount();
	Group saveGroup(GroupTable groupTable);
	void updateGroup(GroupTable groupTable);
	void deleteGroup(Serializable id);
	
	/**
	 * ********************************用户管理*************************************
	 */
	
	void saveUser(Serializable id);
	void deleteUser(Serializable id);
	User selectUser(Serializable id);
	/**
	 * ********************************用户与用户组关系*************************************
	 */
	void bindUserAndGroup(String userId,String groupId);
	void bindUserAndGroups(String userId,String[] groupIds);
	void unBindUserAndGroup(String userId,String groupId);
	
	/**
	 * ********************************流程存储与查询*************************************
	 */
    long processDefinitionCount();
	List<ProcessDefinition> selectAllProcessDefinition(int pageIndex,int pageSize);
	
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
