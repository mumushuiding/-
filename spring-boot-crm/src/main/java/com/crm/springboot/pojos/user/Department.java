package com.crm.springboot.pojos.user;

import java.io.Serializable;

public class Department implements Serializable{
	private String did;
	private String name;
	private String firstId;//子部门
	private String secondId;//上级部门
	private String thirdId;//顶级部门
	private String level;//
	private String type;//采编经营类、行政管理等
	public String getName() {
		return name;
	}
	
	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getFirstId() {
		return firstId;
	}
	public void setFirstId(String firstId) {
		this.firstId = firstId;
	}
	public String getSecondId() {
		return secondId;
	}
	public void setSecondId(String secondId) {
		this.secondId = secondId;
	}
	public String getThirdId() {
		return thirdId;
	}
	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
	
	
}
