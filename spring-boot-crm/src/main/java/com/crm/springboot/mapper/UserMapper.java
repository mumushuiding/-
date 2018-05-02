package com.crm.springboot.mapper;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.DeptIdentityLink;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.pojos.user.UserLinkPost;

public interface UserMapper extends BaseMapper<User> {
	List<User> getBySomething(User user);
	
	List<User> selectAllUser();
	
	List<Dept> selectAllDept();
	List<Post> selectAllPost();
    /**
     * ***********************用户***********************************************
     */
	void deleteUsersByUserIds(String[] ids);
	 /**
     * ***********************用户***********************************************
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
	/**
	 * ***********************组织架构（部门之间的上下级关系）*******************************************
	 */
	void saveDeptIdentityLink(DeptIdentityLink deptIdentityLink);
}
