package com.crm.springboot.controller;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.DateUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.crm.springboot.config.AvailableSettings;
import com.crm.springboot.job.AutoDeductJobWithMonthProcess;
import com.crm.springboot.pojos.assess.MonthAssessCfgPojo;
import com.crm.springboot.pojos.job.JobAndTrigger;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.JobService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.Files;
import com.crm.springboot.utils.JsonUtils;
import com.crm.springboot.utils.RegexUtils;

@Controller
@RequestMapping(value="/job")
public class JobController {
	private static Log log=LogFactory.getLog(JobController.class);
	@Autowired
	private ResponsibilityService responsibilityService;
	 //加入Qulifier注解，通过名称注入bean
	@Autowired
	private JobService jobService;
	@Autowired @Qualifier("Scheduler")
	private Scheduler scheduler;
	@RequestMapping("/{var}")
	public String locate(@PathVariable String var){
		
		return "/system/job/"+var;
	}
	@RequestMapping("/{var}/{var1}")
	public String locate1(@PathVariable String var,@PathVariable String var1,Model model){
		if ("job".equals(var1)) {
			model.addAttribute("taskType", var1);
			return "/system/job/"+var;
		}
		return "/system/job/"+var;
	}
	@RequestMapping("/addJob")
	public String addJob(
			@RequestParam String jobName,
			@RequestParam String jobClassName,
			HttpServletRequest request,
			@RequestParam String jobGroupName,
			@RequestParam String cronExpression) throws Exception{
		jobService.addJob(jobName,jobClassName, jobGroupName, cronExpression);
		return "redirect:"+request.getHeader("referer");
	}
	@RequestMapping("/pauseJob")
	public String pauseJob(@RequestParam String jobName,@RequestParam String jobGroupName,HttpServletRequest request,HttpSession session) throws Exception{
		User user=(User) session.getAttribute("sysuser");
		
		try {
			jobService.pauseJob(jobName, jobGroupName);
			log.info("["+user.getId()+"]["+user.getUsername()+"] 暂停了 自动执行程序  ["+jobName+"]任务组["+jobGroupName+"]");
		} catch (Exception e) {
			log.warn("["+user.getId()+"]["+user.getUsername()+"] 暂停 自动执行程序  ["+jobName+"]任务组["+jobGroupName+"] 失败！！！",e);
		}
		
		return "redirect:"+request.getHeader("referer");
	}
	@RequestMapping("/resumeJob")
	public String resumeJob(@RequestParam String jobName,@RequestParam String jobGroupName,HttpServletRequest request,HttpSession session) throws Exception{
		User user=(User) session.getAttribute("sysuser");
		log.info("["+user.getId()+"]["+user.getUsername()+"] 继续了 自动执行程序  ["+jobName+"]任务组["+jobGroupName+"]");
		jobService.jobResume(jobName, jobGroupName);
		return "redirect:"+request.getHeader("referer");
	}
	@RequestMapping("/rescheduleJob")
	public String rescheduleJob(
			@RequestParam String jobName,
			@RequestParam String jobClassName,
			HttpServletRequest request,
			@RequestParam String jobGroupName,
			@RequestParam String cronExpression) throws Exception{
		jobService.rescheduleJob(jobClassName, jobGroupName, cronExpression);
		
		return "redirect:"+request.getHeader("referer");
	}
	
	@RequestMapping("/deleteJob")
	public String deleteJob(@RequestParam String jobClassName,@RequestParam String jobGroupName,HttpServletRequest request,HttpSession session
			) throws Exception{
		User user=(User) session.getAttribute("sysuser");
		log.info("["+user.getId()+"]["+user.getUsername()+"] 删除了 自动执行程序  ["+jobClassName+"]任务组["+jobGroupName+"]");
		jobService.deleteJob(jobClassName, jobGroupName);
		return "redirect:"+request.getHeader("referer");
	}
	@RequestMapping("/selectAllJob/{taskType}")
	@ResponseBody
	public String selectAllJob(Model model,Integer pageIndex,Integer pageSize,@PathVariable String taskType){
		model.addAttribute("taskType", taskType);
	
		JsonUtils.startPageHelper(pageIndex, pageSize);
		List<JobAndTrigger> jobAndTriggers=jobService.selectAllJob();
		
		return JsonUtils.formatListForPagination(jobAndTriggers, pageIndex, pageSize);
	}
	@RequestMapping("/getJobClassNameAsSelectDom")
	@ResponseBody
	public String getJobClassNameAsSelectDom(){
		
		List<String> result=new ArrayList<String>();
		
		Files files=new Files();
		files.getFiles(AvailableSettings.SCHEDULEJOB_LOCATION);
		List<String> fileList=files.getFileList();
		
		for (String pathname : fileList) {
			
			String[] x=(pathname.replace(".", "\\")).split("\\\\");
			result.add(AvailableSettings.SCHEDULEJOB_PACKAGENAME+"."+x[x.length-2]);
			
		}
		StringBuffer stringBuffer=new StringBuffer("{}");
		stringBuffer.insert(1,"\"classNames\":"+JsonUtils.getGson().toJson(result));
		
		return stringBuffer.toString();
	}
/**
 * *****************************************test*************************************************	
 */
  @RequestMapping("/test")
  @ResponseBody
  public String test(){
	  String urgency=responsibilityService.getDegreeOfUrgencyWithUnsubmittedAssessForm();
	  System.out.println("***********************************urgency="+urgency);
	  
	  User user=new User();
	  user.setId(11421);

	  Date startDate=DateUtil.parseTimesTampDate("2018-06-01 00:00:00");
	 
	  Date endDate=DateUtil.parseTimesTampDate("2018-06-30 00:00:00");
	  System.out.println("startDate="+startDate+",endDate="+endDate);
//	  boolean isde=responsibilityService.isDeduct(user, AvailableSettings.MONTHASSESS_UNSUBMITTEDBEFOREDEADLINE, startDate, endDate);
//	  System.out.println("*********************是否已经扣过分了="+isde);
	  
	  //测试一下 是否可以扣分
	 // responsibilityService.deductWithMonthProcessUnsubmittedBeforeDeadLineWithUser(user, startDate, endDate, AvailableSettings.MONTHASSESS_UNSUBMITTEDBEFOREDEADLINE);
	  MonthAssessCfgPojo monthAssessCfgPojo = new MonthAssessCfgPojo();
	  monthAssessCfgPojo.setTargetGroup("普通员工,中层副职,中层正职");
	  List<User> users=responsibilityService.selectAllUsersWhoUnsubmittedMonthProcess(monthAssessCfgPojo);
	  for (User user2 : users) {
		System.out.println(user2.getUsername());
	}
	 
	  return null;
	  
  }
}
