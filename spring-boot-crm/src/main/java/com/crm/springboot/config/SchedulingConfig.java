package com.crm.springboot.config;



import java.io.File;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.crm.springboot.pojos.assess.MonthAssessCfgPojo;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.utils.DateUtil;

@Configuration
@EnableScheduling
public class SchedulingConfig {
	private final Log log=LogFactory.getLog(SchedulingConfig.class);
	private static Unmarshaller unmarshaller;
	private static MonthAssessCfgPojo monthAssessCfgPojo;

	
	@Autowired
	private ProcessService processService;
	
	
	/**
	 * 查询有哪些用户还未填写月度考核表
	 * @throws JAXBException 
	 */
	
	
	@Scheduled(cron="0/5 * * * * ?")

	public void selectAllUsersWhoUnsubmittedMonthProcess() throws JAXBException{
		monthAssessCfgPojo=getMonthAssessCfgPojo();

        String titleLike=DateUtil.formatDateByFormat(DateUtil.addMonths(new Date(),-1), "yyyy年M月份")+"-月度考核";
        String postNames=monthAssessCfgPojo.getTargetGroup();
       
        HashMap<String, Object> params=new HashMap<String, Object>();
        params.put("titleLike", titleLike);
        params.put("postNames",postNames.split(","));
        processService.selectAllUsersWhoUnsubmittedProcess(params);
      
	}
	public static MonthAssessCfgPojo getMonthAssessCfgPojo() throws JAXBException{
		unmarshaller=getUnmarshaller();
		monthAssessCfgPojo=(MonthAssessCfgPojo) unmarshaller.unmarshal(new File
        		(System.getProperty("user.dir")+File.separator+"upload"+File.separator+"cfg"+File.separator+"monthAssessCfg.xml"));
		return monthAssessCfgPojo;
	}
	public static Unmarshaller getUnmarshaller() throws JAXBException{
		if(unmarshaller==null){
			JAXBContext jc=JAXBContext.newInstance(MonthAssessCfgPojo.class);
	        unmarshaller=jc.createUnmarshaller();
		}
		return unmarshaller;
	}
}
