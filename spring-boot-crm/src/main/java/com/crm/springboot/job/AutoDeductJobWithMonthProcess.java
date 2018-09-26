package com.crm.springboot.job;



import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.crm.springboot.pojos.Dictionary;
import com.crm.springboot.pojos.assess.MonthAssessCfgPojo;
import com.crm.springboot.service.BaseJob;
import com.crm.springboot.service.DictionaryService;
import com.crm.springboot.service.ResponsibilityService;

@Service
public class AutoDeductJobWithMonthProcess implements BaseJob{
    private static final Log log=LogFactory.getLog(AutoDeductJobWithMonthProcess.class);
    @Autowired
    private ResponsibilityService responsibilityService;
    @Autowired
    private DictionaryService dictionaryService;
    public AutoDeductJobWithMonthProcess(){}
    private static MonthAssessCfgPojo monthAssessCfgPojo;
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
	  
	   String cronExpression=(String) arg0.getJobDetail().getJobDataMap().getString("cronExpression");
	  
        if(arg0.getTrigger().getJobDataMap().getString("cronExpression")!=null) cronExpression=arg0.getTrigger().getJobDataMap().getString("cronExpression");
        monthAssessCfgPojo=getMonthAssessCfgPojo();
        String postNames=dictionaryService.selectSingleValueOfDic("考核对象", "月度考核");
        
        
	    String[] x=cronExpression.split(" ");
	    String[] days=x[3].split("-"); 
	    
	    int dayBegin=Integer.valueOf(days[0]);
	    int dayEnd=Integer.valueOf(days[1]);
	    
		
	    
	    
	    Dictionary dictionary=dictionaryService.selectSingleDic("延迟提交扣分日期", "月度考核");
	    int dangerBeginDayOfMonth=Integer.valueOf(dictionary.getValue());
	    
	    Dictionary dictionary2=dictionaryService.selectSingleDic("限期未交扣分日期", "月度考核");
	    int deadLineDayOfMonth=Integer.valueOf(dictionary2.getValue());
	    
	    Dictionary dictionary3=dictionaryService.selectSingleDic("提醒提交日期", "月度考核");
	    int warningBeginDayOfMonth=Integer.valueOf(dictionary3.getValue());
		try {
		    if(dayBegin!=dangerBeginDayOfMonth) {
		    	dictionary.setValue(String.valueOf(dayBegin));
		    	dictionaryService.updateDic(dictionary);
		    	dictionary3.setValue(String.valueOf((dayBegin-2)));
		    	dictionaryService.updateDic(dictionary3);
		    }
		    if(dayEnd!=deadLineDayOfMonth){
		    	dictionary2.setValue(String.valueOf(dayEnd));
		    	dictionaryService.updateDic(dictionary2);
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
		}

	   
	    String deductPointsForUnsubmitted=dictionaryService.selectSingleValueOfDic("月考不合格", "月考自动加减分");
	    String deductPointsForDelay=dictionaryService.selectSingleValueOfDic("月考延迟提交每日扣分", "月考自动加减分");
	    
	   
	    
	    
	    monthAssessCfgPojo.setDangerBeginDayOfMonth(dangerBeginDayOfMonth);
	    monthAssessCfgPojo.setDeadLineDayOfMonth(deadLineDayOfMonth);
	    monthAssessCfgPojo.setTargetGroup(postNames);
	    monthAssessCfgPojo.setWarningBeginDayOfMonth(warningBeginDayOfMonth);
	    monthAssessCfgPojo.setDeductPointsForUnsubmitted(deductPointsForUnsubmitted);
	    monthAssessCfgPojo.setDeductPointsForDelay(deductPointsForDelay);
	    
	 
		responsibilityService.deductWithMonthProcess(monthAssessCfgPojo);
		log.info("判断用户是否有延迟提交或者限期未交月度考核报表，自动扣分程序");
		
	}
	public static MonthAssessCfgPojo getMonthAssessCfgPojo(){
		if(monthAssessCfgPojo==null) monthAssessCfgPojo=new MonthAssessCfgPojo();
		return monthAssessCfgPojo;
	}


}
