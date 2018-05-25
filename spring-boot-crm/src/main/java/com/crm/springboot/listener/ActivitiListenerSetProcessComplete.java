package com.crm.springboot.listener;

import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.service.ProcessService;
@Service
public class ActivitiListenerSetProcessComplete implements ExecutionListener{

	@Autowired
	private ProcessService processService;


	@Override
	public void notify(DelegateExecution execution) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("processInstanceId",execution.getProcessInstanceId());
		ProcessBean processBean=processService.selectAllProcess(params).get(0);
		processBean.setCompleted("1");
		processService.updateProcess(processBean);
	}

}
