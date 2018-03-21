package com.crm.mapper;

import java.util.HashMap;
import java.util.List;

import com.crm.pojos.User;

public interface UserMapper {
	/**
	 * ����id��ѯUser,
	 * �������Ͳ��������XML�ļ��е�<select../>Ԫ�ص�id���Ժ�prameterType����һ��
	 */
	User selectUserById(Integer id);
	/**
	 * �����û�idɾ��
	 * @param pram
	 * @return
	 */
	void deleteUserById(Integer id);
	/**
	 * �����û�
	 * @param user
	 */
	void updateUser(User user);
	/**
	 * ��������Ĳ���ֵ���Զ�ƴ�Ӷ�̬����sql���
	 * @param pram
	 * @return
	 */

	List<User> selectUsersLike(HashMap<String, Object> pram);
	/**
	 * ģ����ѯ
	 * @param user
	 * @return
	 */
	List<User> selectUserLikeName(User user);
}
