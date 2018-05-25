package com.crm.springboot.service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;
import com.crm.springboot.pojos.user.User;

public interface ResponsibilityService {
	
   //根据bpmn流程图中的审批人，设置审批候选组
	void setCandidate(String delegateTaskName);

   
/**
 *******************************Evaluation*********************************************************
 */
	boolean  canIStartEvaluationProcess(User user);
	void saveEvaluation(Evaluation evaluation);
	List<Evaluation> selectAllEvaluation(HashMap<String , Object> params);
	Evaluation selectSingleEvaluation(HashMap<String , Object> params);
	Evaluation selectEvaluationWithProcessInstanceId(String processInstanceId);
	void updateEvaluation(Evaluation evaluation);
	//月考组织评价自动加减分
	void organizationAssessForMonth(Evaluation evaluation);
	void saveProjectAndMark(String userId,Date startDate,Date endDate,String projectContent,String markreason,String marknumber);
	List<String> selectEvaluationBusinessType(String taskType);
	List<String> selectEvaluationType(String taskType);
	/**
	 * taskTitle表示的是业务的时间，比如2018年5月份，2018年上半年等，结果转换成时间字符串，比如 2018年5月份 就转换成2018-5-1~2018-5-31
	 * @param taskTitle
	 * @return
	 */
	HashMap<String, Object> getStartDateStrAndEndDateDateStr(String taskTitle);
	//获取当前审批用户的身份（employee,leader,overseer）
	String getIdentity(Evaluation evaluation,User user);
	//查询截止目前每月分数和
	String selectTotalMarksTillNow();
/**
 *******************************Responsibility*********************************************************
 */
	List<Responsibility> selectAllResponsibility(HashMap<String , Object> params);
	void saveResponsibility(Responsibility responsibility);
	void updateResponsibility(Responsibility responsibility);
	Responsibility selectSingleResponsibility(HashMap<String , Object> params);
	//********************mark********************************
	void saveMark(Mark mark);
	void updateMark(Mark mark);
	void deleteMark(String markId);
	Mark selectMarkByMarkId(String markId);
	List<Mark> selectMarkWithProjectId(Serializable projectId);
	
	String getTitle(Date startDate,Date endDate);
	String getBusinessType(Date startDate,Date endDate);
	
/**
 *******************************Project*********************************************************
 */
	Integer getTotalMarkOfProjects(List<Project> projects);
	Integer getTotalMark(Project project);
	Integer getTotalMark(List<Mark> marks);
	void saveProject(Project project);
	void updateProject(Project project);
	List<Project> selectAllProject(HashMap<String, Object> params);
	Project selectProjectWithProjectId(String projectId);
	void deleteProject(String projectId);
	
/**
 *******************************Mark*********************************************************
 */	
	//查询每个用户的项目总分
	List<Mark> selectTotalMarkAndUser(HashMap<String, Object> params);
	
	List<Mark> selectAllMarkAsType(HashMap<String, Object> params);
	
	void updateMarksAsChecked(HashMap<String, Object> params);
}
