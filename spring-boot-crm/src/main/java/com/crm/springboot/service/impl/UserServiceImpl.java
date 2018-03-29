package com.crm.springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames="user")
@Service("userService")
public class UserServiceImpl implements UserService{
	
    @Autowired
    private UserMapper userMapper;

	@Override
	@CachePut(key="#t.id")
	public <T> T save(T t) {
		userMapper.save(t);
		return t;
	}

	@Override
	@CacheEvict(key="#id")
	public void deleteById(Integer id) {
	
		userMapper.deleteById(id);
	}

	@Override
	@CachePut(key="#t.id")
	public <T> T update(T t) {
		userMapper.update(t);
		return t;
	}

	@Override
	@Cacheable(key="#id")
	public <T> T getById(Integer id) {
		
		return userMapper.getById(id);
	}

	@Override
	public boolean checkLogin(User user) {
		//登陆名登陆
		
		//手机号登陆
		
		return true;
	}


}
