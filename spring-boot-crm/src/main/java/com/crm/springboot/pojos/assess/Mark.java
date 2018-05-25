package com.crm.springboot.pojos.assess;

import java.io.Serializable;
import java.util.Date;

import com.crm.springboot.pojos.user.User;

public class Mark implements Serializable{
	private Integer markId;
	private Integer projectId;
	private String markNumber;
	private String markReason;
	private String accordingly;
	private Date createTime;
	private Date startDate;
	private Date endDate;
	private String userId;
    private User user;
    private String checked;
    
    private String totalMark;
    
    
	@Override
	public String toString() {
		return "Mark [markId=" + markId + ", projectId=" + projectId + ", markNumber=" + markNumber + ", markReason="
				+ markReason + ", accordingly=" + accordingly + ", createTime=" + createTime + "]";
	}
	
	public String getTotalMark() {
		return totalMark;
	}

	public void setTotalMark(String totalMark) {
		this.totalMark = totalMark;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getMarkId() {
		return markId;
	}
	public void setMarkId(Integer markId) {
		this.markId = markId;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public String getMarkNumber() {
		return markNumber;
	}
	public void setMarkNumber(String markNumber) {
		this.markNumber = markNumber;
	}
	public String getMarkReason() {
		return markReason;
	}
	public void setMarkReason(String markReason) {
		this.markReason = markReason;
	}
	public String getAccordingly() {
		return accordingly;
	}
	public void setAccordingly(String accordingly) {
		this.accordingly = accordingly;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}
	
	
}
