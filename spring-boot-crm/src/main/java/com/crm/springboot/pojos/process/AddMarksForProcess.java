package com.crm.springboot.pojos.process;

import java.util.ArrayList;
import java.util.List;

import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Mark;


/**
 * @author Administrator
 *
 */
public class AddMarksForProcess extends AbstractProcessPojo{
    List<Mark> marks=new ArrayList<Mark>();
    String phone;
    String name;
    String id;
    String times;//times格式：2018年5月

    public AddMarksForProcess(){
    	for (int i = 0; i <5; i++) {
			marks.add(new Mark());
		}
    }
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Mark> getMarks() {
		return marks;
	}

	public void setMarks(List<Mark> marks) {
		this.marks = marks;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;

	}

	@Override
	public String getTitle() {
		
		return ProcessType.TITLE_ADDMARKS;
	}

	@Override
	public String getBusinessType() {
		
		return ProcessType.BUSINESSTYPE_ADDMARKS;
	}

	@Override
	public int getRoute() {
		
		return 0;
	}

}
