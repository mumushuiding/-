package com.crm.springboot.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.DeptIdentityLink;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.pojos.user.UserLinkPost;

public interface UserService{
	
	
	public List<User> getBySomething(User user);
	
	public List<User> selectAllUser();
	public List<Dept> selectAllDept();
	public List<Post> selectAllPost();
	/**
	 * ***********************用户***********************************************
	 */
	void deleteUsersByUserIds(String[] ids);
	User update(User user);

	public User getById(String valueOf);

	void deleteById(Serializable id);

	User save(User user);
	List<User> selectAllUserWithHashMap(HashMap<String, Object> params);
	/**
	 * ***********************部门***********************************************
	 */
	
	void saveDept(Dept dept);
	/**
	 * ***********************用户和职位*******************************************
	 */
	List<UserLinkPost> selectUserLinkPostWithUserId(Serializable userId);
	/**
	 * params包含两个值：userId(用户Id)和postIds(用逗号分隔的职位id字符串)
	 * @param params
	 */
	void saveUserLinkPost(HashMap<String, Object> params);
	
	
	/**
	 * ***********************用户和部门*******************************************
	 */
	String[] getDeptNames(User user);
	List<UserLinkDept> selectUserLinkDeptWithUserId(Serializable userId);
	
	void saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(HashMap<String, Object> params);
	/**
	 * 根据用户Id 和 部门id来更新
	 * @param params
	 */
	void updateUserLinkDeptWithUserIdAndDeptIds(String userId,String deptIds);
	List<Dept> selectDistinctSecondLevelDept();
	//根据二级部门Id查询一级部门
	List<Dept> selectDistinctFirstLevelDept(HashMap<String, Object> params);
	List<UserLinkDept> selectUserLinkDeptWithResutltType(HashMap<String, Object> params);
	void deleteUserLinkDept(HashMap<String, Object> params);
	/**
	 * ***********************组织架构（部门之间的上下级关系）*******************************************
	 */
	void saveDeptIdentityLink(DeptIdentityLink deptIdentityLink);
	void saveUserLinkDept(Integer userId,String firstLevelId,String secondLevelId,String thirdLevelId);
}
