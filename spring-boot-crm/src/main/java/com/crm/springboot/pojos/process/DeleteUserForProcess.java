package com.crm.springboot.pojos.process;

import java.util.ArrayList;
import java.util.List;

import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.user.User;

public class DeleteUserForProcess extends AbstractProcessPojo{
    
	List<User> users=new ArrayList<User>();
	public DeleteUserForProcess(){
		for (int i = 0; i <5; i++) {
			users.add(new User());
		}
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String getTitle() {
		
		return ProcessType.TITLE_DELETEUSER;
	}

	@Override
	public String getBusinessType() {
		
		return ProcessType.BUSINESSTYPE_DELETEUSER;
	}
	@Override
	public int getRoute() {
		
		return 2;//直接跳转到考核办
	}

}
