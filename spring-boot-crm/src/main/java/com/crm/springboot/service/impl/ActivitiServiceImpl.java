package com.crm.springboot.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.GroupTable;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessVO;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.RegexUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class ActivitiServiceImpl implements ActivitiService{
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private HistoryService historyService;

    
	@Autowired ProcessService processService;

	@Autowired
	private ResponsibilityService responsibilityService;
    @Autowired
    private SysPowerService sysPowerService;
	@Override
	public List<Group> selectGroups(int nowPage,int pageSize) {
		
		return identityService.createGroupQuery().listPage((nowPage-1)*pageSize, pageSize);
	}

	@Override
	public Group saveGroup(GroupTable groupTable) {
		Group group=identityService.newGroup(String.valueOf(groupTable.getGroupid()));
		group.setName(groupTable.getGroupname());
		group.setType(groupTable.getGroupType());
		identityService.saveGroup(group);
		return group;
	}

	@Override
	public List<Group> selectGroups() {
		
		return identityService.createGroupQuery().list();
	}

	@Override
	public long GroupsCount() {
		
		return identityService.createGroupQuery().count();
	}

	@Override
	public void updateGroup(GroupTable groupTable) {
		Group group=identityService.createGroupQuery().groupId(String.valueOf(groupTable.getGroupid())).singleResult();
		group.setName(groupTable.getGroupname());
		group.setType(groupTable.getGroupType());
		identityService.saveGroup(group);
	}

	@Override
	public void deleteGroup(Serializable id) {
		identityService.deleteGroup(String.valueOf(id));
		
	}

	@Override
	public void saveUser(Serializable id) {
		User user=identityService.newUser(String.valueOf(id));
		identityService.saveUser(user);
	}

	@Override
	public void deleteUser(Serializable id) {
		identityService.deleteUser(String.valueOf(id));
	
		
	}

	@Override
	public void bindUserAndGroup(String userId, String groupId) {
		identityService.createMembership(userId, groupId);
		
	}

	@Override
	public void bindUserAndGroups(String userId, String[] groupIds) {
		for (String groupId : groupIds) {
			identityService.createMembership(userId, groupId);
		}
		
	}

	@Override
	public User selectUser(Serializable id) {
		
		return identityService.createUserQuery().userId(String.valueOf(id)).singleResult();
	}

	@Override
	public void unBindUserAndGroup(String userId, String groupId) {
		identityService.deleteMembership(userId, groupId);
		
	}

	@Override
	public List<ProcessDefinition> selectAllProcessDefinition(int pageIndex, int pageSize) {
		
		return repositoryService.createProcessDefinitionQuery().listPage((pageIndex-1)*pageSize, pageSize);
	}

	@Override
	public long processDefinitionCount() {
		
		return repositoryService.createProcessDefinitionQuery().count();
	}
    @Override
    public void showDiagram(InputStream is,OutputStream out) {
    	
    	try {
			byte[] b=new byte[1024];
			for(int len=-1;(len=is.read(b))!=-1;){
				out.write(b, 0, len);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(is!=null)is.close();
				if(out!=null)out.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
    }
	@Override
	public void viewProcessImage(Serializable deploymentId,String resourceName,OutputStream out) {
		
		InputStream is = repositoryService.getResourceAsStream(String.valueOf(deploymentId), resourceName);
		showDiagram(is, out);
	}

	@Override
	public ProcessInstance startProcess(String processDefinitionKey,String deploymentId) {
		ProcessDefinition pd=repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploymentId)
				.processDefinitionKey(processDefinitionKey)
				.singleResult();
		ProcessInstance pi=runtimeService.startProcessInstanceByKey(pd.getKey());

		return pi;
	}



	@Override
	public long countUserApplyList(Serializable assignee) {
		
		return taskService.createTaskQuery().taskAssignee(String.valueOf(assignee)).count();
	}

	@Override
	public Group selectGroupById(Serializable id) {
		
		return identityService.createGroupQuery().groupId(String.valueOf(id)).singleResult();
	}
	@Override
	
	
	public void asnyMembership(GroupManager groupManager){
		List<User> users=identityService.createUserQuery().memberOfGroup(groupManager.getGroupid()).list();
		boolean flag=false;//为真时 说明存在相同的元素
		for(User user:users){
			
			if(user!=null){
				if(user.getId().equals(String.valueOf(groupManager.getUserid()))){
					flag=true;
					break;
				}
				
				
			}
		}
		if(!flag){
			identityService.createMembership(String.valueOf(groupManager.getUserid()), String.valueOf(groupManager.getGroupid()));
		}
	}
	@Override
	public void getDiagram(String processInstanceId,String executionId,OutputStream out) {
		ProcessInstance pi=runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
	    
		BpmnModel bpmnModel= repositoryService.getBpmnModel(pi.getProcessDefinitionId());
	    InputStream is=new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png",
	    		runtimeService.getActiveActivityIds(executionId), Collections.<String>emptyList(), "宋体", "宋体", "宋体", null, 2.0);
	    showDiagram(is, out);
	}

	@Override
	public long countUserTaskList(Serializable userId) {
		
				
		return taskService.createTaskQuery().taskCandidateOrAssigned(String.valueOf(userId)).count();
	}

	@Override
	public List<Task> getTaskToDoList(Serializable userId, Integer pageIndex, Integer pageSize) {
		List<Group> groups=identityService.createGroupQuery().groupMember(String.valueOf(userId)).list();
		List<String> groupIds=new ArrayList<String>();
		for(Group group:groups){
			
			groupIds.add(group.getId());
		}
		return taskService.createTaskQuery().taskCandidateOrAssigned(String.valueOf(userId)).listPage((pageIndex-1)*pageSize, pageSize);
	}

	

	@Override
	public void claim(String taskId, String userId) {
		
		taskService.claim(taskId, userId);
	}
	@Override
	public List<String> candidateGroups(String userId) {
		List<Group> groups=identityService.createGroupQuery().groupMember(String.valueOf(userId)).list();
		List<String> groupIds=new ArrayList<String>();
		for(Group group:groups){
			
			groupIds.add(group.getId());
		}
		return groupIds;
	}
	@Override
	public List<Task> listCandidateAndAssigneeTasks(String userId, List<String> processInstanceIds, Integer pageIndex,
			Integer pageSize) {
        if(processInstanceIds==null||processInstanceIds.size()==0){
			
			return null;
		}else {
			
			return taskService.createTaskQuery()
					.processInstanceIdIn(processInstanceIds).taskCandidateOrAssigned(userId).orderByTaskCreateTime().desc()
					.listPage((pageIndex-1)*pageSize, pageSize);
		}
	}

	@Override
	public long countListCandidateAndAssigneeTasks(String userId, List<String> processInstanceIds) {
		if(processInstanceIds==null || processInstanceIds.size()==0) return 0;
		
		return taskService.createTaskQuery()
				.processInstanceIdIn(processInstanceIds).taskCandidateOrAssigned(userId).count();
	}

	@Override
	public PageInfo listCandidateAndAssigneePageInfo(String userId, List<String> processInstanceIds, Integer pageIndex,
			Integer pageSize) {
		
		return this.getPageInfo(createTaskVOList(this.listCandidateAndAssigneeTasks(userId,processInstanceIds, pageIndex, pageSize)), this.countListCandidateAndAssigneeTasks(userId, processInstanceIds));
	}
	@Override
	public List<Task> listCandidateTasks(String userId,List<String> processInstanceIds, Integer pageIndex, Integer pageSize) {
		//查询用户所在的所有用户组

		List<String> candidateGroups=this.candidateGroups(userId);
        
		if(candidateGroups==null||candidateGroups.size()==0){
			return null;
		}


		if(processInstanceIds.size()==0){
			
			return null;
		}else {
			
			return taskService.createTaskQuery()
					.processInstanceIdIn(processInstanceIds).taskCandidateGroupIn(candidateGroups)
					.listPage((pageIndex-1)*pageSize, pageSize);
		}
		
	
	}

	@Override
	public List<Task> listAssigneeTasks(String userId, Integer pageIndex, Integer pageSize) {
	
		return taskService.createTaskQuery().taskAssignee(userId).listPage((pageIndex-1)*pageSize, pageSize);
	}

	@Override
	public List<ProcessBean> createTaskVOList(List<Task> tasks) {
		if(tasks==null) return null;
		List<ProcessBean> result=new ArrayList<ProcessBean>();
		if(tasks==null) return result;
		
		HashMap<String, Object> params=new HashMap<String, Object>();
		for(Task task:tasks){
			ProcessInstance pi=this.getProcessInstance(task.getId());
	
			//查询流程参数
			ProcessBean arg=(ProcessBean) this.runtimeService.getVariable(pi.getId(), "arg");
			if(arg==null) break; 
//			params.put("processInstanceId", pi.getProcessInstanceId());
//			ProcessBean arg=processService.selectSingleProcessBean(params);
			//封装值对象
			arg.setProcessInstanceId(task.getProcessInstanceId());
			arg.setTaskId(task.getId());
			arg.setExecutionId(task.getExecutionId());
			result.add(arg);
		}
		return result;
	}


	@Override
	public PageInfo listCandidatePageInfo(String userId,List<String> processInstanceIds, Integer pageIndex, Integer pageSize) {
        
		return this.getPageInfo(createTaskVOList(this.listCandidateTasks(userId,processInstanceIds, pageIndex, pageSize)), this.countListCandidateTasks(userId,processInstanceIds));
	}

	@Override
	public PageInfo listAssigneePageInfo(String userId, Integer pageIndex, Integer pageSize) {
        return this.getPageInfo(createTaskVOList(this.listAssigneeTasks(userId, pageIndex, pageSize)), this.countListAssigneeTasks(userId));
	}

	@Override
	public long countListCandidateTasks(String userId,List<String> processInstanceIds) {
		if(processInstanceIds==null || processInstanceIds.size()==0) return 0;
		List<String> candidateGroups=this.candidateGroups(userId);

		if(candidateGroups==null ||candidateGroups.size()==0) return 0;

		return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).taskCandidateGroupIn(candidateGroups).count();
	}

	@Override
	public long countListAssigneeTasks(String userId) {
		
		return taskService.createTaskQuery().taskAssignee(userId).count();
	}

	@Override
	public List<Comment> getComments(String taskId) {
		ProcessInstance pi=this.getProcessInstance(taskId);
		List<Comment> comments=this.taskService.getProcessInstanceComments(pi.getId());
		
		return comments;
	}

	@Override
	public ProcessInstance getProcessInstance(String taskId) {
		Task task=this.taskService.createTaskQuery().taskId(taskId).singleResult();
		
		return this.runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId())
				.singleResult();
	}

	@Override
	public Task getFirstTask(String processInstanceId) {
		Task task=this.taskService.createTaskQuery()
				.processInstanceId(processInstanceId).orderByTaskCreateTime().desc()
				.singleResult();
		
		return this.taskService.createTaskQuery()
				.processInstanceId(processInstanceId).orderByTaskCreateTime().desc()
				.singleResult();
	}

	@Override
	public void setAssignee(String taskId, String userId) {
		taskService.setAssignee(taskId, userId);
		
	}

	@Override
	public void complete(String taskId, Map<String, Object> variables) {
		taskService.complete(taskId, variables);
		
	}


	@Override
	public void complete(String taskId) {
		this.taskService.complete(taskId);
		
	}

	@Override
	public void addComment(String taskId, String comment, String userId) {
		ProcessInstance pi=this.getProcessInstance(taskId);
		this.identityService.setAuthenticatedUserId(userId);
		//添加评论
		this.taskService.addComment(taskId, pi.getProcessInstanceId(), comment);
		
		
	}

	@Override
	public void completeWithCommentAndAudit(String taskId, String userId, Boolean audit, String comment) {
		this.identityService.setAuthenticatedUserId(userId);
		ProcessInstance pi=getProcessInstance(taskId);
		if(comment!=null&&!"".equals(comment)) this.taskService.addComment(taskId, pi.getId(), comment);
		if(audit!=null&&!"".equals(audit)){
			Map<String, Object> var=new HashMap<String, Object>();
			var.put("pass", Boolean.valueOf(audit));
			this.complete(taskId, var);
			return;
		}
		this.complete(taskId);
		
	}


	@Override
	public List<ProcessDefinition> selectAllProcessDefinitionsOrderByDesc(){
		List<ProcessDefinition> processDefinitions=repositoryService
				.createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().desc()
				.list();
		return processDefinitions;
	}

	@Override
	public void deleteLowerVersionProcessDefinitions(boolean cascade) {
		HashMap<String, ProcessDefinition> map = new HashMap<String,ProcessDefinition>();
		List<ProcessDefinition> processDefinitions=this.selectAllProcessDefinitionsOrderByDesc();
		for(ProcessDefinition pd:processDefinitions){
			ProcessDefinition old=map.get(pd.getKey());
			if(old==null){
				map.put(pd.getKey(), pd);
			}else if(pd.getVersion()>old.getVersion()){
				System.out.println("++++++++++++++++++++++++++++++++++++");
				System.out.println("流程KEY为："+pd.getKey()+",原来的版本为："+map.get(pd.getKey())+",新版本为："+pd.getVersion());
				System.out.println("现在删除旧版本,并保存新的版本");
				this.deleteDeploymentById(old.getDeploymentId(),cascade);
				map.put(pd.getKey(), pd);
			}else {
				System.out.println("++++++++++++++++++++++++++++++++++++");
				System.out.println("流程KEY为："+pd.getKey()+",原来的版本为："+map.get(pd.getKey())+",新版本为："+pd.getVersion());
				System.out.println("原来版本的比较高，现在删除当前流程");
				this.deleteDeploymentById(pd.getDeploymentId(),cascade);
			}
			
		}
		
	}

	@Override
	public void deleteDeploymentById(String deploymentId,boolean cascade) {
		try {
			if(this.getDeploymentById(deploymentId)!=null){
		
				repositoryService.deleteDeployment(deploymentId, cascade);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("已经存在已经启动的流程，无法删除");
		}
		
	}

	@Override
	public long countAllTaskList() {
		
		return taskService.createTaskQuery().count();
	}

	@Override
	public List<Task> selectAllTask(Integer pageIndex, Integer pageSize) {
		
		return taskService.createTaskQuery().listPage((pageIndex-1)*pageSize, pageSize);
	}

	@Override
	public PageInfo selectAllTaskPageInfo(Integer pageIndex, Integer pageSize) {

		return this.getPageInfo(createTaskVOList(this.selectAllTask(pageIndex, pageSize)), this.countAllTaskList());
	}

	@Override
	public void deleteTaskById(String taskId) {
		taskService.deleteTask(taskId);
		
	}



	@Override
	public PageInfo getPageInfo(List<?> list, long totalSize) {
		if(list==null||list.size()==0) return null;
		PageInfo pageInfo=new PageInfo(list);
		pageInfo.setTotal(totalSize);
		return pageInfo;
	}

	@Override
	public long countAllProcessInstance() {
		
		return runtimeService.createProcessInstanceQuery().count();
	}

	@Override
	public List<ProcessInstance> selectAllProcessInstances(Integer pageIndex, Integer pageSize) {
	
		return runtimeService.createProcessInstanceQuery().listPage((pageIndex-1)*pageSize, pageSize);
	}

	@Override
	public PageInfo selectAllProcessInstancesPageInfo(Integer pageIndex, Integer pageSize) {
		
		return this.getPageInfo(this.createProcessVOs(this.selectAllProcessInstances(pageIndex, pageSize)), this.countAllProcessInstance());
	}
	@Override
	public long countAllHistoryProcessInstance() {
		System.out.println("countAllHistoryProcessInstance="+historyService.createHistoricProcessInstanceQuery().count());
		return historyService.createHistoricProcessInstanceQuery().count();
	}

	@Override
	public List<HistoricProcessInstance> selectAllHistoryProcessInstances(Integer pageIndex, Integer pageSize) {
		
		return historyService.createHistoricProcessInstanceQuery().listPage((pageIndex-1)*pageSize, pageSize);
	}

	@Override
	public PageInfo selectAllHistoryProcessInstancesPageInfo(Integer pageIndex, Integer pageSize) {
		
		return this.getPageInfo(createHistoricProcessVOs(this.selectAllHistoryProcessInstances(pageIndex, pageSize)), this.countAllHistoryProcessInstance());
	}
	@Override
	public List<ProcessVO> createHistoricProcessVOs(List<HistoricProcessInstance> historicProcessInstances) {
		List<ProcessVO> result=new ArrayList<ProcessVO>();
		for (HistoricProcessInstance p : historicProcessInstances) {
			System.out.println("p.getId()="+p.getId());
			ProcessVO processVO=new ProcessVO();
			processVO.setId(p.getId());
			processVO.setName(p.getName());
			processVO.setProcessDefinitionName(p.getProcessDefinitionName());
			processVO.setProcessInstanceId(p.getSuperProcessInstanceId());
			processVO.setStartTime(DateUtil.formatTimesTampDate(p.getStartTime()));
			processVO.setProcessDefinitionVersion( p.getProcessDefinitionVersion());         
			processVO.setStartUserId(p.getStartUserId());
			
			
			result.add(processVO);
		}
		return result;
	}

	@Override
	public List<ProcessVO> createProcessVOs(List<ProcessInstance> processInstances) {
		List<ProcessVO> result=new ArrayList<ProcessVO>();
		
		for(ProcessInstance p:processInstances){
			ProcessVO processVO=new ProcessVO();
			processVO.setId(p.getId());
			processVO.setName(p.getName());
			processVO.setProcessDefinitionName(p.getProcessDefinitionName());
			processVO.setProcessInstanceId(p.getProcessInstanceId());
			processVO.setStartTime(DateUtil.formatTimesTampDate(p.getStartTime()));
			processVO.setProcessDefinitionVersion( p.getProcessDefinitionVersion());         
			processVO.setStartUserId(p.getStartUserId());
			processVO.setSuperExecutionId(p.getSuperExecutionId());
			
			result.add(processVO);
		}
		return result;
	}

	@Override
	public void deleteProcessInstance(String processInstanceId,String deleteReason) {
		
		if(this.selectProcessInstance(processInstanceId)!=null){
			runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
		}
		
		
	}

	@Override
	public Deployment getDeploymentById(String deploymentId) {
		
		return repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
	}

	@Override
	public String getDeploymentIdByBusinessKey(String businessKey) {
		List<Deployment> deployments=repositoryService.createDeploymentQuery()
				.processDefinitionKeyLike(businessKey)
				.list();
		long time=0;
		Deployment result=null;
		for (Deployment deployment : deployments) {
			System.out.println(deployment.getDeploymentTime());
			if(deployment.getDeploymentTime().getTime()>time){
				time=deployment.getDeploymentTime().getTime();
				result=deployment;
			}
		}
		//Deployment deployment=repositoryService.createDeploymentQuery().processDefinitionKey(businessKey).singleResult();
		
		return result.getId();
	}

	@Override
	public Task selectTask(String processInstanceId, String taskDefinitionKey) {
		
		return taskService.createTaskQuery().processInstanceId(processInstanceId)
				.taskDefinitionKey(taskDefinitionKey).singleResult();
	}

	@Override
	public void setTaskCandidateGroup(String processInstanceId, String taskDefinitionKey, String delegateTaskName) {
		System.out.println("processInstanceId="+processInstanceId+",taskDefinitionKey="+taskDefinitionKey+",delegateTaskName="+delegateTaskName);
		Task task=selectTask(processInstanceId, taskDefinitionKey);
		if(task==null){
			System.out.println("task==NULL");
		}else {
			System.out.println("task!=NULL");
		}
//		Group group=identityService.createGroupQuery().groupName(delegateTaskName).singleResult();
//		
//		if(taskService==null){
//			System.out.println("taskService==NULL");
//		}
//		System.out.println("task.getId()="+task.getId()+","+delegateTaskName+"---设置候选人组："+group.getId());
//		taskService.addCandidateGroup(task.getId(), group.getId());
		
	}

	@Override
	public Object getVariableFromProcessInstance(String processInstanceId,String variableName) {
		
		return this.runtimeService.getVariable(processInstanceId, variableName);
	}

	@Override
	public ProcessInstance selectProcessInstance(String processInstanceId) {
		
		return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}

	@Override
	public void setVariable(Task task,HashMap<String, Object> variables) {
		
		Execution execution=this.runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
		
		this.runtimeService.setVariables(execution.getId(), variables);
		
	}

	@Override
	public List<String> selectCandidateGroup(String taskId) {
		Task task=this.selectTask(taskId);
		List<String> result=new ArrayList<String>();
		List<IdentityLink> x=taskService.getIdentityLinksForTask(taskId);
		for (IdentityLink identityLink : x) {
			result.add(identityLink.getGroupId());
		}

		return result;
	}

	@Override
	public Task selectTask(String taskId) {
		
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	@Override
	public long countAllHistoricTaskInstancesByUserId(String userId) {
		
		return historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).count();
	}

	@Override
	public List<HistoricTaskInstance> listAllHistoricTaskInstancesByUserId(String userId,Integer pageIndex,
			Integer pageSize) {
		
		
		return historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).listPage((pageIndex-1)*pageSize, pageSize);
	}

	@Override
	public PageInfo listAllHistoricTaskInstancesPageInfoByUserId(String userId, Integer pageIndex, Integer pageSize) {

		return this.getPageInfo(createAllHistoricProcess(this.listAllHistoricTaskInstancesByUserId(userId, pageIndex, pageSize)), this.countAllHistoricTaskInstancesByUserId(userId));
	}

	@Override
	public List<ProcessBean> createAllHistoricProcess(List<HistoricTaskInstance> historicTaskInstances) {
		List<ProcessBean> result=new ArrayList<ProcessBean>();
		HashMap<String, Object> params=new HashMap<String, Object>();
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			params.put("processInstanceId", historicTaskInstance.getProcessInstanceId());
			ProcessBean processBean=processService.selectSingleProcessBean(params);
			if(processBean==null) {
				ProcessBean processBean2=new ProcessBean();
				processBean2.setTitle("相关流程已经删除了");
				result.add(processBean2);
				continue;
			};
			processBean.setTaskId(historicTaskInstance.getId());
			result.add(processBean);
		}
		return result;
	}

	@Override
	public void withdrawTask(String processInstanceId,String userId) {
		HashMap<String, Object> variables = new HashMap<String,Object>();
		variables.put("pass", "false");

		Task fTask=this.getFirstTask(processInstanceId);
		
		this.complete(fTask.getId(), variables);

	}

	@Override
	public HistoricTaskInstance selectSingleHistoricTaskInstance(String userId, String processInstanceId) {
		List<HistoricTaskInstance> historicTaskInstances=historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
				.taskAssignee(userId).orderByTaskCreateTime().desc().list();
		if(historicTaskInstances!=null && historicTaskInstances.size()>0) return historicTaskInstances.get(0);
		return null;
	}

	@Override
	public boolean canIwithdrawTask(String processInstanceId, String userId) {
		
        HistoricTaskInstance historicTaskInstance=this.selectSingleHistoricTaskInstance(userId, processInstanceId);
		
		System.out.println("his.TaskDefinitionKey"+historicTaskInstance.getTaskDefinitionKey());
		
		Task fTask=this.getFirstTask(processInstanceId);
		if(fTask==null) return false;
		System.out.println("fTask="+fTask.getTaskDefinitionKey());
		//默认当前流程的上一个流程名为(当前为usertask2)usertask1
		String nextTaskDefinitionKey="";
		
		if(RegexUtils.isMatch(historicTaskInstance.getTaskDefinitionKey(), "usertask(.*)")){
			Integer num=Integer.parseInt(RegexUtils.getFirstGroup(historicTaskInstance.getTaskDefinitionKey(), "usertask(.*)"));
			nextTaskDefinitionKey="usertask"+(num+1);
			System.out.println("nextTaskDefinitionKey="+nextTaskDefinitionKey);
		}
		if(nextTaskDefinitionKey.equals(fTask.getTaskDefinitionKey())) return true;
		return false;
	}

	










	
}
