package com.crm.springboot.service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.runtime.Execution;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessEntity;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.User;

public interface ProcessService extends BaseService<Process>{
    String getCandidateGroupOfAssessGroupWithDeptname(String deptName);
	List<ProcessBean> selectAllProcess(HashMap<String , Object> params);
	List<ProcessBean> selectAllProcessWithUserNameAndPhone(HashMap<String , Object> params);
	ProcessBean selectSingleProcessBean(HashMap<String , Object> params);
	List<String> selectAllProcessInstanceIds(HashMap<String , Object> params);
	List<String> selectAllProcessInstanceIds(String deptName,String titleLike);
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
	//检索用户相关的考核组待办任务
	List<String> getProcessInstanceIdsWithAssessmentGroup(User user,HashMap<String, Object> params);
	//检索领导待办任务
	List<String> getProcessInstanceIdsWithLeader(User user,HashMap<String, Object> params);
	List<User> selectAllUsersWhoUnsubmittedProcess(HashMap<String, Object> params);
	/**
	 * 查询所有未按时提交指定表格的员工信息
	 * @param params
	 * @return
	 */
	List<User> selectUsersWhoUnsubmittedProcess(HashMap<String, Object> params);
	//查询已经提交表格的人数
	Integer selectCountUserNumberWhichSubmittedProcess(HashMap<String, Object> params);
	Integer selectCountUserNumberWhichSubmittedProcess(String deptName,String titleLike);
	Integer selectCountUserNumberWhichSubmittedProcess(String[] deptNames,String titleLike);
	
	//查询完结的流程数
	Integer selectCountProcess(HashMap<String, Object> params);
	
	Integer selectCountProcess(String deptName,String titleLike,String completed);
	Integer selectCountProcess(String deptName,String deptLevel,String titleLike,String completed);
	Integer selectCountProcess(String deptName,String deptLevel,String titleLike,String completed,List<String> currentCandidateGroupNotIn);
	Integer selectCountProcess(String[] deptNames, String titleLike,String completed);
	Integer selectCountProcess(String[] deptNames, String titleLike,String completed,String currentCandidateGroup);
	//查询所有不同的候选用户组
	List<String> selectDistinctCurrentCandidateGroup(HashMap<String, Object> params);
	
	List<String> selectDistinctCurrentCandidateGroup(String[] deptNames, String titleLike,String completed,String committed);
	//查询一个字段
	List<String> selectSingleFieldFromProcess(HashMap<String, Object> params);
	//根据考核组查询考核下不重复的部门名字
	List<String> selectDistinctDeptWithGroupName(String groupName,String titleLike);
	//获取子部门名
	String[] selectDeptNames(String deptLevel,String deptName);
/**
 * ***************************************************evaluation***************************************************
 */

	List<ProcessEntity> selectAllEvaluationFromProcess(HashMap<String, Object> params);
	
	Integer getSubMittedNumber(String deptName,String deptLevel,String titleLike);
	//审批用时占比
	Integer getApprovalTimeRate(String titleLike,List<String> deptNames,String taskName,String groupName);
	//查询月度考核需要交表的人数
	Integer getNumberHaveToSubmittedTheMonthAssess(String deptName,String deptLevel);
	Integer getNumberHaveToSubmittedTheMonthAssess(Integer deptId,String deptLevel);
	
	/**
	 * ************************************查询activiti************************************************************
	 */
	List<ProcessBean> selectHistoryProcessFromActHiTaskInst(HashMap<String, Object> params);
	List<ProcessBean> selectProcessFromActRuTask(HashMap<String, Object> params);
}
