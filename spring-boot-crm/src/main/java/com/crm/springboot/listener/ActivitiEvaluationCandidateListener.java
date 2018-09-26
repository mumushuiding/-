package com.crm.springboot.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.filter.ApplicationContextHeaderFilter;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.SysPowerService;

@Service
public class ActivitiEvaluationCandidateListener implements TaskListener,ExecutionListener{
	
	
	private static final long serialVersionUID = -774095281717745551L;
	
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
        if("考核组初审".equals(taskname)||"考核组".equals(taskname)||"考查组".equals(taskname)){
        	//获取部门信息,将申请人有多个部门时，具体是以哪个部门名义申请的，这数据保存在变量dept里;
        	
        	UserLinkDept userLinkDept=(UserLinkDept) delegateTask.getVariable("dept");

        	//考核组的流向是根据申请人二级部门或者 一级部门进行分流的（大部门是根据二级部门，一些部门比较特殊，根据一级部门进行分流 ）

        	//String[] deptnames={userLinkDept.getFirstLevel().getName(),userLinkDept.getSecondLevel().getName()};
        	
        	String groupname=getGroupname(userLinkDept);
            
        	candidateGroup=sysPowerService.selectGroupIdWithGroupName(groupname);

        	delegateTask.setVariable("overseerOne",groupname);

        }
        if("考核组审批".equals(taskname)||"考核主任审批".equals(taskname)||"考查主任审批".equals(taskname)){
   
        	String lastGroupname=(String)delegateTask.getVariable("overseerOne");
  
        	if(lastGroupname==null){
        		//获取部门信息,将申请人有多个部门时，具体是以哪个部门名义申请的，这数据保存在变量dept里;
            	
            	UserLinkDept userLinkDept=(UserLinkDept) delegateTask.getVariable("dept");
            	lastGroupname=getGroupname(userLinkDept);
        	}
        	candidateGroup=sysPowerService.selectGroupIdWithGroupName(lastGroupname.replace("组","主任组"));

        }
        if("考核办初审".equals(taskname)||"考核办".equals(taskname)){
        	candidateGroup=sysPowerService.selectGroupIdWithGroupName("考核办");
        }
        if("考核办审批".equals(taskname)||"考核办主任".equals(taskname)){
        	candidateGroup=sysPowerService.selectGroupIdWithGroupName("考核办主任");
        }
        if(taskname.contains("内可撤回")){
        	candidateGroup="无";
        }
		processBean.setCurrentCandidateGroup(candidateGroup);
		processService.updateProcess(processBean);
		delegateTask.addCandidateGroup(candidateGroup);
 		
     	
	}
	/**
	 * 获取考核组
	 * @return
	 */
    public String getGroupname(UserLinkDept userLinkDept){
    	
    	//考核组的流向是根据申请人二级部门或者 一级部门进行分流的（大部门是根据二级部门，一些部门比较特殊，根据一级部门进行分流 ）

    	String[] deptnames={userLinkDept.getFirstLevel().getName(),userLinkDept.getSecondLevel().getName()};
    	
    	String groupname=sysPowerService.getGroupNameWithDepartmentNameAndGroupType(deptnames,"考核组");
        if(groupname==null){
        	String depts="";
    		for (String string : deptnames) {
    			depts+=string+",";
    		}
        	throw new RuntimeException("["+depts+"] 这个部门,没有设置与之对应的 考核组，请与管理人员联系");
        }
		return groupname;
    	
    }
	@Override
	
	public void notify(DelegateExecution execution) {
		/**
		 * execution.getCurrentFlowElement().getName()对应bpmn流程图  <userTask id="usertask1" name="提交申请"></userTask>中的name值
		 * execution.getCurrentFlowElement().getId()对应的是id值
		 */
        
		if(activitiService!=null){
			activitiService.setTaskCandidateGroup(execution.getProcessInstanceId(), execution.getCurrentFlowElement().getId(), execution.getCurrentFlowElement().getName());
		}

		
	}


}
