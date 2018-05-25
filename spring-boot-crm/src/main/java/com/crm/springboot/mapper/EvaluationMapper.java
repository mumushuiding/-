package com.crm.springboot.mapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;

public interface EvaluationMapper {
	//*********************evaluation******************************
	List<Evaluation> selectAllEvaluation(HashMap<String, Object> params);
	void updateEvaluation(Evaluation evaluation);
	void saveEvaluation(Evaluation evaluation);
	
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
	
	
	//*********************mark******************************
	void saveMark(Mark mark);
	void updateMark(Mark mark);
	void deleteMark(String markId);
	Mark selectMarkByMarkId(String markId);
	List<Mark> selectMarkWithProjectId(Serializable projectId);
	//查询每个用户的项目总分
	List<Mark> selectTotalMarkAndUser(HashMap<String, Object> params);
	List<Mark> selectAllMarkAsType(HashMap<String, Object> params);
}
