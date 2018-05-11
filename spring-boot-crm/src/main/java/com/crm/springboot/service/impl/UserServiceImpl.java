package com.crm.springboot.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.DeptIdentityLink;
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

	@Override
//	@CachePut(key="#t.id")
	public User save(User user) {
		
		userMapper.save(user);
		activitiService.saveUser(user.getId());
		return user;
	}

	@Override
//	@CacheEvict(key="#id")
	public void deleteById(Serializable id) {
	
		userMapper.deleteById(id);
	}

	@Override
//	@CachePut(key="#t.id")
	
	public User update(User user) {
		
		userMapper.update(user);
		
		return user;
	}

	@Override
//	@Cacheable(key="#id")
	public User getById(String id) {
		
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
		
		return null;
	}


	@Override
	public void deleteUsersByUserIds(String[] ids) {
		
		userMapper.deleteUsersByUserIds(ids);
		
	}


	@Override
	public String[] getDeptNames(User user) {
		List<UserLinkDept> userLinkDepts=user.getUserLinkDepts();
		
		List<String> dept=new ArrayList<String>();
		for (int i = 0; i < userLinkDepts.size(); i++) {
			
			if(!dept.contains((userLinkDepts.get(i)).getFirstLevel().getName())){
				dept.add((userLinkDepts.get(i)).getFirstLevel().getName());
			}
			if(!dept.contains((userLinkDepts.get(i)).getSecondLevel().getName())){
				dept.add((userLinkDepts.get(i)).getSecondLevel().getName());
			}
		}
		String[] deptNames=new String[dept.size()];
		dept.toArray(deptNames);

		return deptNames;
	}

	@Override
	public void saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(HashMap<String, Object> params) {
		userMapper.saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(params);
		
	}

	@Override
	public void saveDept(Dept dept) {
		userMapper.saveDept(dept);
		
	}

	@Override
	public void saveDeptIdentityLink(DeptIdentityLink deptIdentityLink) {
		userMapper.saveDeptIdentityLink(deptIdentityLink);
		
	}
	@Override
	public void saveUserLinkDept(UserLinkDept userLinkDept) {
		
		this.saveUserLinkDept(userLinkDept.getUserId(), userLinkDept.getFirstLevelIds(), String.valueOf(userLinkDept.getSecondLevel().getDid()), String.valueOf(userLinkDept.getThirdLevel().getDid()));
	}


	@Override
	public void saveUserLinkDept(Integer userId, String firstLevelId, String secondLevelId, String thirdLevelId) {
		UserLinkDept userLinkDept=new UserLinkDept();
		userLinkDept.setUserId(userId);

		//第二列为secondLevel，次级部门
		if(!"".equals(secondLevelId)){
			Dept secondLevel=new Dept();
			secondLevel.setDid(Integer.valueOf(secondLevelId));
			userLinkDept.setSecondLevel(secondLevel);
		}
		
		//第三列为thirdLevel，第三级部门
		if(!"".equals(thirdLevelId)){
			Dept thirdLevel=new Dept();
            thirdLevel.setDid(Integer.valueOf(thirdLevelId));
			userLinkDept.setThirdLevel(thirdLevel);
		}
	    if(firstLevelId!=null&&!"".equals(firstLevelId)){
	    	
	    	HashMap<String, Object> params=new HashMap<String, Object>();
	    	//第一列为firstLevel，最底层部门
	    	String[] firstLevelIds=firstLevelId.split(",");

			params.put("firstLevelIds", firstLevelIds);
			params.put("userLinkDept", userLinkDept);
			userMapper.saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(params);
	    }else{
	    	userMapper.saveUserLinkDeptWithUserLinkDept(userLinkDept);
	    }
		
				
	}

	@Override
	public List<User> selectAllUserWithHashMap(HashMap<String, Object> params) {
		
		return userMapper.selectAllUserWithHashMap(params);
	}

	@Override
	public List<Dept> selectDistinctSecondLevelDept() {
		
		return userMapper.selectDistinctSecondLevelDept();
	}

	@Override
	public List<Dept> selectDistinctFirstLevelDept(HashMap<String, Object> params){
		
		return userMapper.selectDistinctFirstLevelDept(params);
	}

	@Override
	public List<UserLinkDept> selectUserLinkDeptWithResutltType(HashMap<String, Object> params) {
		
		return userMapper.selectUserLinkDeptWithResutltType(params);
	}

	@Override
	public void deleteUserLinkDept(HashMap<String, Object> params) {
		userMapper.deleteUserLinkDept(params);
		
	}

	@Override
	public List<String> selectAllUserLinkDeptIds(HashMap<String, Object> params) {
		
		return userMapper.selectAllUserLinkDeptIds(params);
	}

	@Override
	public void deleteUserLinkDeptByIds(String[] ids) {
		userMapper.deleteUserLinkDeptByIds(ids);
		
	}

	@Override
	public UserLinkDept getSingleUserLinkDept(User user, String deptName) {
		
		UserLinkDept result=null;
		List<UserLinkDept> userLinkDepts=user.getUserLinkDepts();
		
		
		if(userLinkDepts==null && userLinkDepts.size()==0) return null;
		
		if(userLinkDepts.size()==1){
			if(deptName==null) return userLinkDepts.get(0);
			
		}
		if(deptName==null) return null;
		
		for (UserLinkDept userLinkDept : userLinkDepts) {
			if(userLinkDept.getFirstLevel().getName().equals(deptName)){
				result=userLinkDept;break;
			}
		}
		if(result==null){
			for (UserLinkDept userLinkDept : userLinkDepts) {
				if(userLinkDept.getSecondLevel().getName().equals(deptName)){
					result=userLinkDept;break;
				}
			}
		}
		
        
		return result;
	}









}
