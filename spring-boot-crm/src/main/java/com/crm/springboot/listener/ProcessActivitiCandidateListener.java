package com.crm.springboot.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.codehaus.groovy.runtime.callsite.PerInstancePojoMetaClassSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.SysPowerService;

@Service
public class ProcessActivitiCandidateListener implements TaskListener{
	@Autowired
    private ActivitiService activitiService;
    @Autowired 
    private ProcessService processService;
    @Autowired
    private SysPowerService sysPowerService;
	@Override
	public void notify(DelegateTask delegateTask) {
		/**
		 * delegateTask.getName()对应bpmn流程图  <userTask id="usertask1" name="提交申请"></userTask>中的name值
		 * delegateTask.getTaskDefinitionKey()对应的是id值
		 * 
		 */
		
		String taskname=delegateTask.getName();
		
		String candidateGroup=null;
		//获取流程变量
        HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("processInstanceId",delegateTask.getProcessInstanceId());
		ProcessBean processBean=processService.selectSingleProcessBean(params);
		if(processBean==null) return ;
        if("提交申请".equals(taskname)){
        	candidateGroup="";
        }
        if("领导审批".equals(taskname)){
        	//申请用户职级
        	if(processBean.getUser().getPost()==null) throw new RuntimeException("无法查询用户的职级，该流程将失效，请联系管理员");
        	String post=processBean.getUser().getPost().getName();
        	//申请用户的审批权限
        	List<GroupManager> groupManagers=processBean.getUser().getUserLinkGroup();
        	List<String> groups=new ArrayList<String>();
        	for (GroupManager groupManager : groupManagers) {
        		
				groups.add(groupManager.getGroupTable().getGroupname());
			}
        	//分管领导审批
        	if("中层正职".equals(post)||groups.contains("部门主任组")){
        		
        		candidateGroup=sysPowerService.selectGroupIdWithGroupName("分管领导组");
        	}else {
        		//部门主任审批
            	if("普通员工".equals(post)||"中层副职".equals(post)) {
            		
            		candidateGroup=sysPowerService.selectGroupIdWithGroupName("部门主任组");
            	}
			}
        	
        }
        if("考核组".equals(taskname)){
        	
        	candidateGroup=processService.getCandidateGroupOfAssessGroupWithDeptname(processBean.getDeptName());

        }

        if("考核办初审".equals(taskname)){
        	candidateGroup=sysPowerService.selectGroupIdWithGroupName("考核办");
        }
        if("考核办审批".equals(taskname)){
        	candidateGroup=sysPowerService.selectGroupIdWithGroupName("考核办主任");
        }
        if(taskname.contains("内可撤回")){
        	candidateGroup="无";
        }
		processBean.setCurrentCandidateGroup(candidateGroup);
		processService.updateProcess(processBean);
		delegateTask.addCandidateGroup(candidateGroup);
		
	}
	

}
