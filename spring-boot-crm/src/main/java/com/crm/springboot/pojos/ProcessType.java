package com.crm.springboot.pojos;

public class ProcessType {
	// 待办任务标识
	public final static String CANDIDATE = "candidate";
	public final static String TASK_ASSESSGROUP="assessGroups";
	public final static String TASK_Leader="leader";
	// 受理任务标识
	public final static String ASSIGNEE = "assignee";
	
	//用户审批过的任务
	public final static String HISTORY_ASSIGNEE="historyAssignee";
	public final static String HISTORY_PASS="historyPass";
	public final static String HISTORY_REJECT="historyReject";
	
	//单据类型
	public static final String RESPONSIBILITY_TYPE="一线干部责任清单";
	public static final String EVALUCATION_MONTH="一线干部-月度考核";
	public static final String EVALUCATION_HALFYEAR="一线干部-半年考核";
	public static final String EVALUCATION_FULLYEAR="一线干部-年度考核";
	public static final String OTHERS="其它";
	public static final String BUSINESSTYPE_GROUPMANAGER="groupManager";
	
	public static final String BUSINESSTYPE_DELETEUSER="deleteUser";
	public static final String TITLE_DELETEUSER="删除用户";
	
	public static final String BUSINESSTYPE_DELETEMARKSBYOVERTIME="deleteMarksByOverTime";
	public static final String TITLE_DELETEMARKSBYOVERTIME="删除超时扣分申请";
	
	public static final String BUSINESSTYPE_ADDMARKS="addMarks";
	public static final String TITLE_ADDMARKS="添加加分或者减分申请";
	
	public static final String BUSINESSTYPE_UPDATEMARK="updateMark";
	public static final String TITLE_UPDATEMARK="修改加减分申请";
	
	public static final String BUSINESSTYPE_DELETEMARK="deleteMark";
	public static final String TITLE_DELETEMARK="删除加减分申请";
	
	/*
	 * *************************部门管理*************************************
	 */
	public static final String BUSINESSTYPE_ADDDEPARTMENT="addDepartment";
	public static final String TITLE_ADDDEPARTMENT="添加新部门申请";
	
	public static final String BUSINESSTYPE_OTHERS="others";
	public static final String TITLE_POWER_ADDORUPDATE="用户权限添加或者更新";
	
	
	public static final String EVALUCATION_MONTH_PATTER="(.*?)年(.*?)月份-月度考核";
	public static final String EVALUCATION_HALFYEAR_PATTER="(.*?)年(.*?)半年-半年考核";
	public static final String EVALUCATION_FULLYEAR_PATTER="(.*?)年-年度考核";
	/**
	 * 流程类型
	 */
	public static final String PROCESS_EVALUATION="EvaluationProcess";
	
	public static final String PROCESS_UNSUBMITTED="未提交";
	public static final String PROCESS_FINISHED="审结";
	public static final String PROCESS_COMPLETE="complete";
	
	public static final String BEST="best";
	public static final String NORMAL="normal";
	/**
	 * 部门的级别
	 */
	public static final String DEPT_LEVEL_FIRST="first";
	public static final String DEPT_LEVEL_SECOND="second";
	public static final String DEPT_LEVEL_THIRD="third";
}
