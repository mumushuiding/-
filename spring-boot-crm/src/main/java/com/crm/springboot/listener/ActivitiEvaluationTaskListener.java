package com.crm.springboot.listener;

import java.util.HashMap;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.ResponsibilityService;
@Service
public class ActivitiEvaluationTaskListener implements TaskListener{
    @Autowired
    private ProcessService processService;
    @Autowired 
    private ResponsibilityService responsibilityService;
	@Override
	public void notify(DelegateTask delegateTask) {
	/**
	 * delegateTask.getName()对应bpmn流程图  <userTask id="usertask1" name="提交申请"></userTask>中的name值
	 * delegateTask.getTaskDefinitionKey()对应的是id值
	 * 
	 */
	
	HashMap<String, Object> params=new HashMap<String, Object>();
	params.put("processInstanceId",delegateTask.getProcessInstanceId());

	ProcessBean processBean=processService.selectSingleProcessBean(params);
	if(processBean==null) return;

	if("提交申请".equals(delegateTask.getName())){
		
		processBean.setCommitted("0");
		
		processService.updateProcess(processBean);
	}

	
	}

}
