package com.crm.springboot.service;

import java.util.List;

import com.crm.springboot.pojos.LeaveInfo;


public interface LeaveInfoService extends BaseService<LeaveInfo>{
	
	/**
	 * 新增一条请假单记录
	 * @param msg
	 */
	void saveLeaveInfo(String msg);
	/**
	 * 查询待办流程
	 */
	List<LeaveInfo> getById(String userId);
	/**
	 * 完成任务
	 * @param taskId
	 * @param userid
	 * @param audit
	 */
	void completeTaskById(String taskId,String userId,String audit);
}
