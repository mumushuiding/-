package com.crm.springboot.config.activitiServiceTask;

import java.util.HashMap;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.service.UserService;

@Service
public class AjustDeptAndPosition implements JavaDelegate{

    @Autowired
    private UserService userService;
	@Override
	public void execute(DelegateExecution execution) {

		
		UserLinkDept userLinkDept1=(UserLinkDept) execution.getVariable("userLinkDept");
		String position=(String) execution.getVariable("position");
		HashMap<String,Object> params=new HashMap<String, Object>();
		System.out.println("userLinkDept1.getId()="+userLinkDept1.getId());
		params.put("userId", userLinkDept1.getUserId());
		
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

		if (position!=null&&!("").equals(position.trim())) {

			User user=new User();
			user.setId(userLinkDept1.getUserId());
			user.setPosition(position);
			userService.update(user);
		}
		
	}

}
