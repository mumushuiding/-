package com.crm.springboot.service;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.crm.springboot.pojos.FormField;
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessVO;
import com.crm.springboot.pojos.TaskVO;
import com.github.pagehelper.PageInfo;

public interface ActivitiService extends BaseActivitiService{
	
	/**
	 * ********************将数据封装成分页的对象 pageinfo*******************
	 */
	PageInfo getPageInfo(List<?> list,long totalSize);
	
	 //同步act_id_membership和groupmanager表格的内容
	void asnyMembership(GroupManager groupManager);
	
   /**
    * *********************************申请列表****************************************
    */
	
	long countUserApplyList(Serializable assignee);
//	List<Task> getUserApplyList(Serializable assignee, Integer pageIndex, Integer pageSize);
	// 读取请假申请

	/**
	 * ********************************流程实例查询*************************************
	 */
	long countAllProcessInstance();
	List<ProcessInstance> selectAllProcessInstances(Integer pageIndex, Integer pageSize);
	PageInfo selectAllProcessInstancesPageInfo(Integer pageIndex, Integer pageSize);
	List<ProcessVO> createProcessVOs(List<ProcessInstance> processInstances);
	/**
	 * ********************************历史流程实例查询*************************************
	 */
	long countAllHistoryProcessInstance();
	List<HistoricProcessInstance> selectAllHistoryProcessInstances(Integer pageIndex, Integer pageSize);
	PageInfo selectAllHistoryProcessInstancesPageInfo(Integer pageIndex, Integer pageSize);
	List<ProcessVO> createHistoricProcessVOs(List<HistoricProcessInstance> historicProcessInstances);
	/**
	 * 
	 * *********************************task****************
	 */
	Task selectTask(String processInstanceId,String taskDefinitionKey);
	void setTaskCandidateGroup(String processInstanceId,String taskDefinitionKey,String delegateTaskName);
   /**
    * *********************************待办业务列表****************************************
    */
	
	long countAllTaskList();
	List<Task> selectAllTask(Integer pageIndex, Integer pageSize);
	PageInfo selectAllTaskPageInfo(Integer pageIndex, Integer pageSize);
	long countUserTaskList(Serializable userId);
	List<Task> getTaskToDoList(Serializable userId, Integer pageIndex, Integer pageSize);
	//查询待办的全部任务
    List<Task> listCandidateTasks(String userId,Integer pageIndex, Integer pageSize);
    long countListCandidateTasks(String userId);
    PageInfo listCandidatePageInfo(String userId,Integer pageIndex, Integer pageSize);
   // 查询用户所受理的全部任务
 	List<Task> listAssigneeTasks(String userId,Integer pageIndex, Integer pageSize);
 	long countListAssigneeTasks(String userId);
 	PageInfo listAssigneePageInfo(String userId,Integer pageIndex, Integer pageSize);
 // 将Task集合转为TaskVO集合
 	List<TaskVO> createTaskVOList(List<Task> tasks);
 	
 	//查询一个任务所在流程的全部评论
 	List<Comment> getComments(String taskId);

}
