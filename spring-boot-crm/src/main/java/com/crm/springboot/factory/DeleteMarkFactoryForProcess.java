package com.crm.springboot.factory;

import com.crm.springboot.pojos.process.AbstractProcessPojo;
import com.crm.springboot.pojos.process.DeleteMarkForProcess;

public class DeleteMarkFactoryForProcess extends AProcessPojoFactory{

	@Override
	public AbstractProcessPojo getPojo() {
		
		return new DeleteMarkForProcess();
	}

}
