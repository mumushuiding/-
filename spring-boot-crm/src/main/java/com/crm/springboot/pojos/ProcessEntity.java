package com.crm.springboot.pojos;

import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.user.User;

public class ProcessEntity {
	private String processInstanceId;
	private User user;
	private Evaluation evaluation;
    private ProcessBean processBean;
    
	public ProcessBean getProcessBean() {
		return processBean;
	}
	public void setProcessBean(ProcessBean processBean) {
		this.processBean = processBean;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Evaluation getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	
	
}
