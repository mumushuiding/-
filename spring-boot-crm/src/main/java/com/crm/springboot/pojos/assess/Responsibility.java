package com.crm.springboot.pojos.assess;

import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.utils.DateUtil;

public class Responsibility implements Serializable{
	private Integer id;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	private ProcessBean processBean;
	//用来存储任务名
	private String sparation;
	//当前岗位职责
	private String currentJobDescription;
	//当年工作任务
	private String currentJob;
	//新部门,允许多选
	private String newDeptIds;
	//新职务
	private String newPosition;
	//新岗位职责描述
	private String newJobDescription;
	//当年新的工作职责
	private String newJob;
	//调整生效时间
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date ajustDate;
    //生成日期
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
	//保存三级部门id,firstLevelIds为多个值
	private String secondLevelId;
	private String firstLevelIds;
	private String thirdLevelId;
    private String position;
    private String oldPosition;
	@Override
	public String toString() {
		return "Responsibility [processBean=" + processBean + "]";
	}
	
	public String getOldPosition() {
		return oldPosition;
	}

	public void setOldPosition(String oldPosition) {
		this.oldPosition = oldPosition;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSecondLevelId() {
		return secondLevelId;
	}

	public void setSecondLevelId(String secondLevelId) {
		this.secondLevelId = secondLevelId;
	}

	public String getFirstLevelIds() {
		return firstLevelIds;
	}

	public void setFirstLevelIds(String firstLevelIds) {
		this.firstLevelIds = firstLevelIds;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public ProcessBean getProcessBean() {
		return processBean;
	}
	public void setProcessBean(ProcessBean processBean) {
		this.processBean = processBean;
	}
	public String getSparation() {
		return sparation;
	}
	public void setSparation(String sparation) {
		this.sparation = sparation;
	}
	public String getCurrentJobDescription() {
		return currentJobDescription;
	}
	public void setCurrentJobDescription(String currentJobDescription) {
		this.currentJobDescription = currentJobDescription;
	}
	public String getNewDeptIds() {
		return newDeptIds;
	}
	public void setNewDeptIds(String newDeptIds) {
		this.newDeptIds = newDeptIds;
	}
	public String getNewPosition() {
		return newPosition;
	}
	public void setNewPosition(String newPosition) {
		this.newPosition = newPosition;
	}
	public String getNewJobDescription() {
		return newJobDescription;
	}
	public void setNewJobDescription(String newJobDescription) {
		this.newJobDescription = newJobDescription;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		System.out.println("=======================由于今年填表时间较晚，故生成时间默认为1月至二月之间随机的日期");
		
		//this.createTime = createTime;
		this.createTime=DateUtil.getRandomDateBetweenMinAndMax("2018-01-01", "2018-02-15");
	}
	public String getCurrentJob() {
		return currentJob;
	}
	public void setCurrentJob(String currentJob) {
		this.currentJob = currentJob;
	}
	public String getNewJob() {
		return newJob;
	}
	public void setNewJob(String newJob) {
		this.newJob = newJob;
	}
	public Date getAjustDate() {
		return ajustDate;
	}
	public void setAjustDate(Date ajustDate) {
		this.ajustDate = ajustDate;
	}

	public String getThirdLevelId() {
		return thirdLevelId;
	}

	public void setThirdLevelId(String thirdLevelId) {
		this.thirdLevelId = thirdLevelId;
	}


    
}
