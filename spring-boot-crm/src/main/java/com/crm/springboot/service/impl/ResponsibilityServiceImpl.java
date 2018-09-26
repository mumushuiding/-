package com.crm.springboot.service.impl;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.crm.springboot.config.AvailableSettings;
import com.crm.springboot.mapper.EvaluationMapper;
import com.crm.springboot.pojos.Dictionary;
import com.crm.springboot.pojos.GroupTable;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessEntity;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.MonthAssessCfgPojo;
import com.crm.springboot.pojos.assess.Project;
import com.crm.springboot.pojos.assess.Responsibility;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.DeptType;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.DictionaryService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.RegexUtils;
@Service
@Component
public class ResponsibilityServiceImpl implements ResponsibilityService{
	private static final Log log=LogFactory.getLog(ResponsibilityServiceImpl.class);
    @Autowired
    private EvaluationMapper evaluationMapper;
    @Autowired UserService userService;
    @Autowired
    private SysPowerService sysPowerService;
    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private ProcessService processService;
	@Override
	public void saveProject(Project project) {

		project.setCreateTime(new Date());
		try {
			evaluationMapper.saveProject(project);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	@Override
	public List<Project> selectAllProject(HashMap<String, Object> params){
		
		return evaluationMapper.selectAllProject(params);
	}
	@Override
	public void saveMark(Mark mark) {
		
		
		
		try {
			evaluationMapper.saveMark(mark);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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

		int year=DateUtil.getYear();
		int month=DateUtil.getMonth();
		switch (taskType) {
		case ProcessType.EVALUCATION_MONTH:
			result.add(year+"年"+month+"月份-月度考核");
			result.add(year+"年"+(month-1)+"月份-月度考核");
			break;
		case ProcessType.EVALUCATION_HALFYEAR:
			if((month+1)<=6)  result.add((year-1)+"年下半年-半年考核");
			if((month+1)>6) result.add(year+"年上半年-半年考核");
			break;
		case ProcessType.EVALUCATION_FULLYEAR:
			result.add((year-1)+"年-年度考核");
			break;
		case ProcessType.RESPONSIBILITY_TYPE:
			result.add(year+"年"+ProcessType.RESPONSIBILITY_TYPE);
			for (int i =-1; i <2; i++) {
				result.add((year+i)+"年"+ProcessType.RESPONSIBILITY_TYPE);
			}
			break;
		case ProcessType.BUSINESSTYPE_ADDMARKS:
			for(int i=0;i<12;i++){
				if(month-i>0){
					result.add(year+"年"+(month-i)+"月");
				}else {
					result.add((year-1)+"年"+(month-i+12)+"月");
				}
				
			}
			break;
		default:
			break;
		}
//		if(taskType.equals(ProcessType.EVALUCATION_MONTH)){
//			result.add(year+"年"+month+"月份-月度考核");
//			result.add(year+"年"+(month-1)+"月份-月度考核");
//			return result;
//		}
//		if (taskType.equals(ProcessType.EVALUCATION_HALFYEAR)) {
//
//			if((month+1)<=6)  result.add((year-1)+"年下半年-半年考核");
//			if((month+1)>6) result.add(year+"年上半年-半年考核");
//			return result;
//		}
//		if (taskType.equals(ProcessType.EVALUCATION_FULLYEAR)) {
//			result.add((year-1)+"年-年度考核");
//			return result;
//		}
//		if (taskType.equals(ProcessType.RESPONSIBILITY_TYPE)) {
//			result.add(year+"年"+ProcessType.RESPONSIBILITY_TYPE);
//			for (int i =-1; i <2; i++) {
//				result.add((year+i)+"年"+ProcessType.RESPONSIBILITY_TYPE);
//			}
//			return result;
//		}
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
			return result;
		}
		if (RegexUtils.isMatch(taskTitle, ProcessType.EVALUCATION_FULLYEAR_PATTER)) {
			Integer year=Integer.valueOf(RegexUtils.getIndexGroup(1, taskTitle, ProcessType.EVALUCATION_FULLYEAR_PATTER));
			String startDate=DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(year));
			String endDate=DateUtil.formatDefaultDate(DateUtil.getLastDayOfYear(year));
			result.put("startDate", startDate);
			result.put("endDate", endDate);
			
			return result;
		}
		if(RegexUtils.isMatch(taskTitle, ProcessType.EVALUCATION_HALFYEAR_PATTER)){
			int beginMonth=0;
			int endMonth=0;
			String monthBetween=RegexUtils.getIndexGroup(2, taskTitle, ProcessType.EVALUCATION_HALFYEAR_PATTER);
			Integer year=Integer.valueOf(RegexUtils.getIndexGroup(1, taskTitle, ProcessType.EVALUCATION_HALFYEAR_PATTER));
			//判断是上半年还是下半年
//			if("下".equals(monthBetween)){
			if("上".equals(monthBetween)){
				beginMonth=1;
				endMonth=6;
			}else {
				beginMonth=7;
				endMonth=12;
			}
			String startDate=DateUtil.formatDefaultDate(DateUtil.getFirstDayOfMonth(year, beginMonth));
			String endDate=DateUtil.formatDefaultDate(DateUtil.getLastDayOfMonth(year, endMonth));
			
			result.put("startDate", startDate);
			result.put("endDate", endDate);
			return result;
		}
		if(RegexUtils.isMatch(taskTitle, responsibility)){
			Integer year=Integer.valueOf(RegexUtils.getIndexGroup(1, taskTitle, responsibility));
			String startDate=DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(year));
			String endDate=DateUtil.formatDefaultDate(DateUtil.getLastDayOfYear(year));
			result.put("startDate", startDate);
			result.put("endDate", endDate);
			return result;
		}
		return null;
	}

