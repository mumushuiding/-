package com.crm.springboot.mapper;
import java.util.List;

import com.crm.springboot.pojos.User;

public interface UserMapper extends BaseMapper<User> {
	List<User> getBySomething(User user);
	
	List<User> selectAllUser();
}
