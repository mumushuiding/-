package com.crm.springboot.mapper;

import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessEntity;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.user.User;

public interface ProcessMapper extends BaseMapper<ProcessBean>{
	List<ProcessBean> selectAllProcess(HashMap<String , Object> params);
	
	List<ProcessBean> selectAllProcessWithUserNameAndPhone(HashMap<String , Object> params);
	
	List<String> selectAllProcessInstanceIds(HashMap<String , Object> params);
	void deleteProcessByProcessInstanceIds(String[] ids);
	void updateProcess(ProcessBean processBean);
	/**
	 * 查询所有未按时提交指定表格的员工信息,带部门
	 * @param params
	 * @return
	 */
	List<User> selectAllUsersWhoUnsubmittedProcess(HashMap<String, Object> params);
	/**
	 * 查询所有未按时提交指定表格的员工信息
	 * @param params
	 * @return
	 */
	List<User> selectUsersWhoUnsubmittedProcess(HashMap<String, Object> params);
	//查询已经提交表格的人数
	Integer selectCountUserNumberWhichSubmittedProcess(HashMap<String, Object> params);
	//查询完结的流程数
	Integer selectCountProcess(HashMap<String, Object> params);
	
	//查询所有不同的候选用户组
	List<String> selectDistinctCurrentCandidateGroup(HashMap<String, Object> params);
	List<String> selectSingleFieldFromProcess(HashMap<String, Object> params);

	List<ProcessEntity> selectAllEvaluationFromProcess(HashMap<String, Object> params);
	
	/**
	 * ************************************查询activiti************************************************************
	 */
	List<ProcessBean> selectHistoryProcessFromActHiTaskInst(HashMap<String, Object> params);
	List<ProcessBean> selectProcessFromActRuTask(HashMap<String, Object> params);
}