	@Override
	public Double getTotalMarkOfProjects(List<Project> projects) {
		if(projects==null||projects.size()==0) return 0.0;
		Double totalMark=0.00;
		for (Project project : projects) {
			totalMark+=this.getTotalMark(project);
		}
		
		return (double)Math.round(totalMark*100)/100;
	}
	@Override
	public Double getTotalMarkOfAllMonthAssessment(List<Project> projects) {
		Double totalMark=0.00;
		totalMark+=getTotalMarkOfProjects(projects);
		
		return null;
	}
	@Override
	public Double getTotalMark(Project project) {
		
		return this.getTotalMark(project.getMarks());
	}
	@Override
	public Double getTotalMark(List<Mark> marks) {
		Double totalMark=0.00;
		
		for (Mark mark : marks) {
			if(mark!=null){
				if(mark.getMarkNumber()=="") continue;
				totalMark+=Double.valueOf(mark.getMarkNumber());
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

		LinkedList<String> result=new LinkedList<String>();
        result.add(ProcessType.RESPONSIBILITY_TYPE);
        result.add(ProcessType.EVALUCATION_MONTH);
        result.add(ProcessType.EVALUCATION_HALFYEAR);
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
		HashMap<String,Object> params=new HashMap<String, Object>();
		
		switch (assess) {
		case "优秀":
			reason="加分项:月度考核定评为优";
			params.put("name","月考评优");
		    params.put("type","月考自动加减分");
		    markNumber=dictionaryService.selectSingleDic(params).getValue();
			break;
		case "基本合格":
			reason="减分项:月度考核定评为基本合格";	
			params.put("name","月考基本合格");
		    params.put("type","月考自动加减分");
		    markNumber=dictionaryService.selectSingleDic(params).getValue();
			break;
		case "不合格":
			reason="减分项:月度考核定评为不合格";
			params.put("name","月考不合格");
		    params.put("type","月考自动加减分");
		    markNumber=dictionaryService.selectSingleDic(params).getValue();
			break;

		default:
			break;
		}
       if(!"".equals(reason)){
    	    this.saveProjectAndMark(String.valueOf(evaluation.getProcessBean().getUser().getId()), evaluation.getStartDate(),
    	    		evaluation.getEndDate(),
    	    		"月度考核自动加减分项", reason, markNumber,1);
       }
	}
	@Override
	public void saveProjectAndMarkIngoreExisted(String userId, Date startDate, Date endDate, String projectContent,
			String markreason, String marknumber, Integer completed) {
		startDate=DateUtil.parseDefaultDate(DateUtil.formatDefaultDate(startDate));
		endDate=DateUtil.parseDefaultDate(DateUtil.formatDefaultDate(endDate));
		Project project=new Project();
   		project.setStartDate(startDate);
   		project.setEndDate(endDate);
   		project.setCompleted(String.valueOf(completed));
   		project.setCreateTime(new Date());
   		
   		project.setUserId(userId);
   		project.setProjectContent(projectContent);
   		Project pro=this.selectSingleProjectWithProjectEntity(project);
   		if(pro==null){
   			
   			this.saveProject(project);
   		}else {
			project=pro;
			
		}
   		
   		
   		
		Mark mark=new Mark();
		
		mark.setProjectId(project.getProjectId());
		mark.setAccordingly(markreason);
		
		
		mark.setMarkNumber(marknumber);
		mark.setMarkReason(projectContent);
		mark.setStartDate(startDate);
		mark.setEndDate(endDate);
		mark.setUserId(userId);
		mark.setChecked(String.valueOf(completed));
		this.saveMark(mark);
		
	}
	@Override
	public void saveProjectAndMark(String userId,Date startDate,Date endDate,String projectContent,String markreason,String marknumber,Integer completed) {
		startDate=DateUtil.parseDefaultDate(DateUtil.formatDefaultDate(startDate));
		endDate=DateUtil.parseDefaultDate(DateUtil.formatDefaultDate(endDate));
		Project project=new Project();
   		project.setStartDate(startDate);
   		project.setEndDate(endDate);
   		project.setCompleted(String.valueOf(completed));
   		project.setCreateTime(new Date());
   		
   		project.setUserId(userId);
   		project.setProjectContent(projectContent);
   		Project pro=this.selectSingleProjectWithProjectEntity(project);
   		if(pro==null){
   			
   			this.saveProject(project);
   		}else {
			project=pro;
			
		}
   		
   		
   		
		Mark mark=new Mark();
		
		mark.setProjectId(project.getProjectId());
		mark.setAccordingly(markreason);
		
		
		mark.setMarkNumber(marknumber);
		mark.setMarkReason(projectContent);
		mark.setStartDate(startDate);
		mark.setEndDate(endDate);
		mark.setUserId(userId);
		mark.setChecked(String.valueOf(completed));
		if(this.selectSingleMarkWithMarkEntity(mark)==null){
			this.saveMark(mark);
		}
		
		
	}
	@Override
	public String getIdentity(Evaluation evaluation,User user) {
		  return this.getIdentity(evaluation.getProcessBean(), user);
	}
	@Override
	public String getIdentity(ProcessBean processBean, User user) {
		 
		  //流程如果已对结束了，任务人都不能修改
		  if("1".equals(processBean.getCompleted()))return null;
		  //获取当前候选组，传递给页面，使对应的内容可以编辑
		  String currentCandidateGroup=processBean.getCurrentCandidateGroup();

		  if(currentCandidateGroup==null||"".equals(currentCandidateGroup)){
			  //本人允许修改
			  if(String.valueOf(processBean.getUser().getId()).equals(String.valueOf(user.getId()))){
				 
				  if("1".equals(processBean.getCommitted())) return null;//如果已经提交本人无法修改
				  return "employee";
			  }
		  }
		  //当前用户是否包含候选用户组
		  List<String> candidateGroups=activitiService.candidateGroups(String.valueOf(user.getId()));
		 
		  if(!candidateGroups.contains(currentCandidateGroup)) return null;

		  
		  
		  GroupTable groupTable=sysPowerService.selectGroupWithGroupId(currentCandidateGroup);
		  
		  if(groupTable.getGroupname().contains("考")) {
			  
			  return "overseer";
		  }else {
			
		  }
		  if(groupTable.getGroupname().contains("部门主任")||groupTable.getGroupname().contains("分管领导")) return "leader";
		return null;
	}
	@Override
	public String getIdentity(String processInstanceId, User user) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("processInstanceId", processInstanceId);
		ProcessBean processBean=processService.selectSingleProcessBean(params);
		
		return this.getIdentity(processBean, user);
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
			
			mark.setChecked("1");
			this.updateMark(mark);
		}
	}

