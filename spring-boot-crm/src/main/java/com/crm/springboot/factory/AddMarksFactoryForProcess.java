package com.crm.springboot.factory;

import com.crm.springboot.pojos.process.AbstractProcessPojo;
import com.crm.springboot.pojos.process.AddMarksForProcess;

public class AddMarksFactoryForProcess  extends AProcessPojoFactory {

	@Override
	public AbstractProcessPojo getPojo() {
		
		return new AddMarksForProcess();
	}

	

	

}
