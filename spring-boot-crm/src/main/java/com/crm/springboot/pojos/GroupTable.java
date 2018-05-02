package com.crm.springboot.pojos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class GroupTable implements Serializable{
	private int groupid;
	private String groupname;
	private String groupType;
	private String groupinfo;
	private int creatorid;
	private String creatorname;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createdDate;
	
	private List<ActionGroup> actionGroups;
	
	private String ids;//做为接收页面传递多个Id值的容器，没有其它作用
	
	@Override
	public String toString() {
		return "GroupTable [groupid=" + groupid + ", groupname=" + groupname + ", groupType=" + groupType + ", groupinfo="
				+ groupinfo + ", creatorid=" + creatorid + ", creatorname=" + creatorname + ", createdDate="
				+ createdDate + "]";
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getGroupinfo() {
		return groupinfo;
	}
	public void setGroupinfo(String groupinfo) {
		this.groupinfo = groupinfo;
	}
	public int getCreatorid() {
		return creatorid;
	}
	public void setCreatorid(int creatorid) {
		this.creatorid = creatorid;
	}
	
	
	public String getCreatorname() {
		return creatorname;
	}
	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public List<ActionGroup> getActionGroups() {
		return actionGroups;
	}
	public void setActionGroups(List<ActionGroup> actionGroups) {
		this.actionGroups = actionGroups;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	
	
}
