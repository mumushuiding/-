package com.crm.springboot.service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.runtime.Execution;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.user.User;

public interface ProcessService extends BaseService<Process>{
    
	List<ProcessBean> selectAllProcess(HashMap<String , Object> params);
	ProcessBean selectSingleProcessBean(HashMap<String , Object> params);
	List<String> selectAllProcessInstanceIds(HashMap<String , Object> params);
	void deleteProcessByProcessInstanceIds(String[] ids);
	
	
	
	void updateProcess(ProcessBean processBean);
	
	void setCompleted(Execution execution);
	//获得目前流程的候选人组x，并断当前操作人是否拥有x,并返回判断结果
	boolean isUserHasTheCandidateGroup(String userId,String processInstanceId);
	String isUserHasTheCandidateGroup(String userId,String processInstanceId,String referer) throws UnsupportedEncodingException;
	/**
	 * 判断一个用户是否已经存在名为title的流程，主要是为了防止重复提交月度考核
	 * @param userId
	 * @param title
	 * @return
	 */
	boolean isProcessExists(String userId,String title);
	//检索用户相关的所有流程
	List<String> getProcessInstanceIdsWithUser(User user,HashMap<String, Object> params);
	
	List<User> selectAllUsersWhoUnsubmittedProcess(HashMap<String, Object> params);
}