	@Override
	public void deductWithMonthProcessDelaySubmittedWithUser(User user, Date startDate, Date endDate,
			String markReason) {
		//查询是否已经被扣过分了
				HashMap<String, Object> params1=new HashMap<String, Object>();
				params1.put("startDate",DateUtil.formatDefaultDate(startDate));
				params1.put("endDate",DateUtil.formatDefaultDate(endDate));
				params1.put("userId",String.valueOf(user.getId()));
				params1.put("markReason",markReason);
				//用户防止重复扣，但是因为延期提交是每天都要扣的，所以删除
//				if(isDeduct(user,AvailableSettings.MONTHASSESS_DELAYSUBMITTED, startDate, endDate)){
//					
//					return ;
//				}
				//延迟提交 扣分
				HashMap<String, Object> params=new HashMap<String, Object>();
				params.put("name","月考延迟提交每日扣分");
			    params.put("type","月考自动加减分");
				String marknumber=dictionaryService.selectSingleDic(params).getValue();
				
				this.saveProjectAndMarkIngoreExisted(String.valueOf(user.getId()), startDate, endDate, AvailableSettings.SYSTEM_UPLOAD, markReason, marknumber,1);
	}
	@Override
	public void deductWithMonthProcessUnsubmittedBeforeDeadLineWithUser(User user, Date startDate, Date endDate,
			String markReason) {
	        	//查询是否已经被扣过分了
				if(isDeduct(user, AvailableSettings.MONTHASSESS_UNSUBMITTEDBEFOREDEADLINE, startDate, endDate)){
					
					return ;
				}
		
//				HashMap<String, Object> params1=new HashMap<String, Object>();	
//				params1.put("startDate",DateUtil.formatDefaultDate(startDate));
//				params1.put("endDate",DateUtil.formatDefaultDate(endDate));
//				params1.put("userId",String.valueOf(user.getId()));
//				params1.put("markReason",markReason);
				

				//限期未交判定不合格
				HashMap<String, Object> params=new HashMap<String, Object>();
				params.put("name","月考不合格");
			    params.put("type","月考自动加减分");
				String marknumber=dictionaryService.selectSingleDic(params).getValue();
				try {
					this.saveProjectAndMark(String.valueOf(user.getId()), startDate, endDate, AvailableSettings.SYSTEM_UPLOAD, markReason, marknumber,1);
				    System.out.println("*****************用户【"+user.getId()+"】限期未交，成功扣分");
				    //删除延迟扣分的
				    
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			
		
	}
	@Override
	public boolean isDeduct(User user, String accordingly, Date startDate, Date endDate) {
		//查询是否已经被扣过分了
				HashMap<String, Object> params1=new HashMap<String, Object>();
				
				
				params1.put("startDate",DateUtil.formatDefaultDate(startDate));
				params1.put("endDate",DateUtil.formatDefaultDate(endDate));
				params1.put("userId",String.valueOf(user.getId()));
				params1.put("accordingly", accordingly);
				if(this.selectAllMarkAsType(params1).size()>0){
					System.out.println("*************限期未提交已经扣过分了**************");
					return true;
				}
				System.out.println("*************限期未提交还没有扣过分**************");
				return false;
	}
	@Override
	public String getDegreeOfUrgencyWithUnsubmittedAssessForm() {
		int today=DateUtil.getToDay();
		int warning=Integer.valueOf(dictionaryService.selectSingleValueOfDic("提醒提交日期", "月度考核"));
		int danger=Integer.valueOf(dictionaryService.selectSingleValueOfDic("延迟提交扣分日期", "月度考核"));
		int deadLine=Integer.valueOf(dictionaryService.selectSingleValueOfDic("限期未交扣分日期", "月度考核"));
		
		//如果最后期限为0，那么不做任务限制
		if(deadLine==0)return AvailableSettings.DEGREEOFURGENCY_NOMAL;
		if(today<warning) return AvailableSettings.DEGREEOFURGENCY_NOMAL;
		if(today<danger) return AvailableSettings.DEGREEOFURGENCY_WARN;
		if(today<deadLine) return AvailableSettings.DEGREEOFURGENCY_DANGER;
		if(today>=deadLine) return AvailableSettings.DEGREEOFURGENCY_DEAD;
		
		return AvailableSettings.DEGREEOFURGENCY_NOMAL;
	}
	@Override
	public boolean isOverTheDeadLineForSubmittedMonthProcess() {
		int today=DateUtil.getToDay();
	
		Dictionary dictionary=dictionaryService.selectSingleDic("限期未交扣分日期","月度考核");
		int deadLineDayOfMonth=0;
		if(dictionary!=null) deadLineDayOfMonth=Integer.valueOf(dictionary.getValue());
		
		if(deadLineDayOfMonth==0) return false;
		if(today>deadLineDayOfMonth)return true;
		
		return false;
	}
	@Override
	public List<User> selectAllUsersWhoUnsubmittedMonthProcess(MonthAssessCfgPojo monthAssessCfgPojo) {
		String titleLike=DateUtil.formatDateByFormat(DateUtil.addMonths(new Date(),-1), "yyyy年M月份")+"-月度考核";
        String postNames=monthAssessCfgPojo.getTargetGroup();
       
        HashMap<String, Object> params=new HashMap<String, Object>();
        params.put("titleLike", titleLike);
        params.put("postNames", postNames.split(","));
        //params.put("committed", "0");
        return processService.selectUsersWhoUnsubmittedProcess(params);
	}
	@Override
	public void deductWithMonthProcess(MonthAssessCfgPojo monthAssessCfgPojo) {
	
		List<User> users=selectAllUsersWhoUnsubmittedMonthProcess(monthAssessCfgPojo);
		Date startDate=DateUtil.getFirstDayOfMonth(DateUtil.getYear(), DateUtil.getMonth());
		Date endDate=DateUtil.getLastDayOfMonth(DateUtil.getYear(), DateUtil.getMonth());
		
		
//		if(getDegreeOfUrgencyWithUnsubmittedAssessForm()==AvailableSettings.DEGREEOFURGENCY_DANGER){
//			//限期未交每天扣0.5分
//			for (User user : users) {
//				deductWithMonthProcessDelaySubmittedWithUser(user,startDate, endDate, AvailableSettings.MONTHASSESS_DELAYSUBMITTED);
//			}
//		    return ;
//		}
//		//限时未交，设置不合格
//		if(getDegreeOfUrgencyWithUnsubmittedAssessForm()==AvailableSettings.DEGREEOFURGENCY_DEAD){
//	    		log.info("月度考核截止日期为["+monthAssessCfgPojo.getDeadLineDayOfMonth()+"]号，目前仍为交表的设置为不合格");
//	    		for (User user : users) {
//	    			deductWithMonthProcessUnsubmittedBeforeDeadLineWithUser(user,startDate,endDate,AvailableSettings.MONTHASSESS_UNSUBMITTEDBEFOREDEADLINE);
//				}
//	    	return ;
//		}
		for (User user : users) {
			deductWithMonthProcessDelaySubmittedWithUser(user,startDate, endDate, AvailableSettings.MONTHASSESS_DELAYSUBMITTED);
		}
		
	}
	@Override
	public String getTargetGroupWithMonthAssess() {
		Dictionary dictionary=dictionaryService.selectSingleDic("考核对象", "月度考核");
		if(dictionary==null)return null;
		return dictionary.getValue();
	}
	@Override
	public int countTotalMarkAndUser(HashMap<String, Object> params) {
		
		return evaluationMapper.countTotalMarkAndUser(params);
	}
	@Override
	public void updateMonthAssessCfg(String cronExpression) {
		


	    Dictionary dictionary=dictionaryService.selectSingleDic("延迟提交扣分日期", "月度考核");
	    int dangerBeginDayOfMonth=Integer.valueOf(dictionary.getValue());
	    
	    Dictionary dictionary2=dictionaryService.selectSingleDic("限期未交扣分日期", "月度考核");
	    int deadLineDayOfMonth=Integer.valueOf(dictionary2.getValue());
	    
	    Dictionary dictionary3=dictionaryService.selectSingleDic("提醒提交日期", "月度考核");
	    
	    
		String[] x=cronExpression.split(" ");
	    String[] days=x[3].split("-");
	    //如果不是时间段，那么开始执行的日期默认是deadline;
	    if(days==null){
	    	dictionary2.setValue(String.valueOf(x[3]));
	    	dictionaryService.updateDic(dictionary2);
	    }
	    int dayBegin=Integer.valueOf(days[0]);
	    int dayEnd=Integer.valueOf(days[1]);
	   
		try {
		    if(dayBegin!=dangerBeginDayOfMonth) {
		    	dictionary.setValue(String.valueOf(dayBegin));
		    	dictionaryService.updateDic(dictionary);
		    	dictionary3.setValue(String.valueOf((dayBegin-2)));
		    	dictionaryService.updateDic(dictionary3);
		    }
		    if(dayEnd!=deadLineDayOfMonth){
		    	dictionary2.setValue(String.valueOf(dayEnd));
		    	dictionaryService.updateDic(dictionary2);
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	@Override
	public boolean canIDeleteProject(String projectId) {
		Project project=this.selectProjectWithProjectId(projectId);
		if(project==null||"系统导入".equals(project.getProjectContent())) return false;

		return true;
	}
	@Override
	public Double getBaseMarkOfAssessment() {
		String baseMark=dictionaryService.selectSingleValueOfDic("基础分", "基本定格");
		if(baseMark==null) throw new RuntimeException("系统未设置半年和年度考核的基础分，请联系管理员 ");
		return Double.valueOf(baseMark);
	}
	@Override
	public Double selectTotalMark(HashMap<String, Object> params) {
		Double result=evaluationMapper.selectTotalMark(params);
		if(result==null)return null;
		return evaluationMapper.selectTotalMark(params);
	}
	@Override
	public Double selectTotalMark(Integer userId, String checked, String startDate, String endDate) {
		 HashMap<String, Object> params2 = new HashMap<String,Object>();
		  params2.put("userId",userId);
		  params2.put("startDate",startDate);
		  params2.put("endDate", endDate);
         params2.put("checked", checked);
		return this.selectTotalMark(params2);
	}
	@Override
	public List<ProcessEntity> selectEvaluationWithUser(HashMap<String, Object> params) {
		
		return evaluationMapper.selectEvaluationWithUser(params);
	}
	@Override
	public List<Project> selectProject(HashMap<String, Object> params) {
		
		return evaluationMapper.selectProject(params);
	}
	@Override
	public String getTitleLikeForYear(String taskType, String times) {
       
		int month=DateUtil.getMonth()+1;
	    int year=DateUtil.getYear();
	    String titleLike=null;
		switch (taskType) {
	    //半年考核排名
		case "halfYear":

			//标题
			
            if (times==null||"".equals(times)) {
            	if(month<=6){
    				titleLike=(year-1)+"年下半年-半年考核";
    			}else {
    				titleLike=year+"年上半年-半年考核";
    			}
			}else {
				titleLike=times+"-半年考核";
			}
			
			break;
	    //全年考核排名
        case "fullYear":
           if (times==null||"".equals(times)) {
        	    year=DateUtil.getYear();
              	titleLike=(year-1)+"年-年度考核"; 
            }else {
            	titleLike=times+"-年度考核";
			}
        	
			break;

		default:
			break;
		}
		return titleLike;
	}
	@Override
	public String getBusinessType(String taskType) {
		switch (taskType) {
	    //半年考核排名
		case "halfYear":
			//类型
			return ProcessType.EVALUCATION_HALFYEAR;
	    //全年考核排名
        case "fullYear":
        	return ProcessType.EVALUCATION_FULLYEAR;
		default:
			break;
		}
		return null;
	}

	@Override
	public Evaluation selectSingleEvaluationAsResultType(String phone, String sparation) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("phone", phone);
		params.put("sparation", sparation);
		params.put("completed", "0");
		return this.selectSingleEvaluationAsResultType(params);
	}
	@Override
	public Evaluation selectSingleEvaluationAsResultType(HashMap<String, Object> params) {
		List<Evaluation> eList=evaluationMapper.selectAllEvaluationWithResultType(params);
		if(eList==null||eList.size()==0) return null;
		return eList.get(0);
	}
	@Override
	public boolean isOverRate(HashMap<String, Object> params, Double rate) {
		 if(params.containsKey("completed")) params.remove("completed");
		  double total=processService.selectCountProcess(params);
		  params.put("completed", "1");
		  double currentNum=processService.selectCountProcess(params);
		  params.remove("completed");
		  if(currentNum/total>rate) return true;
		  
		return false;
	}
	@Override
	public List<Evaluation> selectAllEvaluationWithProcess(HashMap<String, Object> params) {
		
		return evaluationMapper.selectAllEvaluationWithProcess(params);
	}
	@Override
	public Evaluation selectSingleEvaluationWithProcess(HashMap<String, Object> params) {
		List<Evaluation> list=this.selectAllEvaluationWithProcess(params);
		if(list==null||list.size()==0) return null;
		return list.get(0);
	}
	@Override
	public Evaluation selectSingleEvaluationWithProcess(String phone, String sparation) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("phone", phone);
		params.put("sparation", sparation);
		//params.put("completed", "0");
		return this.selectSingleEvaluationWithProcess(params);
	}
	@Override
	public double getTotalMarkOfEvaluation(Evaluation evaluation) {
		Dept dept=userService.selectSingleDept(evaluation.getProcessBean().getDeptName());
		
		DeptType deptType=userService.selectSingleDeptType(dept.getDid());
		double leader=0;
		if(evaluation.getLeadershipEvaluation()!=null)
		leader=Double.valueOf(dictionaryService.selectSingleValueOfDic(evaluation.getLeadershipEvaluation(), "基本定格对应得分"))*Double.valueOf(dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "领导点评评分占比"));
		//组织考评
		double org=0;
		if(evaluation.getOverseerEvaluation()!=null&&!"".equals(evaluation.getOverseerEvaluation())) org=Double.valueOf(dictionaryService.selectSingleValueOfDic(evaluation.getOverseerEvaluation(), "基本定格对应得分"))*Double.valueOf(dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "组织点评评分占比"));
		//考核量化分
		double assess=0;
		if(evaluation.getMarks()!=null&&!"".equals(evaluation.getMarks())) assess=Double.valueOf(evaluation.getMarks())*Double.valueOf(dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "考核量化分占比"));
		//群众评议
		double pub=0;
		if(evaluation.getPublicEvaluation()!=null&&!"".equals(evaluation.getPublicEvaluation())) pub=Double.valueOf(dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "群众点评评分占比"))*Double.valueOf(evaluation.getPublicEvaluation());
		double totalMark=assess+leader+org+pub;
