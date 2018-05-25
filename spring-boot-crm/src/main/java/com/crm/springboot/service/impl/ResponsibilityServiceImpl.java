package com.crm.springboot.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.type.NTextType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import com.crm.springboot.mapper.EvaluationMapper;
import com.crm.springboot.pojos.GroupTable;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkPost;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.RegexUtils;
@Service
@Component
public class ResponsibilityServiceImpl implements ResponsibilityService{
    @Autowired
    private EvaluationMapper evaluationMapper;
    @Autowired UserService userService;
    @Autowired
    private SysPowerService sysPowerService;
    @Autowired
    private ActivitiService activitiService;
	@Override
	public void saveProject(Project project) {

		project.setCreateTime(new Date());
		evaluationMapper.saveProject(project);
		
	}
	@Override
	public List<Project> selectAllProject(HashMap<String, Object> params){
		
		return evaluationMapper.selectAllProject(params);
	}
	@Override
	public void saveMark(Mark mark) {
		
		mark.setCreateTime(new Date());
		
		evaluationMapper.saveMark(mark);
		
	}

	@Override
	public Project selectProjectWithProjectId(String projectId) {
		
		return evaluationMapper.selectProjectWithProjectId(projectId);
	}
	@Override
	public List<Responsibility> selectAllResponsibility(HashMap<String, Object> params) {
		
		return evaluationMapper.selectAllResponsibility(params);
	}
	@Override
	public void saveResponsibility(Responsibility responsibility) {
		
		evaluationMapper.saveResponsibility(responsibility);
	}
	@Override
	public List<Mark> selectMarkWithProjectId(Serializable projectId) {
		
		return evaluationMapper.selectMarkWithProjectId(projectId);
	}
	@Override
	public void updateProject(Project project) {
		evaluationMapper.updateProject(project);
		
	}
	

