package com.crm.springboot.pojos.process;

import org.springframework.stereotype.Service;

import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessType;

@Service
public class GroupManagerForProcess extends AbstractProcessPojo{

    private GroupManager groupManager=new GroupManager();

	@Override
	public String getTitle() {
		
		return ProcessType.TITLE_POWER_ADDORUPDATE;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	@Override
	public String getBusinessType() {
		
		return ProcessType.BUSINESSTYPE_GROUPMANAGER;
	}

	@Override
	public int getRoute() {
		
		return 1;
	}
    

}
