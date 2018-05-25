package com.crm.springboot.config.activitiServiceTask;

import java.util.HashMap;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.utils.DateUtil;
@Service
public class EvaluationServiceTaskForMonth implements JavaDelegate{
    @Autowired
    private ProcessService processService;
    @Autowired
    private ResponsibilityService responsibilityService;
	@Override
	public void execute(DelegateExecution execution) {
		
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("processInstanceId",execution.getProcessInstanceId());

		ProcessBean processBean=processService.selectSingleProcessBean(params);
		if(processBean==null) return;
			
		Evaluation evaluation=responsibilityService.selectSingleEvaluation(params);
		//根据月度考核最终组织的评分，自动添加项目并加上相应的分数
		responsibilityService.organizationAssessForMonth(evaluation);
		//设置与该流程相关的所有评分为已经审核
		HashMap<String, Object> params2 = new HashMap<String,Object>();
		params2.put("userId", String.valueOf(processBean.getUser().getId()));
		params2.put("startDate", DateUtil.formatDefaultDate(evaluation.getStartDate()));
		params2.put("endDate",DateUtil.formatDefaultDate(evaluation.getEndDate()));
		responsibilityService.updateMarksAsChecked(params2);
	}

}
