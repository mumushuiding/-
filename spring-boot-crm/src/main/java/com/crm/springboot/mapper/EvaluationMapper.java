package com.crm.springboot.mapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.ProcessEntity;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;

public interface EvaluationMapper {
	//*********************evaluation******************************
	List<Evaluation> selectAllEvaluation(HashMap<String, Object> params);
	List<Evaluation> selectAllEvaluationWithResultType(HashMap<String, Object> params);
	List<Evaluation> selectAllEvaluationWithProcess(HashMap<String, Object> params);
	void updateEvaluation(Evaluation evaluation);
	void saveEvaluation(Evaluation evaluation);
	//返回evaluation和user
	List<ProcessEntity> selectEvaluationWithUser(HashMap<String, Object> params);
	
	//*********************responsibility******************************
	List<Responsibility> selectAllResponsibility(HashMap<String , Object> params);
	void saveResponsibility(Responsibility responsibility);
	void updateResponsibility(Responsibility responsibility);
	
	//*********************project******************************
	void saveProject(Project project);
	void deleteProject(String projectId);
	List<Project> selectAllProject(HashMap<String, Object> params);
	void updateProject(Project project);
	Project selectProjectWithProjectId(Serializable projectId);
	List<Project> selectProject(HashMap<String, Object> params);
	List<Project> selectProjectWithProjectEntity(Project project);
	
	/**
	 * 
	 * @param whereClause
	 */
	void deleteFromProject(HashMap<String, Object> params);
	
	//*********************mark******************************
	
	List<String> selectSingleColumnFromMark(HashMap<String, Object> params);
	
	void saveMark(Mark mark);
	void updateMark(Mark mark);
	void deleteMark(String markId);
	Mark selectMarkByMarkId(String markId);
	List<Mark> selectMarkWithProjectId(Serializable projectId);
	List<Mark> selectMarkWithMarkEntity(Mark mark);
	//查询每个用户的项目总分
	List<Mark> selectTotalMarkAndUser(HashMap<String, Object> params);
	List<Mark> selectTotalMarkWithAllUser(HashMap<String, Object> params);
	List<Mark> selectAllMarkAsType(HashMap<String, Object> params);
	int countTotalMarkAndUser(HashMap<String, Object> params);
	//计算总分
	Double selectTotalMark(HashMap<String, Object> params);
}
