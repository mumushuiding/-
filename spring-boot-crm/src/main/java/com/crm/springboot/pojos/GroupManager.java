package com.crm.springboot.pojos;

import java.io.Serializable;
//该表纪录着每个用户所属于的group，可以一个用户对应多个group
import java.util.Date;
import java.util.List;

import com.crm.springboot.pojos.user.User;

public class GroupManager implements Serializable{
	private Integer groupmanagerid;
	private Integer userid;
	private String username;
	private GroupTable groupTable;
	private Integer creatorid;
	private String creatorname;
	private Date createdate;
	
	private String ids;//做为接收页面传递多个Id值的容器，没有其它作用
	
	private User user;
	private String groupid;//同步act_id_membership表格时用
	@Override
	public String toString() {
		return "GroupManager [groupmanagerid=" + groupmanagerid + ", userid=" + userid + ", username=" + username
				+ ", group=" + groupTable + ", creatorid=" + creatorid + ", creatorname=" + creatorname + ", createdate="
				+ createdate + "]";
	}
	public Integer getGroupmanagerid() {
		return groupmanagerid;
	}
	public void setGroupmanagerid(Integer groupmanagerid) {
		this.groupmanagerid = groupmanagerid;
	}
	public Integer getUserid() {
		
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public GroupTable getGroupTable() {
		return groupTable;
	}
	public void setGroupTable(GroupTable groupTable) {
		this.groupTable = groupTable;
	}
	public Integer getCreatorid() {
		return creatorid;
	}
	public void setCreatorid(Integer creatorid) {
		this.creatorid = creatorid;
	}
	
	public String getCreatorname() {
		return creatorname;
	}
	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	
}
