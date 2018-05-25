package com.crm.springboot.pojos;

public class Action {
	private Integer actionid;
	private String actionname;
	private ActionColumn actionColumn;
	private String action;
	
	
	@Override
	public String toString() {
		return "Action [actionid=" + actionid + ", actionname=" + actionname + ", actionColumn=" + actionColumn
				+ ", action=" + action + "]";
	}
	public Integer getActionid() {
		return actionid;
	}
	public void setActionid(Integer actionid) {
		this.actionid = actionid;
	}
	public String getActionname() {
		return actionname;
	}
	public void setActionname(String actionname) {
		this.actionname = actionname;
	}
	public ActionColumn getActionColumn() {
		return actionColumn;
	}
	public void setActionColumn(ActionColumn actionColumn) {
		this.actionColumn = actionColumn;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
}
