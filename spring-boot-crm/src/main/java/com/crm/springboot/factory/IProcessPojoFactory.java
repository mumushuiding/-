package com.crm.springboot.factory;

import javax.servlet.http.HttpServletRequest;

import com.crm.springboot.pojos.process.AbstractProcessPojo;

public interface IProcessPojoFactory {
	public AbstractProcessPojo getPojo();
	public AbstractProcessPojo getParamByBean(HttpServletRequest request);
}
