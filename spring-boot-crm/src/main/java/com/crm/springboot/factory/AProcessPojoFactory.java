package com.crm.springboot.factory;



import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;

import org.springframework.beans.PropertyValues;


import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.process.AbstractProcessPojo;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;



public abstract class AProcessPojoFactory {
	public abstract AbstractProcessPojo getPojo();
	public AbstractProcessPojo getParamByBean(HttpServletRequest request) {
		
        AbstractProcessPojo aPojo=getPojo();
		Post post=new Post();
		User user=new User();
		user.setPost(post);
		
    	ProcessBean processBean=new ProcessBean();
    	processBean.setUser(user);
    	aPojo.setProcessBean(processBean);
		BeanWrapperImpl bw = new BeanWrapperImpl(aPojo);
	    
		
		
//		MutablePropertyValues pvs = new MutablePropertyValues(); 
//		Enumeration<?> attrNames = request.getParameterNames();
//        while (attrNames.hasMoreElements()) {  
//            String key = (String) attrNames.nextElement();  
//            pvs.addPropertyValue(key, request.getParameterValues(key)); 
//           
//            
//
//        }  
        
        PropertyValues pvs = new MutablePropertyValues(request.getParameterMap());
        bw.setPropertyValues(pvs, true, true);
        
        for(Entry<String, String[]> entry:request.getParameterMap().entrySet()){
        	System.out.println(entry.getKey()+","+String.valueOf(entry.getValue()));
        }
		return aPojo;
	}
}
