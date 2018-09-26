package com.crm.springboot.service;


import java.util.List;

import com.crm.springboot.pojos.job.JobAndTrigger;
import com.github.pagehelper.PageInfo;

public interface JobService{
	public void addJob(String jobName,String jobClassName, String jobGroupName, String cronExpression)throws Exception;
	public void pauseJob(String jobClassName, String jobGroupName) throws Exception;
	public void jobResume(String jobClassName, String jobGroupName) throws Exception;
	public void rescheduleJob(String jobClassName, String jobGroupName, String cronExpression)throws Exception;
	public void deleteJob(String jobClassName, String jobGroupName) throws Exception;
	public List<JobAndTrigger> selectAllJob();
	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);
}
