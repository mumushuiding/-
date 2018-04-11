package com.crm.springboot.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.UserService;
/**
 * User服务接口实现类
 * @Service("userService")用于当前类注释为一个Spring的bean,名为userService
 * @author Administrator
 *
 */
//@CacheConfig(cacheNames="user")
@Service("userService")
public class UserServiceImpl implements UserService{
	
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ActivitiService activitiService;

//	@Override
//	@CachePut(key="#t.id")
	public <T> T save(T t) {
		User user=(User) t;
		userMapper.save(t);
		activitiService.saveUser(user.getId());
		return t;
	}

//	@Override
//	@CacheEvict(key="#id")
	public void deleteById(Serializable id) {
	
		userMapper.deleteById(id);
	}

//	@Override
//	@CachePut(key="#t.id")
	public <T> T update(T t) {
		userMapper.update(t);
		return t;
	}

//	@Override
//	@Cacheable(key="#id")
	public <T> T getById(Serializable id) {
		
		return userMapper.getById(id);
	}

	@Override
	public List<User> getBySomething(User user) {
		
		return userMapper.getBySomething(user);
	}

	@Override
	public List<User> selectAllUser() {
		
		return userMapper.selectAllUser();
	}


}
