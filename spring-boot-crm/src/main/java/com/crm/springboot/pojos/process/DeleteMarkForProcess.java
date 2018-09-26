package com.crm.springboot.pojos.process;

import java.util.ArrayList;
import java.util.List;

import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Mark;


public class DeleteMarkForProcess extends AbstractProcessPojo{
	List<Mark> marks=new ArrayList<Mark>();
	public DeleteMarkForProcess(){
		for (int i = 0; i <5; i++) {
			marks.add(new Mark());
		}
	}
	
	public List<Mark> getMarks() {
		return marks;
	}

	public void setMarks(List<Mark> marks) {
		this.marks = marks;
	}

	@Override
	public String getTitle() {
		
		return ProcessType.TITLE_DELETEMARK;
	}

	@Override
	public String getBusinessType() {
		
		return ProcessType.BUSINESSTYPE_DELETEMARK;
	}

	@Override
	public int getRoute() {
		
		return 2;//直接跳转到考核办
	}

}
