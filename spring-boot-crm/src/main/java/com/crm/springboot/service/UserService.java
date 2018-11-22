package com.crm.springboot.service;

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

public interface UserService{
	
	
	public List<User> getBySomething(User user);
	
	public List<User> selectAllUser();
	public List<Dept> selectAllDept();
	public List<Post> selectAllPost();
	List<User> selectUserAsResultType(HashMap<String, Object> params);
	User selectSingleUserAsResultType(HashMap<String, Object> params);
	User selectSingleUserAsResultType(String phone,String email);
	/**
	 * ***********************用户***********************************************
	 * 
	 */
	List<User> selectUser(HashMap<String, Object> params);
	User selectSingleUser(HashMap<String, Object> params);
	User selectSingleUserWithPhone(String phone);
	void deleteUsersByUserIds(String[] ids);
	void disableUsersByUserIds(String[] ids);
	User update(User user);

	public User getById(String valueOf);

	void deleteById(Serializable id);

	User save(User user);
	List<User> selectAllUserWithHashMap(HashMap<String, Object> params);
	User selectSingleUserWithHashMap(HashMap<String, Object> params);
	String selectUserIdByPhone(String phone);
    //根据用户职级设置审批组
	void saveGroupManagerWithUser(User user);
	User selectUserByUserIdAsType(String id);
	
 	//根据部门名和考核组查询用户
 	List<User> selectUserWithDeptNameAndGroupName(String deptName,String groupName);
 	List<String> selectUserIdWithDeptNameAndGroupName(HashMap<String, Object> params);
 	List<String> selectUserIdWithDeptNameAndGroupName(List<String> deptNames,String groupName);
 	/**
	 * ***********************用户组***********************************************
	 */
 	boolean isContainsGroupLike(User user,String groupnameLike);
	/**
	 * ***********************部门***********************************************
	 */
 	boolean isDeptExists(Department dept);
	List<Dept> selectDeptWithDepartment(Department dept);
	void saveDepartment(Department department);
	void saveDept(Dept dept);
	//获取用户三级部门信息
	UserLinkDept getSingleUserLinkDept(User user,String condition);
	Dept selectSingleDept(HashMap<String, Object> params);
	Dept selectSingleDept(String deptName);
	Dept selectSingleDept(int deptId);
	List<Dept> selectAllDepts(HashMap<String, Object> params);
	HashMap<String, Object> selectSingleDeptAsHashMap(HashMap<String, Object> params);
	List<HashMap<String, Object>> selectAllDeptsAsHashMap(HashMap<String, Object> params);
	
	List<DeptType> selectAllDeptType(HashMap<String, Object> params);
	List<DeptType> selectAllDeptType(int deptId,int dicId);
	
	DeptType selectSingleDeptType(int deptId);
	DeptType selectSingleDeptType(String deptName);
	void saveDeptType(DeptType deptType);
	String getDeptName(User user);
	/**
	 * ***********************用户和职级*******************************************
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
	void saveAndUpdateUserLinkDeptWithUser(User user);
	void saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(HashMap<String, Object> params);
	/**
	 * 根据用户Id 和 部门id来更新
	 * @param params
	 */
	List<Dept> selectDistinctSecondLevelDept();
	//根据二级部门Id查询一级部门
	List<Dept> selectDistinctFirstLevelDept(HashMap<String, Object> params);
	List<UserLinkDept> selectUserLinkDeptWithResutltType(HashMap<String, Object> params);
	
	void deleteUserLinkDept(HashMap<String, Object> params);
	
	//查询所有id
	List<String> selectAllUserLinkDeptIds(HashMap<String, Object> params);
	void deleteUserLinkDeptByIds(String[] ids);
	
	//查询用户人数
	Integer selectCountUserNumber(HashMap<String, Object> params);
	Integer selectCountUserNumberWithFirsDeptId(Integer firstDeptId,String postIds);
	Integer selectCountUserNumberWithSecondDeptId(Integer SecondDeptId,String postIds);
	Integer selectCountUserNumber(Integer deptId,String deptLevel,String postIds);
	
	//根据部门名获取所有子部门名
	List<String> getDeptNames(String deptName,String deptLevel);

	
	List<String> selectSingleColumnFromInfoDept(HashMap<String, Object> params);
	List<String> selectSingleColumnFromInfoDept(String column,String level,String deptName);
	
	List<UserLinkDept> selectAllUserLinkDept(HashMap<String, Object> params);
	UserLinkDept selectSingleUserLinkDept(HashMap<String, Object> params);
	UserLinkDept selectSingleUserLinkDept(String firstDeptName);
	/**
	 * ***********************组织架构（部门之间的上下级关系）*******************************************
	 */
	void saveDeptIdentityLink(DeptIdentityLink deptIdentityLink);
	void saveDeptIdentityLinkWithHashMap(HashMap<String, Object> params);
	void saveDeptIdentityLink(String firstId,String secondId,String thirdId);
	void saveUserLinkDept(UserLinkDept userLinkDept);
	void saveUserLinkDept(Integer userId,String firstLevelId,String secondLevelId,String thirdLevelId);
	//查询所有子部门名
	List<String> selectDistinctFirstDeptNames(HashMap<String, Object> params);
	List<String> selectDistinctFirstDeptNames(Integer secondDeptId);
	
}
