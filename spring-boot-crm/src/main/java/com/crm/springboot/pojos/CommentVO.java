package com.crm.springboot.pojos;

import java.io.Serializable;

public class CommentVO implements Serializable{
	//评论人
	private String username;
	
	//评论内容
	private String comment;
	
	//是否通过
	private boolean audit;
	//评论时间
	private String time;

	public String getUsername() {
		return username;
	}
    
	public void setUsername(String username) {
		this.username = username;
	}



	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isAudit() {
		return audit;
	}

	public void setAudit(boolean audit) {
		this.audit = audit;
	}
	
	

}
