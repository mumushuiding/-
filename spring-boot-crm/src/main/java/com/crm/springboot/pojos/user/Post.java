package com.crm.springboot.pojos.user;

import java.io.Serializable;

public class Post implements Serializable{
	private Integer pId;
	private String name;
	public Integer getpId() {
		return pId;
	}
	public void setpId(Integer pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
