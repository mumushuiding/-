package com.crm.springboot.pojos.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.crm.springboot.pojos.GroupManager;
import com.fasterxml.jackson.annotation.JsonFormat;


public class User implements Serializable{
	private Integer id;
	private String loginname;
	private String password;
	private String username;
	private String sex;
    private List<UserLinkDept> userLinkDept;
    private List<UserLinkPost> userLinkPost;
    //用户所属的审批管理组
    private List<GroupManager> userLinkGroup;
    private String email;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    
    private String phone;
    
    private String deptIds;//做为接收页面传递多个Id值的容器，没有其它作用
    private String postIds;//做为接收页面传递多个Id值的容器，没有其它作用
    
    
    
	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	public String getPostIds() {
		return postIds;
	}

	public void setPostIds(String postIds) {
		this.postIds = postIds;
	}

	public  User() {
		super();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", loginname=" + loginname + ", password=" + password + ", username=" + username
				+ ", sex=" + sex + ", userLinkDept=" + userLinkDept + ", userLinkPost=" + userLinkPost + ", email="
				+ email + ", birthday=" + birthday + ", phone=" + phone + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<UserLinkDept> getUserLinkDept() {
		return userLinkDept;
	}

	public void setUserLinkDept(List<UserLinkDept> userLinkDept) {
		this.userLinkDept = userLinkDept;
	}

	public List<UserLinkPost> getUserLinkPost() {
		return userLinkPost;
	}

	public void setUserLinkPost(List<UserLinkPost> userLinkPost) {
		this.userLinkPost = userLinkPost;
	}

	public List<GroupManager> getUserLinkGroup() {
		return userLinkGroup;
	}

	public void setUserLinkGroup(List<GroupManager> userLinkGroup) {
		this.userLinkGroup = userLinkGroup;
	}


	
}
