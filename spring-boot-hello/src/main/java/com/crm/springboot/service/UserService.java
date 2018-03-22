package com.crm.springboot.service;

import com.crm.springboot.pojos.User;

public interface UserService {
	public User login(User user);
	
	public User getUserById(int id);
}
