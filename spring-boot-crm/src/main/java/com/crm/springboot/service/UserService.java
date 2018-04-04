package com.crm.springboot.service;

import java.util.List;

import com.crm.springboot.pojos.User;

public interface UserService extends BaseService<User> {
	
	
	public List<User> getBySomething(User user);
}
