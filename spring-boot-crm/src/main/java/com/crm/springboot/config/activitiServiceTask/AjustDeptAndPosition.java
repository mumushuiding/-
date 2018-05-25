package com.crm.springboot.config.activitiServiceTask;

import java.util.HashMap;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.service.UserService;

@Service
public class AjustDeptAndPosition implements JavaDelegate{
    private Expression user;
    private Expression userLinkDept;
    @Autowired
    private UserService userService;
	@Override
	public void execute(DelegateExecution execution) {

		User user1=(User) user.getValue(execution);
		UserLinkDept userLinkDept1=(UserLinkDept) userLinkDept.getValue(execution);
		HashMap<String,Object> params=new HashMap<String, Object>();
		params.put("userId", user1.getId());
		
		//首先先判定部门是否有需要修改
		if(userLinkDept1.getFirstLevelIds()!=null
				&&userLinkDept1.getFirstLevelIds().trim()!=""
				&&String.valueOf(userLinkDept1.getSecondLevel().getDid())!=null
				&&String.valueOf(userLinkDept1.getSecondLevel().getDid()).trim()!="")
		{
			List<String> idsList=userService.selectAllUserLinkDeptIds(params);
			userService.deleteUserLinkDeptByIds(idsList.toArray(new String[idsList.size()]));
			userService.saveUserLinkDept(userLinkDept1);
		}
		
		
		if(user1.getPosition()!=null&&user1.getPosition()!="")userService.update(user1);
	}

}
