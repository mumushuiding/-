package com.crm.springboot.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.pojos.user.UserLinkPost;
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
	public <T> T getById(String id) {
		
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

	@Override
	public List<Dept> selectAllDept() {
		
		return userMapper.selectAllDept();
	}

	@Override
	public List<Post> selectAllPost() {
		
		return userMapper.selectAllPost();
	}

	@Override
	public List<UserLinkPost> selectUserLinkPostWithUserId(Serializable userId) {
		
		return userMapper.selectUserLinkPostWithUserId(userId);
	}

	@Override
	public void saveUserLinkPost(HashMap<String, Object> params) {
		
		userMapper.saveUserLinkPostWithUserIdAndPostIds(params);
		
	}

	@Override
	public List<UserLinkDept> selectUserLinkDeptWithUserId(Serializable userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUserLinkDept(HashMap<String, Object> params) {
		
		userMapper.saveUserLinkDeptWithUserIdAndDeptIds(params);
		
	}

	@Override
	public void saveUserWithPostIdsAndDeptIds(User user) {
		this.save(user);
		
		if(user.getId()!=0){
			if(user.getDeptIds()!=null){
				HashMap<String , Object> params1=new HashMap<String, Object>();
				params1.put("userId", user.getId());
				params1.put("deptIds", (user.getDeptIds()).split(","));
				 this.saveUserLinkDept(params1);
				
			}
			if(user.getPostIds()!=null){
				HashMap<String, Object> params2=new HashMap<String, Object>();
				params2.put("userId", user.getId());
				params2.put("postIds",(user.getPostIds()).split(","));
				this.saveUserLinkPost(params2);
			}
			
	       
	        
		}
		
	}

	@Override
	public void deleteUsersByUserIds(String[] ids) {
		
		userMapper.deleteUsersByUserIds(ids);
		
	}




}
