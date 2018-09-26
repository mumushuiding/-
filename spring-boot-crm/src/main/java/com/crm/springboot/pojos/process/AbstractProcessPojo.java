package com.crm.springboot.pojos.process;

import java.io.Serializable;

import com.crm.springboot.pojos.ProcessBean;

public abstract class AbstractProcessPojo implements Serializable{
	private ProcessBean processBean;
	private String comment;
	public ProcessBean getProcessBean() {
		return processBean;
	}

	public void setProcessBean(ProcessBean processBean) {
		this.processBean = processBean;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	//标题
	public abstract String getTitle();
	//类型
	public abstract String getBusinessType();
	
	//route:为1的话必须要经过考核办,为2的话提交申请之后直接到考核办
	public abstract int getRoute();
	

	
	

	
	
}
