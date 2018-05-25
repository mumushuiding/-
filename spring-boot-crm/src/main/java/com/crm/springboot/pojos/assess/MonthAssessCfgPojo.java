package com.crm.springboot.pojos.assess;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="month-mapping")
public class MonthAssessCfgPojo {
	
	private String cronSchedule;
	
	private String targetGroup;
	
	private String yellowLightDay;
	
	private String redLightDay;
	
	private String deadLineDay;
	
	private String deductPoints;
	@Override
	public String toString() {
		return "MonthAssessCfgPojo [cronSchedule=" + cronSchedule + ", targetGroup=" + targetGroup + ", yellowLightDay="
				+ yellowLightDay + ", redLightDay=" + redLightDay + ", deadLineDay=" + deadLineDay + ", deductPoints="
				+ deductPoints + "]";
	}
	public String getCronSchedule() {
		return cronSchedule;
	}
	public void setCronSchedule(String cronSchedule) {
		this.cronSchedule = cronSchedule;
	}
	public String getTargetGroup() {
		return targetGroup;
	}
	public void setTargetGroup(String targetGroup) {
		this.targetGroup = targetGroup;
	}
	public String getYellowLightDay() {
		return yellowLightDay;
	}
	public void setYellowLightDay(String yellowLightDay) {
		this.yellowLightDay = yellowLightDay;
	}
	public String getRedLightDay() {
		return redLightDay;
	}
	public void setRedLightDay(String redLightDay) {
		this.redLightDay = redLightDay;
	}
	public String getDeadLineDay() {
		return deadLineDay;
	}
	public void setDeadLineDay(String deadLineDay) {
		this.deadLineDay = deadLineDay;
	}
	public String getDeductPoints() {
		return deductPoints;
	}
	public void setDeductPoints(String deductPoints) {
		this.deductPoints = deductPoints;
	}


}
