package com.crm.springboot.service.impl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.mapper.ProcessMapper;
import com.crm.springboot.pojos.Dictionary;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessEntity;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.DictionaryService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.DateUtil;
@Service
public class ProcessServiceImpl implements ProcessService{
    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private SysPowerService sysPowerService;
    @Autowired
    private UserService userService;
    @Autowired
    private DictionaryService dictionaryService;
	@Override
	public <T> T save(T t) {
		
		processMapper.save(t);
		return t;
	}

	@Override
	public <T> void deleteById(Serializable id) {
		processMapper.deleteById(id);
		activitiService.deleteProcessInstance(String.valueOf(id), "废弃");
		
	}

	@Override
	public <T> T update(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProcessBean> selectAllProcess(HashMap<String, Object> params) {
		
		return processMapper.selectAllProcess(params);
	}

	@Override
	public void deleteProcessByProcessInstanceIds(String[] ids) {
		processMapper.deleteProcessByProcessInstanceIds(ids);
		for (String id : ids) {
			activitiService.deleteProcessInstance(id, "废弃");
		}
		
		
	}

	@Override
	public List<String> selectAllProcessInstanceIds(HashMap<String, Object> params) {

		return processMapper.selectAllProcessInstanceIds(params);
	}

	@Override
	public void setCompleted(Execution execution) {
//		HashMap<String, Object> params = new HashMap<String,Object>();
//		params.put("processInstanceId", processInstanceId);
//		ProcessBean processBean=this.selectAllProcess(params).get(0);
//		processBean.setCompleted(completed);
//		this.updateProcess(processBean);
		System.out.println("=================流程结束========================");
	}

	@Override
	public void updateProcess(ProcessBean processBean) {
		processMapper.updateProcess(processBean);
		
	}

	@Override
	public ProcessBean selectSingleProcessBean(HashMap<String, Object> params) {
		List<ProcessBean> processBeans=this.selectAllProcess(params);
		
		if(processBeans==null || processBeans.size()==0) return null;
		
		
		return this.selectAllProcess(params).get(0);
	}

	@Override
	public boolean isUserHasTheCandidateGroup(String userId, String processInstanceId) {
		//根据流程定义ID查询ProcessBean
		HashMap<String , Object> params=new HashMap<String, Object>();
		params.put("processInstanceId", processInstanceId);
		ProcessBean processBean=this.selectSingleProcessBean(params);
		
		//查询当前用户属于的所有用户组
		params.remove("processInstanceId");
		params.put("userId", userId);
		List<String> groupIds=sysPowerService.selectAllGroupIdsWithHashMap(params);
        
		//判断当前用户是否属于流程的候选人组
		if(
				processBean==null||
				processBean.getCurrentCandidateGroup()==null||
				processBean.getCurrentCandidateGroup().equals("")||
				groupIds.contains(processBean.getCurrentCandidateGroup())){
			return true;
		}

		return false;
	}

	@Override
	public String isUserHasTheCandidateGroup(String userId, String processInstanceId, String referer) throws UnsupportedEncodingException {
		if(referer.contains("?msg=")){
			int x=referer.indexOf("?msg=");
			referer=referer.substring(0, x);
			
		}
		if(!this.isUserHasTheCandidateGroup(userId, processInstanceId)){
			String msg=URLEncoder.encode("该申请已经提交，你无权修改，若要修改可以让下一级审批人先驳回","UTF-8");
			return "redirect:"+referer+"?msg="+msg;
		}
		return null;
	}

	@Override
	public boolean isProcessExists(String userId, String title) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("titleExactly", title);
		ProcessBean processBean=selectSingleProcessBean(params);
		return processBean==null?false:true;
	}

	@Override
	public List<String> getProcessInstanceIdsWithLeader(User user, HashMap<String, Object> params) {
		//用户涉及部门
		

    	List<String> candidateGroups=sysPowerService.getGroupsOfLeaderWithUserId(String.valueOf(user.getId()));
    	
    	if(candidateGroups==null) return null;
    	
    	
    	
		if(candidateGroups!=null)params.put("candidateGroups",candidateGroups.toArray(new String[candidateGroups.size()]));
    	
		return this.selectAllProcessInstanceIds(params);
	}
	@Override
	public List<String> getProcessInstanceIdsWithAssessmentGroup(User user, HashMap<String, Object> params) {
		//用户涉及的考核组
		List<String> assessGroups=sysPowerService.getGroupsOfOvereerWithUserId(String.valueOf(user.getId()));
		if(assessGroups==null) return null;
		params.put("candidateGroups",assessGroups.toArray(new String[assessGroups.size()]));
		return this.selectAllProcessInstanceIds(params);
	}
	@Override
	public List<String> getProcessInstanceIdsWithUser(User user, HashMap<String, Object> params) {
		//用户涉及部门
		String[] deptNames=userService.getDeptNames(user);
		//用户涉及的考核组
		List<String> assessGroups=sysPowerService.getGroupsOfOvereerWithUserId(String.valueOf(user.getId()));
		
		if(deptNames==null&&assessGroups==null) return null;
		
		if(deptNames!=null)params.put("deptNames",deptNames);
		if(assessGroups!=null)params.put("candidateGroups",assessGroups.toArray(new String[assessGroups.size()]));
    	
		return this.selectAllProcessInstanceIds(params);
	}

