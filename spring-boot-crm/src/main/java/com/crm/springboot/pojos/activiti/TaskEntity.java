package com.crm.springboot.pojos.activiti;

public class TaskEntity {
	private String ID_;
	private String DESCRIPTION_;
	
	
	
	@Override
	public String toString() {
		return "TaskEntity [ID_=" + ID_ + ", DESCRIPTION_=" + DESCRIPTION_ + "]";
	}
	public String getID_() {
		return ID_;
	}
	public void setID_(String iD_) {
		ID_ = iD_;
	}
	public String getDESCRIPTION_() {
		return DESCRIPTION_;
	}
	public void setDESCRIPTION_(String dESCRIPTION_) {
		DESCRIPTION_ = dESCRIPTION_;
	}
	
	
	
	
}
