package com.crm.springboot.pojos.user;

public class DeptIdentityLink {
	private Integer id;
	private Dept firstLevel;
	private Dept secondLevel;
	private Dept thirdLevel;
	
	
	@Override
	public String toString() {
		return "DeptIdentityLink [id=" + id + ", firstLevel=" + firstLevel + ", secondLevel=" + secondLevel
				+ ", thirdLevel=" + thirdLevel + "]";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
