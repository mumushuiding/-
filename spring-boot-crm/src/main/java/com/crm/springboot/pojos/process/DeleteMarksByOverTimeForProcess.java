package com.crm.springboot.pojos.process;

import com.crm.springboot.pojos.ProcessType;

public class DeleteMarksByOverTimeForProcess extends AbstractProcessPojo{
    private String times;
    
	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}
	@Override
	public String getTitle() {
		
		return ProcessType.TITLE_DELETEMARKSBYOVERTIME;
	}

	@Override
	public String getBusinessType() {
		
		return ProcessType.BUSINESSTYPE_DELETEMARKSBYOVERTIME;
	}

	@Override
	public int getRoute() {
		
		return 2;
	}

}
