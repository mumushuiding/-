package com.crm.springboot.pojos;

import java.util.Date;

public class ProcessVO {
	private String id;
	private String processInstanceId;
	private String startUserId;
	private String startTime;
	private String processDefinitionName;
	private String name;
	private Integer processDefinitionVersion;
	private String superExecutionId;
	
	
	public String getSuperExecutionId() {
		return superExecutionId;
	}
	public void setSuperExecutionId(String superExecutionId) {
		this.superExecutionId = superExecutionId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getStartUserId() {
		return startUserId;
	}
	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getProcessDefinitionName() {
		return processDefinitionName;
	}
	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}
	public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}
	
	
}
