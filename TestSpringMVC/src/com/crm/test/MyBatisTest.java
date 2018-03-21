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
		
		
		//输出类在mybatis中的别名Alias
//		Map<String, Class<?>> typeMap = con.getTypeAliasRegistry().getTypeAliases();
//		for(Entry<String, Class<?>> entry: typeMap.entrySet()) {
//		    System.out.println(entry.getKey() + " ================> " + entry.getValue());
//		}
//		
		
		User user=new User();
		Dept dept=new Dept();
		UserMapper uMapper=session.getMapper(UserMapper.class);
		//插入
//		user.setLoginname("王五");
//		user.setPassword("123456");
//		dept.setId(101);
//		user.setDept(dept);
//		user.setPhone("1235");
//		session.insert("saveUser",user);
//		
		//单条查询
		
		user=uMapper.selectUserById(10000);
		System.out.println(user.toString());
		user.setLoginname("人");
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
		//批量查询

//		HashMap<String, Object> pram=new HashMap<String, Object>();
//		pram.put("did",100);
//		List<User> users=uMapper.selectUsersLike(pram);
//		for(User u:users) System.out.println(u.toString());
		
		//模糊查询
//		user.setLoginname("王");
//		List<User> users=uMapper.selectUserLikeName(user);
//		for(User u:users) System.out.println(u.toString());
//		
		//更新
//		user.setLoginname("林");
//		user.setPassword("1");
//		session.update("updateUser", user);
//		user=session.selectOne("selectUser", 10);
//		System.out.println(user.toString());
		

		
		//查询部门
//		dept=session.selectOne("selectDeptWithId", 100);
//		System.out.println(dept.toString());
		
		//根据部门ID查询所有用户
//		List<User> users=session.selectList("selectUsersWithDeptId", 100);
//		for(User u:users) System.out.println(u.toString());
		
		//查询所有部门的信息
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
