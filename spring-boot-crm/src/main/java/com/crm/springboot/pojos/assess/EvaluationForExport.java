package com.crm.springboot.pojos.assess;

public class EvaluationForExport {
	//如果有增加属性需要更改/file/export/assessment/{taskType}
	private String username;
	private String phone;
	private String deptName;//部门
	private String marks;//考效总分
	private String leadershipEvaluation;//领导点评
	private String publicEvaluation;//群众评议
	private String overseerEvaluation;//组织考评
	private String totalMark;//总分
	private String result;//总分
	
	public static String getColnames(){
		return "用户名,电话,部门,考效总分,领导点评,群众评议,组织考评,总分,考核结果";
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getMarks() {
		return marks;
	}
	public void setMarks(String marks) {
		this.marks = marks;
	}
	public String getLeadershipEvaluation() {
		return leadershipEvaluation;
	}
	public void setLeadershipEvaluation(String leadershipEvaluation) {
		this.leadershipEvaluation = leadershipEvaluation;
	}
	public String getPublicEvaluation() {
		return publicEvaluation;
	}
	public void setPublicEvaluation(String publicEvaluation) {
		this.publicEvaluation = publicEvaluation;
	}
	public String getOverseerEvaluation() {
		return overseerEvaluation;
	}
	public void setOverseerEvaluation(String overseerEvaluation) {
		this.overseerEvaluation = overseerEvaluation;
	}
	public String getTotalMark() {
		return totalMark;
	}
	public void setTotalMark(String totalMark) {
		this.totalMark = totalMark;
	}
	
	
	
}
