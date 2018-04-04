package com.crm.springboot.service.impl;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.mapper.LeaveInfoMapper;
import com.crm.springboot.pojos.LeaveInfo;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.LeaveInfoService;
@Service("leaveInfoService")
public class LeaveInfoServiceImpl implements LeaveInfoService{
	@Autowired
	private LeaveInfoMapper leaveInfoMapper;
	@Autowired
	private ActivitiService activitiService;
	@Override
	public <T> T save(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void deleteById(Serializable id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T update(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveLeaveInfo(String msg) {
		System.out.println("============新增请假表");
		LeaveInfo leaveInfo=new LeaveInfo();
		leaveInfo.setLeaveMsg(msg);
		String id=UUID.randomUUID().toString();
		leaveInfo.setId(id);
		leaveInfoMapper.save(leaveInfo);
		activitiService.startProcess("leaveProcess", id);
		
	}

	@Override
	public List<LeaveInfo> getById(String userId) {
		List<LeaveInfo> leaveInfoList=new ArrayList<LeaveInfo>();
		//待审批的流程
		List<Task> taskList=activitiService.findTaskByUserId(userId);
		for(Task task:taskList){
			//获得业务流程的bussinessKey
			String buinessKey=activitiService.getProcessInstance(task).getBusinessKey();
			LeaveInfo leaveInfo=leaveInfoMapper.getById(buinessKey);
			leaveInfo.setTaskId(task.getId());
			leaveInfoList.add(leaveInfo);
		}
		return leaveInfoList;
	}

	@Override
	public void completeTaskById(String taskId, String userId, String audit) {
		activitiService.completeTaskByUser(taskId, userId, audit);
		
	}
	/**
	 * 查找上级审批人
	 * @param execution
	 * @return
	 */
	public List<String> findApprovers(DelegateExecution execution){
		return Arrays.asList("p1","p2");
	}
	/**
	 * 查询相关人事经理
	 * @param execution
	 * @return
	 */
	public List<String> findHr(DelegateExecution execution){
		return Arrays.asList("hr1","hr2");
	}
	public void changeStatus(DelegateExecution execution,String status){
		String key=execution.getProcessInstanceBusinessKey();
		LeaveInfo leaveInfo=leaveInfoMapper.getById(key);
		leaveInfo.setStatus(status);
		leaveInfoMapper.update(leaveInfo);
	}
	@Override
	public <T> T getById(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

}
