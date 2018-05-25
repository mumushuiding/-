package com.crm.springboot.pojos.user;

import java.io.Serializable;

public class UserLinkDept implements Serializable{
	private Integer id;
	private Integer userId;
	private Dept firstLevel;
	private Dept secondLevel;
	private Dept thirdLevel;
	
    private String firstLevelIds;//做为接收页面传递多个最底层部门Id值的容器，没有其它作用
	
    
    
	@Override
	public String toString() {
		return "UserLinkDept [id=" + id + ", userId=" + userId + ", firstLevel=" + firstLevel + ", secondLevel="
				+ secondLevel + ", thirdLevel=" + thirdLevel + ", firstLevelIds=" + firstLevelIds + "]";
	}

	public String getFirstLevelIds() {
		return firstLevelIds;
	}
	public void setFirstLevelIds(String firstLevelIds) {
		this.firstLevelIds = firstLevelIds;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Dept getFirstLevel() {
		return firstLevel;
	}
	public void setFirstLevel(Dept firstLevel) {
		this.firstLevel = firstLevel;
	}
	public Dept getSecondLevel() {
		return secondLevel;
	}
	public void setSecondLevel(Dept secondLevel) {
		this.secondLevel = secondLevel;
	}
	public Dept getThirdLevel() {
		return thirdLevel;
	}
	public void setThirdLevel(Dept thirdLevel) {
		this.thirdLevel = thirdLevel;
	}
	


	
	
}
