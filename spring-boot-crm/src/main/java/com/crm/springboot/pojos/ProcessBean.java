package com.crm.springboot.pojos;

import java.io.Serializable;
import java.util.List;

import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;


public class ProcessBean implements Serializable{

	
	    //对应的流程实例Id
	    private String processInstanceId;
	    //申请日期
		private String requestedDate;
        
		private User user;
		//申请的标题
		private String title;

		
		//单据类型
		private String businessType;
		//对应的流程实例executionId
		private String executionId;
		//对应的部署文件Id
		
		private String deploymentId;
		//用来暂存taskI
		private String taskId;
		//对应流程的key
		private String businessKey;
		
		private String firstTaskId;
		//部门，主要是当一个用户属于多个部门时，该参数决定，目前是以哪个部门身份提交的

		private String deptName;
        private String completed;
        private String committed;
		private String currentCandidateGroup;

		
		
		@Override
		public String toString() {
			return "ProcessBean [processInstanceId=" + processInstanceId + ", requestedDate=" + requestedDate
					+ ", user=" + user + ", title=" + title + ", businessType=" + businessType + ", executionId="
					+ executionId + ", deploymentId=" + deploymentId + ", taskId=" + taskId + ", businessKey="
					+ businessKey + ", firstTaskId=" + firstTaskId + ", deptName=" + deptName + ", completed="
					+ completed + ", committed=" + committed + ", currentCandidateGroup=" + currentCandidateGroup + "]";
		}
		
		public String getCurrentCandidateGroup() {
			return currentCandidateGroup;
		}
		public void setCurrentCandidateGroup(String currentCandidateGroup) {
			this.currentCandidateGroup = currentCandidateGroup;
		}
		public String getCommitted() {
			return committed;
		}
		public void setCommitted(String committed) {
			this.committed = committed;
		}
		public String getFirstTaskId() {
			return firstTaskId;
		}
		public void setFirstTaskId(String firstTaskId) {
			
			this.firstTaskId = firstTaskId;
		}
		public String getDeptName() {
			return deptName;
		}
		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}
		public String getRequestedDate() {
			return requestedDate;
		}
		public void setRequestedDate(String requestedDate) {
			this.requestedDate = requestedDate;
		}

		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getBusinessType() {
			return businessType;
		}
		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}
		public String getProcessInstanceId() {
			return processInstanceId;
		}
		public void setProcessInstanceId(String processInstanceId) {
			this.processInstanceId = processInstanceId;
		}
		public String getExecutionId() {
			return executionId;
		}
		public void setExecutionId(String executionId) {
			this.executionId = executionId;
		}
		public String getDeploymentId() {
			return deploymentId;
		}
		public void setDeploymentId(String deploymentId) {
			this.deploymentId = deploymentId;
		}
		public String getBusinessKey() {
			return businessKey;
		}
		public void setBusinessKey(String businessKey) {
			this.businessKey = businessKey;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public String getCompleted() {
			return completed;
		}
		public void setCompleted(String completed) {
			this.completed = completed;
		}
		public String getTaskId() {
			return taskId;
		}
		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}

}
