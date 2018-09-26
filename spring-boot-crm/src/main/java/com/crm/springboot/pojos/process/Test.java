package com.crm.springboot.pojos.process;

import java.lang.reflect.Constructor;

import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorUtils;

import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.user.User;

public class Test {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		GroupManagerForProcess aPojo=BeanUtils.instantiate(GroupManagerForProcess.class);
		BeanWrapperImpl bw = new BeanWrapperImpl(aPojo);
		
		MutablePropertyValues pvs = new MutablePropertyValues(); 
		pvs.addPropertyValue("groupManager.groupmanagerid", "123"); 
		pvs.addPropertyValue("groupManager.groupid", "456"); 
		pvs.addPropertyValue("processBean.user.username", "linting");
		//bw.convertForProperty("linting", "processBean.user.username");
		bw.setPropertyValues(pvs, true, true);
		//bw.setPropertyValue("processBean.user.username", "linting");
        System.out.println(aPojo.getProcessBean().getUser().getUsername());
        System.out.println(aPojo.getGroupManager().getGroupmanagerid());
	}

}
