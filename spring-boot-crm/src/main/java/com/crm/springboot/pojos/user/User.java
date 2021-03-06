package com.crm.springboot.pojos.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.crm.springboot.pojos.GroupManager;
import com.fasterxml.jackson.annotation.JsonFormat;


public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3352357640051007148L;
	private Integer id;
	private String loginname;
	private String password;
	private String username;
	private String sex;
    private List<UserLinkDept> userLinkDepts;
    //员工职级
    private Post post;
    //员工的职务
    private String position;
    //用户所属的审批管理组
    private List<GroupManager> userLinkGroup;
    private String email;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    
    private String phone;
    
    private String avatar;
    
    private UserLinkDept userLinkDept;//主要是注册时接收员工部门属性，没有其它作用
    
    private String deptIds;////主要是接收注册时员工的部门信息;
    
    private int retire;
    
	@Override
	public String toString() {
		return "User [id=" + id + ", loginname=" + loginname + ", password=" + password + ", username=" + username
				+ ", sex=" + sex + ", userLinkDepts=" + userLinkDepts + ", post=" + post + ", position=" + position
				+ ", userLinkGroup=" + userLinkGroup + ", email=" + email + ", birthday=" + birthday + ", phone="
				+ phone + ", avatar=" + avatar + ", userLinkDept=" + userLinkDept + ", deptIds=" + deptIds + "]";
	}



	public int getRetire() {
		return retire;
	}



	public void setRetire(int retire) {
		this.retire = retire;
	}



	public String getPosition() {
		return position;
	}



	public void setPosition(String position) {
		this.position = position;
	}



	public String getDeptIds() {
		return deptIds;
	}



	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}



	public String getAvatar() {
		return avatar;
	}



	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}



	public void setUserLinkDept(UserLinkDept userLinkDept) {
		this.userLinkDept = userLinkDept;
	}



	public  User() {
		super();
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


	
	public List<UserLinkDept> getUserLinkDepts() {
		return userLinkDepts;
	}



	public void setUserLinkDepts(List<UserLinkDept> userLinkDepts) {
		this.userLinkDepts = userLinkDepts;
	}



	public UserLinkDept getUserLinkDept() {
		return userLinkDept;
	}



	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public List<GroupManager> getUserLinkGroup() {
		return userLinkGroup;
	}

	public void setUserLinkGroup(List<GroupManager> userLinkGroup) {
		this.userLinkGroup = userLinkGroup;
	}


	
}
