package com.crm.springboot.mapper;

import java.util.List;

import com.crm.springboot.pojos.job.JobAndTrigger;

public interface JobMapper {
	public List<JobAndTrigger> selectAllJob();
	public List<JobAndTrigger> getJobAndTriggerDetails();
}
