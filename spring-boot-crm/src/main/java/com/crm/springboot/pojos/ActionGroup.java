package com.crm.springboot.pojos;

import java.util.Date;

public class ActionGroup {
	private Integer actiongroupid;
	private Action action;
	private Integer groupid;
	private Integer creatorid;
	private String creatorname;
	private Date createdate;
	
	private String ids;//做为接收页面传递多个Id值的容器，没有其它作用
	
	public Integer getActiongroupid() {
		return actiongroupid;
	}
	public void setActiongroupid(Integer actiongroupid) {
		this.actiongroupid = actiongroupid;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	
	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	public Integer getCreatorid() {
		return creatorid;
	}
	public void setCreatorid(Integer creatorid) {
		this.creatorid = creatorid;
	}
	public String getCreatorname() {
		return creatorname;
	}
	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	
	
}
