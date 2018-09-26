package com.crm.springboot.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.core.jmx.JobDetailSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.crm.springboot.mapper.JobMapper;
import com.crm.springboot.pojos.job.JobAndTrigger;
import com.crm.springboot.service.BaseJob;
import com.crm.springboot.service.JobService;
import com.crm.springboot.service.ResponsibilityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service
public class ScheduleJobServiceImpl implements JobService{
	private static final Log log=LogFactory.getLog(ScheduleJobServiceImpl.class);
	@Autowired @Qualifier("Scheduler")
	private Scheduler scheduler;
	
	@Autowired
	private JobMapper jobMapper;
	@Autowired
	private ResponsibilityService responsibilityService;
	@Override
	public void addJob(String jobName,String jobClassName, String jobGroupName, String cronExpression) throws Exception {
		
		
		//构建Job信息
		//JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();
		 // 创建一个JobDetail实例，指定SimpleJob
        Map<String, Object> JobDetailmap = new HashMap<String, Object>();
        JobDetailmap.put("name", jobName);// 设置任务名字
        JobDetailmap.put("group", jobGroupName);// 设置任务组
        JobDetailmap.put("jobClass", jobClassName);// 指定执行类
                                                                // Task.class.getCanonicalName()
        JobDetail jobDetail = JobDetailSupport.newJobDetail(JobDetailmap);
        
        jobDetail.getJobDataMap().put("cronExpression", cronExpression);// 传输的上下文
        
		//表达式调试构建器
		CronScheduleBuilder scheduleBuilder=CronScheduleBuilder.cronSchedule(cronExpression);
	
		//按新的cronexpression表达式构建一个新的trigger
		CronTrigger trigger=TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
				.withSchedule(scheduleBuilder).build();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			//启动调试器
			scheduler.start();
		} catch (SchedulerException e) {
			log.info("创建定时任务失败"+e);
			throw new Exception("创建定时任务失败");
		}
	}
    public static BaseJob getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob)class1.newInstance();
    }
	@Override
	public void pauseJob(String jobName, String jobGroupName) throws Exception {
		
		scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
		
		
	}
	@Override
	public void jobResume(String jobName, String jobGroupName) throws Exception {
		
		scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
		
	}
	@Override
	public void rescheduleJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
		try {
            //如果是月度考核
		    if("com.crm.springboot.job.AutoDeductJobWithMonthProcess".equals(jobClassName)){
		    	responsibilityService.updateMonthAssessCfg(cronExpression);
		    }
		    
			TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
			
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger.getJobDataMap().put("cronExpression", cronExpression);
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (Exception e) {
			log.info("更新定时任务失败"+e);
			 throw new Exception("更新定时任务失败");
		}
	}
	@Override
	public void deleteJob(String jobClassName, String jobGroupName) throws Exception {
		
		scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
		scheduler.deleteJob(JobKey.jobKey(jobClassName,jobGroupName));
		
	}
	@Override
	public List<JobAndTrigger> selectAllJob() {
		
		return jobMapper.selectAllJob();
	}
	@Override
	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<JobAndTrigger> list = jobMapper.selectAllJob();
		PageInfo<JobAndTrigger> page = new PageInfo<JobAndTrigger>(list);
		return page;
	}

}
