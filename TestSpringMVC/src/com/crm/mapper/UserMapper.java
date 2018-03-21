package com.crm.mapper;

import java.util.HashMap;
import java.util.List;

import com.crm.pojos.User;

public interface UserMapper {
	/**
	 * 根据id查询User,
	 * 方法名和参数必须和XML文件中的<select../>元素的id属性和prameterType属性一致
	 */
	User selectUserById(Integer id);
	/**
	 * 根据用户id删除
	 * @param pram
	 * @return
	 */
	void deleteUserById(Integer id);
	/**
	 * 更新用户
	 * @param user
	 */
	void updateUser(User user);
	/**
	 * 根据输入的参数值，自动拼接动态生成sql语句
	 * @param pram
	 * @return
	 */

	List<User> selectUsersLike(HashMap<String, Object> pram);
	/**
	 * 模糊查询
	 * @param user
	 * @return
	 */
	List<User> selectUserLikeName(User user);
}
