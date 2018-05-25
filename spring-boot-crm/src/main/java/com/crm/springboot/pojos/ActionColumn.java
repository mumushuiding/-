package com.crm.springboot.pojos;

import java.util.List;


public class ActionColumn {
	private Integer actioncolumnid;
	private String actioncolumnname;
	private List<Action> actions;
	
	
	@Override
	public String toString() {
		return "ActionColumn [actioncolumnid=" + actioncolumnid + ", actioncolumnname=" + actioncolumnname
				+ ", actions=" + actions + "]";
	}
	public Integer getActioncolumnid() {
		return actioncolumnid;
	}
	public void setActioncolumnid(Integer actioncolumnid) {
		this.actioncolumnid = actioncolumnid;
	}
	public String getActioncolumnname() {
		return actioncolumnname;
	}
	public void setActioncolumnname(String actioncolumnname) {
		this.actioncolumnname = actioncolumnname;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	
	
}
