package com.crm.springboot.pojos.assess;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.crm.springboot.pojos.FormField;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.user.User;

public class Evaluation implements Serializable{
	private Integer eId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	private ProcessBean processBean;
	private String selfEvaluation;
	private String attendance;
	private String overseerEvaluation;
	private String leadershipEvaluation;
	private String publicEvaluation;
	private String organizationEvaluation;
	private String totalMark;
	private String evaluationType;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String sparation;
	private String leadershipRemark;
	private String shortComesAndPlan;
	private List<Project> projects;
	//判断流程是否已经提交 
	private String committed;
	



	public String getCommitted() {
		return committed;
	}


	public void setCommitted(String committed) {
		this.committed = committed;
	}


	public String getLeadershipRemark() {
		return leadershipRemark;
	}


	public void setLeadershipRemark(String leadershipRemark) {
		this.leadershipRemark = leadershipRemark;
	}


	public String getShortComesAndPlan() {
		return shortComesAndPlan;
	}


	public void setShortComesAndPlan(String shortComesAndPlan) {
		this.shortComesAndPlan = shortComesAndPlan;
	}


	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	public Integer geteId() {
		return eId;
	}
	public void seteId(Integer eId) {
		this.eId = eId;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	

	public ProcessBean getProcessBean() {
		return processBean;
	}


	public void setProcessBean(ProcessBean processBean) {
		this.processBean = processBean;
	}


	public String getSelfEvaluation() {
		return selfEvaluation;
	}
	public void setSelfEvaluation(String selfEvaluation) {
		this.selfEvaluation = selfEvaluation;
	}
	public String getAttendance() {
		return attendance;
	}
	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}
	public String getOverseerEvaluation() {
		return overseerEvaluation;
	}
	public void setOverseerEvaluation(String overseerEvaluation) {
		this.overseerEvaluation = overseerEvaluation;
	}
	public String getLeadershipEvaluation() {
		return leadershipEvaluation;
	}
	public void setLeadershipEvaluation(String leadershipEvaluation) {
		this.leadershipEvaluation = leadershipEvaluation;
	}
	public String getPublicEvaluation() {
		return publicEvaluation;
	}
	public void setPublicEvaluation(String publicEvaluation) {
		this.publicEvaluation = publicEvaluation;
	}
	public String getOrganizationEvaluation() {
		return organizationEvaluation;
	}
	public void setOrganizationEvaluation(String organizationEvaluation) {
		this.organizationEvaluation = organizationEvaluation;
	}
	public String getTotalMark() {
		return totalMark;
	}
	public void setTotalMark(String totalMark) {
		this.totalMark = totalMark;
	}
	public String getEvaluationType() {
		return evaluationType;
	}
	public void setEvaluationType(String evaluationType) {
		this.evaluationType = evaluationType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSparation() {
		return sparation;
	}
	public void setSparation(String sparation) {
		this.sparation = sparation;
	}

	
	
}