	@Override
	public List<User> selectAllUsersWhoUnsubmittedProcess(HashMap<String, Object> params) {
		
		return processMapper.selectAllUsersWhoUnsubmittedProcess(params);
	}

	@Override
	public Integer selectCountUserNumberWhichSubmittedProcess(HashMap<String, Object> params) {
		
		return processMapper.selectCountUserNumberWhichSubmittedProcess(params);
	}

	@Override
	public Integer selectCountUserNumberWhichSubmittedProcess(String deptName, String titleLike) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("deptName", deptName);
		params.put("titleLike", titleLike);
		return this.selectCountUserNumberWhichSubmittedProcess(params);
	}

	@Override
	public Integer selectCountProcess(HashMap<String, Object> params) {
		
		return processMapper.selectCountProcess(params);
	}

	@Override
	public Integer selectCountProcess(String deptName, String titleLike,String completed) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("deptName", deptName);
		params.put("titleLike", titleLike);
		params.put("completed", completed);
		return this.selectCountProcess(params);
	}
	@Override
	public Integer selectCountProcess(String[] deptNames, String titleLike,String completed) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("deptNames", deptNames);
		params.put("titleLike", titleLike);
		
		params.put("completed", completed);
		return this.selectCountProcess(params);
	}
	@Override
	public Integer selectCountProcess(String deptName, String deptLevel, String titleLike, String completed) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		List<String> deptNames=userService.getDeptNames(deptName, deptLevel);
		params.put("deptNames", deptNames);
		params.put("titleLike", titleLike);
		
		params.put("completed", completed);

		return this.selectCountProcess(params);
	}
	@Override
	public Integer selectCountProcess(String deptName, String deptLevel, String titleLike, String completed,
			List<String> currentCandidateGroupNotIn) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		List<String> deptNames=userService.getDeptNames(deptName, deptLevel);

		params.put("deptNames", deptNames);
		params.put("titleLike", titleLike);
		
		params.put("completed", completed);
        
		params.put("currentCandidateGroupNotIn", currentCandidateGroupNotIn);
		return this.selectCountProcess(params);
	}
	@Override
	public Integer selectCountUserNumberWhichSubmittedProcess(String[] deptNames, String titleLike) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("deptNames", deptNames);
		params.put("titleLike", titleLike);
		return this.selectCountUserNumberWhichSubmittedProcess(params);
	}

	@Override
	public List<String> selectDistinctCurrentCandidateGroup(HashMap<String, Object> params) {
		
		return processMapper.selectDistinctCurrentCandidateGroup(params);
	}

	@Override
	public List<String> selectDistinctCurrentCandidateGroup(String[] deptNames, String titleLike, String completed,
			String committed) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("deptNames", deptNames);
		params.put("titleLike", titleLike);
		params.put("completed", completed);
		params.put("committed", committed);
		return this.selectDistinctCurrentCandidateGroup(params);
	}

	@Override
	public Integer selectCountProcess(String[] deptNames, String titleLike, String completed, String currentCandidateGroup) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("deptNames", deptNames);
		params.put("titleLike", titleLike);
		params.put("completed", completed);
		params.put("currentCandidateGroup", currentCandidateGroup);
		return this.selectCountProcess(params);
	}

	@Override
	public List<String> selectSingleFieldFromProcess(HashMap<String, Object> params) {
		
		return processMapper.selectSingleFieldFromProcess(params);
	}

	@Override
	public List<String> selectDistinctDeptWithGroupName(String groupName,String titleLike) {
		//首先查询当前考核组下的流程总数
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("titleLike", titleLike);
		switch (groupName) {
		case ProcessType.PROCESS_UNSUBMITTED:
			params.put("committed", "0");
			break;
		case ProcessType.PROCESS_FINISHED:
			params.put("completed", "1");
			break;
		default:
			String currentCandidateGroup=sysPowerService.selectGroupIdWithGroupName(groupName);
			if(currentCandidateGroup==null) return null;
			params.put("currentCandidateGroup", currentCandidateGroup);
			break;
		}
		params.put("keywordBefore", "distinct");

		return this.selectSingleFieldFromProcess(params);
	}
	/**
	 * 查询子部门
	 */
	@Override
	public String[] selectDeptNames(String deptLevel, String deptName) {
		List<String> dnames=null;
		dnames=userService.selectSingleColumnFromInfoDept("distinct name ", deptLevel, deptName);
		if (dnames==null||dnames.size()==0) return null;
		return dnames.toArray(new String[dnames.size()]);
	}

	@Override
	public List<ProcessEntity> selectAllEvaluationFromProcess(HashMap<String, Object> params) {
		
		return processMapper.selectAllEvaluationFromProcess(params);
	}

	@Override
	public List<String> selectAllProcessInstanceIds(String deptName, String titleLike) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("deptNames", deptName.split(","));
		params.put("title", titleLike);
		return processMapper.selectAllProcessInstanceIds(params);
	}

	@Override
	public Integer getSubMittedNumber(String deptName,String deptLevel,String titleLike) {
		

        Dept dept=userService.selectSingleDept(deptName);
	    if(dept==null) throw new RuntimeException("该部门["+deptName+"]不存在");

	    List<String> dnames=userService.getDeptNames(deptName, deptLevel);//涉及到的部门

		return this.selectCountUserNumberWhichSubmittedProcess(dnames.toArray(new String[dnames.size()]), titleLike);
	}

	@Override
	public Integer getApprovalTimeRate(String titleLike,List<String> deptNames,String taskName,String groupName) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		HashMap<String, Object> parMap=new HashMap<String, Object>();
		
		parMap.put("title", titleLike);
		List<String> totalProcessInstanceIds=this.selectAllProcessInstanceIds(parMap);
		parMap.put("deptNames", deptNames);
		List<String> processInstanceIds=this.selectAllProcessInstanceIds(parMap);//根据部门和titleLike查询出processInstanceIds
		String takeTime="0";//审批耗时
		String totalTime="0";
		Integer timeRate=100;


		
		if(processInstanceIds!=null&&processInstanceIds.size()>0){
			params.put("processInstanceIds", totalProcessInstanceIds);
			params.put("name", taskName);
			List<String> assignees=userService.selectUserIdWithDeptNameAndGroupName(null, groupName);
			params.put("assignees", assignees);
			totalTime=activitiService.selectSumDuration(params);//所有领导组审批耗时
			
			
			params.put("processInstanceIds", processInstanceIds);
			assignees=userService.selectUserIdWithDeptNameAndGroupName(deptNames, groupName);
			
			params.put("assignees", assignees);
			takeTime=activitiService.selectSumDuration(params);//当前部门领导组审批耗时
			
			if ("0".equals(takeTime)) {//如果还未审批的话，返回100
				
				return 100;
			}

		}
		if(!totalTime.equals("0")) timeRate=(int) (Long.parseLong(takeTime)*100/Long.parseLong(totalTime));
	
		return timeRate;
	}

	@Override
	public Integer getNumberHaveToSubmittedTheMonthAssess(Integer deptId, String deptLevel) {
		Integer a=0;//查询部门需要交表的人数
		String postIds=dictionaryService.selectSingleValueOfDic("考核对象id", "月度考核");
		a=userService.selectCountUserNumber(deptId, deptLevel, postIds);

		return a;
	}

	@Override
	public Integer getNumberHaveToSubmittedTheMonthAssess(String deptName, String deptLevel) {
		Dept dept=userService.selectSingleDept(deptName);
		if(dept==null) throw new RuntimeException("该部门["+deptName+"]不存在");
		return this.getNumberHaveToSubmittedTheMonthAssess(dept.getDid(), deptLevel);
	}

	@Override
	public List<ProcessBean> selectHistoryProcessFromActHiTaskInst(HashMap<String, Object> params) {
		
		return processMapper.selectHistoryProcessFromActHiTaskInst(params);
	}

	@Override
	public List<ProcessBean> selectProcessFromActRuTask(HashMap<String, Object> params) {
	
		return processMapper.selectProcessFromActRuTask(params);
	}

	@Override
	public List<ProcessBean> selectAllProcessWithUserNameAndPhone(HashMap<String, Object> params) {
		
		return processMapper.selectAllProcessWithUserNameAndPhone(params);
	}

	@Override
	public List<User> selectUsersWhoUnsubmittedProcess(HashMap<String, Object> params) {
		
		return processMapper.selectUsersWhoUnsubmittedProcess(params);
	}

	@Override
	public String getCandidateGroupOfAssessGroupWithDeptname(String deptName) {
		Dictionary dictionary=dictionaryService.selectSingleDict(deptName, "考核组");
		if(dictionary!=null){
			
			return sysPowerService.selectGroupIdWithGroupName(dictionary.getName());
		}else {
			UserLinkDept userLinkDept=userService.selectSingleUserLinkDept(deptName);
			dictionary=dictionaryService.selectSingleDict(userLinkDept.getSecondLevel().getName(), "考核组");
			return sysPowerService.selectGroupIdWithGroupName(dictionary.getName());
		}
		
	
		
	}









}
