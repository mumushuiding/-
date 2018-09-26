package com.crm.springboot.factory;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.process.AbstractProcessPojo;
import com.crm.springboot.pojos.process.DeleteMarksByOverTimeForProcess;
import com.crm.springboot.pojos.process.GroupManagerForProcess;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;

public class DeleteMarksByOverTimeFactoryForProcess extends AProcessPojoFactory{


	@Override
	public AbstractProcessPojo getPojo() {
		
		return new DeleteMarksByOverTimeForProcess();
	}


}
