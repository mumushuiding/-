package com.crm.springboot.factory;

import com.crm.springboot.pojos.process.AbstractProcessPojo;
import com.crm.springboot.pojos.process.AddDeptForProcess;

public class AddDepartmentFacotoryForProcess extends AProcessPojoFactory{

	@Override
	public AbstractProcessPojo getPojo() {
		
		return new AddDeptForProcess();
	}

}
