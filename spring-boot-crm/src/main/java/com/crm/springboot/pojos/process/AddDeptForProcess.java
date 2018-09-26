package com.crm.springboot.pojos.process;

import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.user.Department;

public class AddDeptForProcess extends AbstractProcessPojo{
	private Department department=new Department();
	@Override
	public String getTitle() {
		
		return ProcessType.TITLE_ADDDEPARTMENT;
	}

	@Override
	public String getBusinessType() {
		
		return ProcessType.BUSINESSTYPE_ADDDEPARTMENT;
	}

	@Override
	public int getRoute() {
		
		return 1;//要经过考核办
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	

}
