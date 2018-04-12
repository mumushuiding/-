package com.crm.springboot.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.pojos.GroupTable;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.utils.FileUtils;
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
	public void viewProcessImage(Serializable deploymentId,String resourceName,OutputStream out) {
		
		InputStream in = null;
		try {
			in=repositoryService.getResourceAsStream(String.valueOf(deploymentId), resourceName);
			byte[] b=new byte[1024];
			for(int len=-1;(len=in.read(b))!=-1;){
				out.write(b, 0, len);
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
	   
	}

	@Override
	public ProcessInstance startProcess(String processDefinitionKey) {
		
		ProcessDefinition pd=repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).singleResult();
		ProcessInstance pi=runtimeService.startProcessInstanceByKey(pd.getKey());
		return pi;
	}





	


}
