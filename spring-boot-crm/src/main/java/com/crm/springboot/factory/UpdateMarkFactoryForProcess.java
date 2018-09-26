package com.crm.springboot.factory;

import com.crm.springboot.pojos.process.AbstractProcessPojo;
import com.crm.springboot.pojos.process.UpdateMarkForProcess;

public class UpdateMarkFactoryForProcess extends AProcessPojoFactory{

	@Override
	public AbstractProcessPojo getPojo() {
		
		return new UpdateMarkForProcess();
	}

}
