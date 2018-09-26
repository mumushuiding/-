package com.crm.springboot.service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessEntity;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.MonthAssessCfgPojo;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;
import com.crm.springboot.pojos.user.DeptType;
import com.crm.springboot.pojos.user.User;

public interface ResponsibilityService {
	
 //根据bpmn流程图中的审批人，设置审批候选组
	void setCandidate(String delegateTaskName);
	/**
	 * 延误提交和限期未提交的，自动扣分
	 * @throws JAXBException 
	 */
		
	
    public void deductWithMonthProcess(MonthAssessCfgPojo monthAssessCfgPojo);
    public void deductWithMonthProcessDelaySubmittedWithUser(User user,Date startDate,Date endDate,String markReason);
    public void deductWithMonthProcessUnsubmittedBeforeDeadLineWithUser(User user,Date startDate,Date endDate,String markReason);
    public boolean isDeduct(User user,String markReason,Date startDate,Date endDate);
    public String getDegreeOfUrgencyWithUnsubmittedAssessForm();
    public boolean isOverTheDeadLineForSubmittedMonthProcess();
    public String getTargetGroupWithMonthAssess();
    public void updateMonthAssessCfg(String cronExpression);
    /**
     * 判断是否已经过了月度考核交表日期
     */

    /**
	 * 查询有哪些用户还未填写月度考核表
	 * @throws JAXBException 
	 */
	public List<User> selectAllUsersWhoUnsubmittedMonthProcess(MonthAssessCfgPojo monthAssessCfgPojo);
/**
 *******************************Evaluation*********************************************************
 */
	boolean  canIStartEvaluationProcess(User user);
	void saveEvaluation(Evaluation evaluation);
	List<Evaluation> selectAllEvaluation(HashMap<String , Object> params);
	List<Evaluation> selectAllEvaluationWithProcess(HashMap<String, Object> params);
	Evaluation selectSingleEvaluationWithProcess(HashMap<String, Object> params);
	Evaluation selectSingleEvaluation(HashMap<String , Object> params);
	Evaluation selectSingleEvaluation(String phone,String sparation);
	Evaluation selectSingleEvaluationWithProcess(String phone,String sparation);
	Evaluation selectSingleEvaluationAsResultType(String phone,String sparation);
	Evaluation selectSingleEvaluationAsResultType(HashMap<String , Object> params);
	Evaluation selectEvaluationWithProcessInstanceId(String processInstanceId);
	void updateEvaluation(Evaluation evaluation);

	//月考组织评价自动加减分
	void organizationAssessForMonth(Evaluation evaluation);
	void saveProjectAndMark(String userId,Date startDate,Date endDate,String projectContent,String markreason,String marknumber,Integer completed);
	void saveProjectAndMarkIngoreExisted(String userId,Date startDate,Date endDate,String projectContent,String markreason,String marknumber,Integer completed);
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
	String getIdentity(ProcessBean processBean,User user);
	String getIdentity(String processInstanceId,User user);
	//查询截止目前每月分数和
	String selectTotalMarksTillNow();
	//返回evaluation和user
	List<ProcessEntity> selectEvaluationWithUser(HashMap<String, Object> params);
	//获取半年或者全年考核表的标题
	String getTitleLikeForYear(String taskType,String times);
	//获取表单类型
	String getBusinessType(String taskType);
	//查询半年或者全年考核审批结束的是否超过指定的rate
	boolean isOverRate(HashMap<String, Object> params,Double rate);
	//计算半年或者全年考核总分
	double getTotalMarkOfEvaluation(Evaluation evaluation);

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
	Double getTotalMarkOfProjects(List<Project> projects);
	Double getTotalMarkOfAllMonthAssessment(List<Project> projects);
	double getTotalMarkOfAllMonthAssessmentWithBasemark(Evaluation evaluation);
	//获取基础分
	Double getBaseMarkOfAssessment();
	Double getTotalMark(Project project);
	Double getTotalMark(List<Mark> marks);
	void saveProject(Project project);
	void updateProject(Project project);
	List<Project> selectAllProject(HashMap<String, Object> params);
	Project selectProjectWithProjectId(String projectId);
	void deleteProject(String projectId);
	boolean canIDeleteProject(String projectId);
	/**
	 * 查询project
	 * @param params
	 * @return
	 */
	List<Project> selectProject(HashMap<String, Object> params);

	/**
	 * 
	 * @param whereClause,userId
	 */
	void deleteFromProject(HashMap<String, Object> params);
	
	List<Project> selectProjectWithProjectEntity(Project project);
	Project selectSingleProjectWithProjectEntity(Project project);
	
/**
 *******************************Mark*********************************************************
 */	
	//查询每个用户的项目总分
	List<Mark> selectTotalMarkAndUser(HashMap<String, Object> params);
	
	List<Mark> selectTotalMarkWithAllUser(HashMap<String, Object> params);
	
	List<Mark> selectAllMarkAsType(HashMap<String, Object> params);
	
	List<Mark> selectMarkWithMarkEntity(Mark mark);
	
	Mark selectSingleMarkWithMarkEntity(Mark mark);
	
	void updateMarksAsChecked(HashMap<String, Object> params);
	
	int countTotalMarkAndUser(HashMap<String, Object> params);
	
	//计算总分
	Double selectTotalMark(HashMap<String, Object> params);
	Double selectTotalMark(Integer userId,String checked,String startDate,String endDate);
	//获取指定用户有延时扣分的所有月分,最近12个月
	List<String> selectAllMonthWhichDelaySubmitMonthAssessApply(Integer userId,int numbers);
	//删除指定月份的延时未交扣分 date格式:2018年8月
	void deleteAllMonthWhichDelaySubmitMonthAssessApply(String date,Integer userId);
}
