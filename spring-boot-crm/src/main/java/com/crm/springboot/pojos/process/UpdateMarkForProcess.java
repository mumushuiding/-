package com.crm.springboot.pojos.process;

import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Mark;

public class UpdateMarkForProcess extends AbstractProcessPojo{
	String phone;
    String name;
    Mark mark=new Mark();
    Mark oldMark=new Mark();
    
    
	public Mark getOldMark() {
		return oldMark;
	}

	public void setOldMark(Mark oldMark) {
		this.oldMark = oldMark;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Mark getMark() {
		return mark;
	}

	public void setMark(Mark mark) {
		this.mark = mark;
	}

	@Override
	public String getTitle() {
		
		return ProcessType.TITLE_UPDATEMARK;
	}

	@Override
	public String getBusinessType() {
		
		return ProcessType.BUSINESSTYPE_UPDATEMARK;
	}

	@Override
	public int getRoute() {
		
		return 2;
	}

}
