package com.crm.springboot.pojos;

public class ProcessType {
	// 待办任务标识
	public final static String CANDIDATE = "candidate";
	
	// 受理任务标识
	public final static String ASSIGNEE = "assignee";
	
	//用户审批过的任务
	public final static String HISTORY_ASSIGNEE="historyAssignee";
	//单据类型
	public static final String RESPONSIBILITY_TYPE="一线干部责任清单";
	public static final String EVALUCATION_MONTH="一线干部-月度考核";
	public static final String EVALUCATION_HALFYEAR="一线干部-半年考核";
	public static final String EVALUCATION_FULLYEAR="一线干部-年度考核";
	
	/**
	 * 流程类型
	 */
	public static final String PROCESS_EVALUATION="EvaluationProcess";
}