//		System.out.println("leader:"+leader+"---"+dictionaryService.selectSingleValueOfDic(evaluation.getLeadershipEvaluation(), "基本定格对应得分")+"*"+dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "领导点评评分占比"));
//		System.out.println("overseer:"+org+"---"+Double.valueOf(dictionaryService.selectSingleValueOfDic(evaluation.getOverseerEvaluation(), "基本定格对应得分"))+"*"+dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "组织点评评分占比"));
//		System.out.println("assess:"+assess+"---"+evaluation.getMarks()+"*"+Double.valueOf(dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "考核量化分占比")));
//		System.out.println("public:"+pub+"---"+evaluation.getPublicEvaluation()+"*"+Double.valueOf(dictionaryService.selectSingleValueOfDic(deptType.getDictionary().getName(), "群众点评评分占比")));
//		System.out.println("totalMark:"+(double)Math.round(totalMark*100)/100);
		totalMark=(double)Math.round(totalMark*100)/100;
		return totalMark;
	}
	@Override
	public Evaluation selectSingleEvaluation(String phone, String sparation) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("phone", phone);
		params.put("sparation", sparation);
		params.put("completed", "0");
		return this.selectSingleEvaluation(params);
	}
	@Override
	public double getTotalMarkOfAllMonthAssessmentWithBasemark(Evaluation evaluation) {
		Double totalMark =this.selectTotalMark(evaluation.getProcessBean().getUser().getId(), 
				  "1", DateUtil.formatTimesTampDate(evaluation.getStartDate()),
				  DateUtil.formatTimesTampDate(evaluation.getEndDate()));
		totalMark=totalMark==null?0.0:totalMark;
        Double baseMark=this.getBaseMarkOfAssessment();
		
		return (double)Math.round((totalMark+baseMark)*100)/100;
	}
	@Override
	public List<String> selectAllMonthWhichDelaySubmitMonthAssessApply(Integer userId,int limit) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("column", "distinct startDate ");
		params.put("userId", userId);
		params.put("limitClause", "limit 0,"+(limit-1));
		params.put("whereClause", " accordingly in ('月度考核限期未交，被判定为不合格','月度考核延迟提交产生扣分')");
		params.put("orderClause", "order by startDate desc");
		List<String> list=evaluationMapper.selectSingleColumnFromMark(params);
		if(list==null||list.size()==0) return null;
	    List<String> result=new ArrayList<String>();
	    for (String str : list) {
	    	Date date=DateUtil.parseTimesTampDate(str);
	    	
			int month=DateUtil.getMonth(date)+1;
			
			int year=DateUtil.getYear(date);
			
			
			
			result.add(year+"年"+month+"月");
		}
		return result;
	}
	@Override
	public void deleteAllMonthWhichDelaySubmitMonthAssessApply(String date,Integer userId) {
	    int year=0;
		int month=0;
		String pattern="(.*?)年(.*?)月";
		if(RegexUtils.isMatch(date,pattern)){
			   year=Integer.valueOf(RegexUtils.getIndexGroup(1, date, pattern));
			   month=Integer.valueOf(RegexUtils.getIndexGroup(2, date, pattern));
			   String startDate=DateUtil.formatDefaultDate(DateUtil.getFirstDayOfMonth(year, month));
			   
			   HashMap<String, Object> params=new HashMap<String, Object>();
			    
				
			   params.put("whereClause", "projectId in (select projectId from res_mark where userId="+userId+" and startDate='"+startDate+"' and accordingly in ('月度考核限期未交，被判定为不合格','月度考核延迟提交产生扣分') )");
			    
			   this.deleteFromProject(params);
		}
		
	}
	@Override
	public void deleteFromProject(HashMap<String, Object> params) {
		try {
			evaluationMapper.deleteFromProject(params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
	}
	@Override
	public List<Project> selectProjectWithProjectEntity(Project project) {
		if(project==null) return null;
		return evaluationMapper.selectProjectWithProjectEntity(project);
	}
	@Override
	public Project selectSingleProjectWithProjectEntity(Project project) {
		
		List<Project> resList=this.selectProjectWithProjectEntity(project);
		if(resList==null||resList.size()==0) return null;
		return resList.get(0);
	}
	@Override
	public List<Mark> selectMarkWithMarkEntity(Mark mark) {
		if(mark==null) return null;
		return evaluationMapper.selectMarkWithMarkEntity(mark);
	}
	@Override
	public Mark selectSingleMarkWithMarkEntity(Mark mark) {
		List<Mark> marks=this.selectMarkWithMarkEntity(mark);
		if(marks==null||marks.size()==0) return null;
		return marks.get(0);
	}
	@Override
	public List<Mark> selectTotalMarkWithAllUser(HashMap<String, Object> params) {
		
		return evaluationMapper.selectTotalMarkWithAllUser(params);
	}











}
