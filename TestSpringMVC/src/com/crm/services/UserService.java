package com.crm.services;

import org.springframework.stereotype.Service;

import com.crm.pojos.User;

@Service
public class UserService {
	
   public boolean checkLogin(User user){
	   if("admin".equals(user.getLoginname())&&"123".equals(user.getPassword())) return true;
	   return false;
   }
}
