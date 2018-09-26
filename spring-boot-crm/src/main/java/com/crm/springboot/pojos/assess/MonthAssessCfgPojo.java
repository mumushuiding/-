package com.crm.springboot.pojos.assess;

public class MonthAssessCfgPojo {
	
	private String cronSchedule;
	
	private String targetGroup;
	
	private int warningBeginDayOfMonth;
	
	private int dangerBeginDayOfMonth;
	
	private int deadLineDayOfMonth;
	
	private String deductPointsForDelay;
	
	private String deductPointsForUnsubmitted;

	
	
	@Override
	public String toString() {
		return "MonthAssessCfgPojo [cronSchedule=" + cronSchedule + ", targetGroup=" + targetGroup
				+ ", warningBeginDayOfMonth=" + warningBeginDayOfMonth + ", dangerBeginDayOfMonth="
				+ dangerBeginDayOfMonth + ", deadLineDayOfMonth=" + deadLineDayOfMonth + ", deductPointsForDelay="
				+ deductPointsForDelay + ", deductPointsForUnsubmitted=" + deductPointsForUnsubmitted + "]";
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

	public int getWarningBeginDayOfMonth() {
		return warningBeginDayOfMonth;
	}

	public void setWarningBeginDayOfMonth(int warningBeginDayOfMonth) {
		this.warningBeginDayOfMonth = warningBeginDayOfMonth;
	}

	public int getDangerBeginDayOfMonth() {
		return dangerBeginDayOfMonth;
	}

	public void setDangerBeginDayOfMonth(int dangerBeginDayOfMonth) {
		this.dangerBeginDayOfMonth = dangerBeginDayOfMonth;
	}

	public int getDeadLineDayOfMonth() {
		return deadLineDayOfMonth;
	}

	public void setDeadLineDayOfMonth(int deadLineDayOfMonth) {
		this.deadLineDayOfMonth = deadLineDayOfMonth;
	}

	public String getDeductPointsForDelay() {
		return deductPointsForDelay;
	}

	public void setDeductPointsForDelay(String deductPointsForDelay) {
		this.deductPointsForDelay = deductPointsForDelay;
	}

	public String getDeductPointsForUnsubmitted() {
		return deductPointsForUnsubmitted;
	}

	public void setDeductPointsForUnsubmitted(String deductPointsForUnsubmitted) {
		this.deductPointsForUnsubmitted = deductPointsForUnsubmitted;
	}


	
	

}
