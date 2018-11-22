package com.crm.springboot.mapper;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.user.Department;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.DeptIdentityLink;
import com.crm.springboot.pojos.user.DeptType;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.pojos.user.UserLinkPost;

public interface UserMapper extends BaseMapper<User> {

	
	List<Dept> selectAllDept();
	List<Post> selectAllPost();
	
	
    /**
     * ***********************用户***********************************************
     * 
     * 
     */
	
	List<User> selectUser(HashMap<String, Object> params);

	List<User> getBySomething(User user);
	
	List<User> selectAllUser();
	List<User> selectUserAsResultType(HashMap<String, Object> params);
	void deleteUsersByUserIds(String[] ids);
	void disableUsersByUserIds(String[] ids);
	List<User> selectAllUserWithHashMap(HashMap<String, Object> params);
	
	String selectUserIdByPhone(String phone);
	
	User selectUserByUserIdAsType(String id);
	
	//根据部门名和审批组查询用户
	List<User> selectUserWithDeptNameAndGroupName(HashMap<String, Object> params);
	List<String> selectUserIdWithDeptNameAndGroupName(HashMap<String, Object> params);
	 /**
     * ***********************部门***********************************************
     */
	
	List<Dept> selectDeptWithDepartment(Department department);
	void saveDept(Dept dept);
	void saveDepartment(Department department);
	List<Dept> selectAllDepts(HashMap<String, Object> params);
	List<HashMap<String, Object>> selectAllDeptsAsHashMap(HashMap<String, Object> params);
	List<DeptType> selectAllDeptType(HashMap<String, Object> params);
	void saveDeptType(DeptType deptType);
	List<String> selectSingleColumnFromInfoDept(HashMap<String, Object> params);
	/**
	 * ***********************用户和职位*******************************************
	 */
	List<UserLinkPost> selectUserLinkPostWithUserId(Serializable userId);
	/**
	 * params包含两个值：userId(用户Id)和postIds(用逗号分隔的职位id字符串)
	 * @param params
	 */
	void saveUserLinkPostWithUserIdAndPostIds(HashMap<String, Object> params);
	/**
	 * ***********************用户和部门*******************************************
	 */
	List<UserLinkDept> selectUserLinkDeptWithUserId(Serializable userId);
	/**
	 * params包含两个值：userLinkDept(对象userLinkDept)和最底层部门firstLevelIds(用逗号分隔的职位id字符串)
	 * @param params
	 */
	void saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(HashMap<String, Object> params);
	void saveUserLinkDeptWithUserLinkDept(UserLinkDept userLinkDept);
	List<Dept> selectDistinctSecondLevelDept();
	//根据二级部门Id查询一级部门
	List<Dept> selectDistinctFirstLevelDept(HashMap<String, Object> params);
	//查询用户当前所在一个部门或分管的多个部门
	List<UserLinkDept> selectUserLinkDeptWithResutltType(HashMap<String, Object> params);
	void deleteUserLinkDept(HashMap<String, Object> params);
	List<String> selectAllUserLinkDeptIds(HashMap<String, Object> params);
	List<UserLinkDept> selectAllUserLinkDept(HashMap<String, Object> params);
	void deleteUserLinkDeptByIds(String[] ids);
	
	//查询用户人数
	Integer selectCountUserNumber(HashMap<String, Object> params);
	/**
	 * ***********************组织架构（部门之间的上下级关系）*******************************************
	 */
	void saveDeptIdentityLink(DeptIdentityLink deptIdentityLink);
	void saveDeptIdentityLinkWithHashMap(HashMap<String, Object> params);
	List<String> selectDistinctFirstDeptNames(HashMap<String, Object> params);
	
}
