package com.crm.springboot.service.impl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.mapper.ProcessMapper;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
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
		System.out.println("查询所有的进程 ");
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
//		List<String> result=new ArrayList<String>();
//		for (ProcessBean pb : this.selectAllProcess(params)) {
//			result.add(pb.getProcessInstanceId());
//		}
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
			System.out.println(referer);
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



}
