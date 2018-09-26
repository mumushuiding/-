package com.crm.springboot.config;

import java.io.File;

public class AvailableSettings {
	//public final static String TEMP_UPLOAD_LOCATION="D:\\GitHub_Repository\\spring-boot-crm\\upload\\";
	public final static String TEMP_UPLOAD_LOCATION="/www/spring-boot-crm/upload/";
	/**
	 * ************************************紧急程度************************************************
	 */
	public final static String DEGREEOFURGENCY_NOMAL="normal";
	public final static String DEGREEOFURGENCY_WARN="warn";
	public final static String DEGREEOFURGENCY_DANGER="danger";
	public final static String DEGREEOFURGENCY_DEAD="dead";
	
	/**
	 * ************************************系统导入************************************************
	 */
	public final static String SYSTEM_UPLOAD="系统导入";
	public final static String MONTHASSESS_UNSUBMITTEDBEFOREDEADLINE="月度考核限期未交，被判定为不合格";
	public final static String MONTHASSESS_DELAYSUBMITTED="月度考核延迟提交产生扣分";
	
	/**
	 * ************************************存放定时任务的地址************************************************
	 */
	public final static String SCHEDULEJOB_PACKAGENAME="com.crm.springboot.job";
	public final static String SCHEDULEJOB_LOCATION=System.getProperty("user.dir")+File.separator+"src"+File.separator+
			"main"+File.separator+"java"+File.separator+
			"com"+File.separator+"crm"+File.separator+
			"springboot"+File.separator+"job";

}
