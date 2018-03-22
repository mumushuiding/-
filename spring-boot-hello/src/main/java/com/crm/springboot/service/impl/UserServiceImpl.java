package com.crm.springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.User;
import com.crm.springboot.service.UserService;

/**
 * User服务接口实现类
 * @Service("userService")用于当前类注释为一个Spring的bean,名为userService
 * @author Administrator
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService{
	@Autowired
	private UserMapper userMapper;

	@Override
	public User login(User user) {
		
		return userMapper.findUserWithNameAndPass(user.getLoginname(),user.getPassword());
	}

	@Override
	public User getUserById(int id) {

		return userMapper.selectUserById(id);
	}
	

}
