package com.lt.dubbo.domain;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5868238083332429410L;
	private Integer id;
	private String username;
	private String loginnam;
	
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", loginnam=" + loginnam + "]";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLoginnam() {
		return loginnam;
	}
	public void setLoginnam(String loginnam) {
		this.loginnam = loginnam;
	}
	
	
	
}