	@Override
	public void saveEvaluation(Evaluation evaluation) {
		evaluationMapper.saveEvaluation(evaluation);
		
	}
	@Override
	public boolean canIStartEvaluationProcess(User user) {
	    String[] canList={"普通员工","中层副职","中层正职"};
	    
	   
    	if( Arrays.asList(canList).contains(user.getPost().getName())){
	    	
	    	return true;
	    }
	
	    
		return false;
	}
	@Override
	public void setCandidate(String delegateTaskName) {
		
		if("部门主任审批".equals(delegateTaskName)){
			
		}
	}
	@Override
	public List<Evaluation> selectAllEvaluation(HashMap<String, Object> params) {
		
		return evaluationMapper.selectAllEvaluation(params);
	}
	@Override
	public Evaluation selectEvaluationWithProcessInstanceId(String processInstanceId) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("processInstanceId",processInstanceId);
		List<Evaluation> evaluations=this.selectAllEvaluation(params);
		if(evaluations==null|| evaluations.size()==0)return null;
		return evaluations.get(0);
	}
	@Override
	public void updateEvaluation(Evaluation evaluation) {
		evaluationMapper.updateEvaluation(evaluation);
		
	}
    public void set(){
    	System.out.println("=====================ok==============");
    }
    @Override
	public String getTitle(Date startDate, Date endDate) {
		long nd = 1000 * 24 * 60 * 60;
		long diff=(endDate.getTime()-startDate.getTime())/nd;
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(startDate);
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH);
	    if(diff<32){
	    	
	    	return year+"年"+month+"月份-月度考核";
	    }
	    if(diff<186){
	    	
	    	return year+"年"+(month>6?"下半年":"上半年")+"-半年考核";
	    }
	    if(diff<367){
	    	return year+"年"+"-年度考核";
	    }
		return null;
	}
	@Override
	public String getBusinessType(Date startDate, Date endDate) {
		long nd = 1000 * 24 * 60 * 60;
		long diff=(endDate.getTime()-startDate.getTime())/nd;
		Calendar calendar=Calendar.getInstance();
		
	    if(diff<32){
	    	
	    	return ProcessType.EVALUCATION_MONTH;
	    }
        if(diff<186) return ProcessType.EVALUCATION_HALFYEAR;
	    if(diff<367){
	    	return ProcessType.EVALUCATION_FULLYEAR;
	    }
		return null;
	}
	@Override
	public List<String> selectEvaluationBusinessType(String taskType) {
		List<String> result=new ArrayList<String>();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH);
		if(taskType.equals(ProcessType.EVALUCATION_MONTH)){
			result.add(year+"年"+month+"月份-月度考核");
		}
		if (taskType.equals(ProcessType.RESPONSIBILITY_TYPE)) {
			result.add(year+"年"+ProcessType.RESPONSIBILITY_TYPE);
			for (int i =-1; i <2; i++) {
				result.add((year+i)+"年"+ProcessType.RESPONSIBILITY_TYPE);
			}
		}
		return result;
	}
	@Override
	public HashMap<String, Object> getStartDateStrAndEndDateDateStr(String taskTitle) {
		HashMap<String, Object> result=new HashMap<String, Object>();
		String monthAssess="(.*?)年(.*?)月份";
		String responsibility="(.*?)年一线干部责任清单";
		if(RegexUtils.isMatch(taskTitle, monthAssess)){
			Integer year=Integer.valueOf(RegexUtils.getIndexGroup(1, taskTitle, monthAssess));
			Integer month=Integer.valueOf(RegexUtils.getIndexGroup(2, taskTitle, monthAssess));
			String startDate=DateUtil.formatDefaultDate(DateUtil.getFirstDayOfMonth(year, month));
			String endDate=DateUtil.formatDefaultDate(DateUtil.getLastDayOfMonth(year, month));
			result.put("startDate", startDate);
			result.put("endDate", endDate);
		}
		if(RegexUtils.isMatch(taskTitle, responsibility)){
			Integer year=Integer.valueOf(RegexUtils.getIndexGroup(1, taskTitle, responsibility));
			String startDate=DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(year));
			String endDate=DateUtil.formatDefaultDate(DateUtil.getLastDayOfYear(year));
			result.put("startDate", startDate);
			result.put("endDate", endDate);
		}
		return result;
	}

	@Override
	public Integer getTotalMarkOfProjects(List<Project> projects) {
		Integer totalMark=0;
		for (Project project : projects) {
			totalMark+=this.getTotalMark(project);
		}
		return totalMark;
	}
	@Override
	public Integer getTotalMark(Project project) {
		
		return this.getTotalMark(project.getMarks());
	}
	@Override
	public Integer getTotalMark(List<Mark> marks) {
		Integer totalMark=0;
		
		for (Mark mark : marks) {
			if(mark!=null){
				totalMark+=Integer.valueOf(mark.getMarkNumber());
			}
		}
		return totalMark;
	}
	@Override
	public void updateMark(Mark mark) {
		evaluationMapper.updateMark(mark);
		
	}
	@Override
	public void deleteMark(String markId) {
		evaluationMapper.deleteMark(markId);
		
	}
	@Override
	public Mark selectMarkByMarkId(String markId) {
		
		return evaluationMapper.selectMarkByMarkId(markId);
	}
	@Override
	public void deleteProject(String projectId) {
		evaluationMapper.deleteProject(projectId);
		
	}
	@Override
	public Evaluation selectSingleEvaluation(HashMap<String, Object> params) {
		List<Evaluation> evaluations=this.selectAllEvaluation(params);
		if (evaluations==null||evaluations.size()==0) {
			return null;
		}
		return evaluations.get(0);
	}
	@Override
	public LinkedList<String> selectEvaluationType(String taskType) {
		
		String[] type={
				ProcessType.RESPONSIBILITY_TYPE,
				ProcessType.EVALUCATION_MONTH,
				ProcessType.EVALUCATION_FULLYEAR};
		LinkedList<String> result=new LinkedList<String>();
        result.add(ProcessType.RESPONSIBILITY_TYPE);
        result.add(ProcessType.EVALUCATION_MONTH);
        result.add(ProcessType.EVALUCATION_FULLYEAR);
		if(result.contains(taskType)){
			result.remove(taskType);
			result.addFirst(taskType);
			
		}
		return result;
	}
	@Override
	public void updateResponsibility(Responsibility responsibility) {
		evaluationMapper.updateResponsibility(responsibility);
		
	}
	@Override
	public Responsibility selectSingleResponsibility(HashMap<String, Object> params) {
		List<Responsibility> responsibilities=this.selectAllResponsibility(params);
		if(responsibilities==null||responsibilities.size()==0)return null;
		return responsibilities.get(0);
	}
	@Override
	public void organizationAssessForMonth(Evaluation evaluation) {
		String assess=evaluation.getOverseerEvaluation();		
		String reason="";
		String markNumber="";
		switch (assess) {
		case "优秀":
			reason="加分项:月度考核定评为优（1分）";
			markNumber="1";
			break;
		case "基本合格":
			reason="减分项:月度考核定评为基本合格扣（1分），不合格扣（3分）";	
			markNumber="-1";
			break;
		case "不合格":
			reason="减分项:月度考核定评为基本合格扣（1分），不合格扣（3分）";	
			markNumber="-3";
			break;

		default:
			break;
		}
       if(!"".equals(reason)){
    	    this.saveProjectAndMark(String.valueOf(evaluation.getProcessBean().getUser().getId()), evaluation.getStartDate(),
    	    		evaluation.getEndDate(),
    	    		"月度考核自动加减分项", reason, markNumber);
       }
	}
	@Override
	public void saveProjectAndMark(String userId,Date startDate,Date endDate,String projectContent,String markreason,String marknumber) {
		Project project=new Project();
   		project.setStartDate(startDate);
   		project.setEndDate(endDate);
   		project.setCompleted("1");
   		project.setCreateTime(new Date());
   		
   		project.setUserId(userId);
   		project.setProjectContent(projectContent);
   		this.saveProject(project);
   		
   		
		Mark mark=new Mark();
		mark.setProjectId(project.getProjectId());
		mark.setAccordingly(markreason);
		mark.setCreateTime(new Date());
		mark.setMarkNumber(marknumber);
		mark.setMarkReason(markreason);
		mark.setStartDate(startDate);
		mark.setEndDate(endDate);
		mark.setUserId(userId);
		mark.setChecked("1");
		this.saveMark(mark);
		
	}
	@Override
	public String getIdentity(Evaluation evaluation,User user) {
		  //获取当前候选组，传递给页面，使对应的内容可以编辑
		  String currentCandidateGroup=evaluation.getProcessBean().getCurrentCandidateGroup();
		  if(currentCandidateGroup==null||"".equals(currentCandidateGroup)) return "employee";
		  //当前用户是否包含候选用户组
		  List<String> candidateGroups=activitiService.candidateGroups(String.valueOf(user.getId()));
		  
		  if(!candidateGroups.contains(currentCandidateGroup)) return "";

		  
		  
		  GroupTable groupTable=sysPowerService.selectGroupWithGroupId(currentCandidateGroup);
		  if(groupTable.getGroupname().contains("考")) return "overseer";
		  if(groupTable.getGroupname().contains("部门主任")||groupTable.getGroupname().contains("分管领导")) return "leader";
		 
		return "";
	}
	@Override
	public String selectTotalMarksTillNow() {
		
		return null;
	}
	@Override
	public List<Mark> selectTotalMarkAndUser(HashMap<String, Object> params) {
		
		return evaluationMapper.selectTotalMarkAndUser(params);
	}
	@Override
	public List<Mark> selectAllMarkAsType(HashMap<String, Object> params) {
		
		return evaluationMapper.selectAllMarkAsType(params);
	}
	@Override
	public void updateMarksAsChecked(HashMap<String, Object> params) {
		List<Mark> marks=this.selectAllMarkAsType(params);
		for (Mark mark : marks) {
			System.out.println("mark="+mark.toString());
			mark.setChecked("1");
			this.updateMark(mark);
		}
	}




}
