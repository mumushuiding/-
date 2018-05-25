package com.crm.springboot.pojos.user;

import java.io.Serializable;

public class UserLinkPost implements Serializable{
	private int id;
	private int userId;
	private Post post;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	

	
}
