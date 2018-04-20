package com.crm.springboot.pojos;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;


public class User implements Serializable{
	private Integer id;
	private String loginname;
	private String password;
	private String username;
	private Integer sex;
    private Dept dept;
    private String email;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    
    private String phone;
	public  User() {
		super();
		
		
	}
	
	








	@Override
	public String toString() {
		return "User [id=" + id + ", loginname=" + loginname + ", password=" + password + ", username=" + username
				+ ", sex=" + sex + ", dept=" + dept + ", birthday=" + birthday + ", phone=" + phone + "]";
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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}


	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}





	public Dept getDept() {
	
		return dept;
	}





	public void setDept(Dept dept) {
		
		this.dept = dept;
	}





	public String getPhone() {
		return phone;
	}





	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
