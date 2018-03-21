package com.crm.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.lang.UsesJava7;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.crm.mapper.UserMapper;
import com.crm.pojos.Dept;
import com.crm.pojos.User;

public class MyBatisTest {

	public static void main(String[] args) throws Exception{
		InputStream inputStream=Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		Configuration con=sqlSessionFactory.getConfiguration();
		
		
		//�������mybatis�еı���Alias
//		Map<String, Class<?>> typeMap = con.getTypeAliasRegistry().getTypeAliases();
//		for(Entry<String, Class<?>> entry: typeMap.entrySet()) {
//		    System.out.println(entry.getKey() + " ================> " + entry.getValue());
//		}
//		
		
		User user=new User();
		Dept dept=new Dept();
		UserMapper uMapper=session.getMapper(UserMapper.class);
		//����
//		user.setLoginname("����");
//		user.setPassword("123456");
//		dept.setId(101);
//		user.setDept(dept);
//		user.setPhone("1235");
//		session.insert("saveUser",user);
//		
		//������ѯ
		
		user=uMapper.selectUserById(10000);
		System.out.println(user.toString());
		user.setLoginname("��");
		user.setPhone("456");
		uMapper.updateUser(user);
//		uMapper.deleteUserById(10005);
		session.commit();
		session.close();
		
		session=sqlSessionFactory.openSession();
		uMapper=session.getMapper(UserMapper.class);
		User user2=uMapper.selectUserById(10000);
		System.out.println(user2.toString());
		session.commit();
		session.close();
		//������ѯ

//		HashMap<String, Object> pram=new HashMap<String, Object>();
//		pram.put("did",100);
//		List<User> users=uMapper.selectUsersLike(pram);
//		for(User u:users) System.out.println(u.toString());
		
		//ģ����ѯ
//		user.setLoginname("��");
//		List<User> users=uMapper.selectUserLikeName(user);
//		for(User u:users) System.out.println(u.toString());
//		
		//����
//		user.setLoginname("��");
//		user.setPassword("1");
//		session.update("updateUser", user);
//		user=session.selectOne("selectUser", 10);
//		System.out.println(user.toString());
		

		
		//��ѯ����
//		dept=session.selectOne("selectDeptWithId", 100);
//		System.out.println(dept.toString());
		
		//���ݲ���ID��ѯ�����û�
//		List<User> users=session.selectList("selectUsersWithDeptId", 100);
//		for(User u:users) System.out.println(u.toString());
		
		//��ѯ���в��ŵ���Ϣ
//		List<Dept> dept_list=session.selectList("selectAllDept");
//		for(Dept d:dept_list){
//			System.out.println(d);
//			List<User> user_list=d.getUsers();
//			System.out.println("user_list="+user_list.size());
//			for(User u:user_list) System.out.println(u.toString());
//			
//		}
		
//		session.close();

	}

}
