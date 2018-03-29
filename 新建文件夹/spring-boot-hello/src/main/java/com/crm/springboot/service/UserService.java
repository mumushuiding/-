package com.crm.springboot.service;

import com.crm.springboot.pojos.User;

public interface UserService extends BaseService<User> {
	/**
	 * 登陆检查
	 * @param user
	 * @return
	 */
	public boolean checkLogin(User user);
}
