package com.crm.springboot.factory;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;


import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.process.AbstractProcessPojo;
import com.crm.springboot.pojos.process.GroupManagerForProcess;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;

public class GroupManagerFactoryForProcess extends AProcessPojoFactory{
    private GroupManager group=null;
   
	@Override
	public AbstractProcessPojo getPojo() {
		
		
		return new GroupManagerForProcess();
	}

//	@Override
//	public AbstractProcessPojo getParamByBean(HttpServletRequest request) {
//		
//		AbstractProcessPojo aPojo=null;
//		try {
//			aPojo = BeanUtils.instantiateClass(GroupManagerForProcess.class.getDeclaredConstructor());
//		} catch (BeanInstantiationException | NoSuchMethodException | SecurityException e) {
//			
//			e.printStackTrace();
//		}
//		Post post=new Post();
//		User user=new User();
//		user.setPost(post);
//		
//    	ProcessBean processBean=new ProcessBean();
//    	processBean.setUser(user);
//    	aPojo.setProcessBean(processBean);
//		BeanWrapperImpl bw = new BeanWrapperImpl(aPojo);
//		MutablePropertyValues pvs = new MutablePropertyValues(); 
//		Enumeration<?> attrNames = request.getParameterNames();
//		
//        while (attrNames.hasMoreElements()) {  
//            String key = (String) attrNames.nextElement();  
//            pvs.addPropertyValue(key, request.getParameterValues(key)); 
//
//        }  
//        bw.setPropertyValues(pvs, true, true);
//
//        return aPojo;  
//	}

	

}
