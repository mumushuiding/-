package com.crm.springboot.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.annotation.RequestScope;

import com.crm.springboot.pojos.GroupTable;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.DictionaryService;
import com.crm.springboot.service.ProcessService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.JsonUtils;

@Controller
@RequestMapping("/process")
public class ProcessController {
	@Autowired
	private ProcessService processService;
	@Autowired
	private UserService userService;
	@Autowired 
	private DictionaryService dictionaryService;
	@Autowired
	private SysPowerService sysPowerService;
	@Autowired
	private ActivitiService activitiService;
	@RequestMapping("/deleteProcess/{ids}")
	public String deleteProcess(@PathVariable String ids,HttpServletRequest request){
		String referer=request.getHeader("referer");
		processService.deleteProcessByProcessInstanceIds(ids.split(","));
		
		return "redirect:"+referer;
	}
	
	/**
	 * ****************************************月度考核********************************************************************
	 */
	
	@RequestMapping("/getHistoryMonthEfficiency/{deptLevel}/{deptName}")
	@ResponseBody
	public String getHistoryMonthEfficiency(
			@PathVariable String deptLevel,
			@PathVariable String deptName){
		
		//查询前6个月
		List<String> labelList=activitiService.generateMonthLabels(6);

		//确定查询部门
		List<String> names=userService.selectSingleColumnFromInfoDept("distinct name ", deptLevel, deptName);
		if(names==null|| names.size()==0) return null;
		
		//查询数据
		List<List<Double>> datasetList=new ArrayList<List<Double>>();
		
        for (int i=0;i<names.size();i++) {
			datasetList.add(new ArrayList<Double>());
		}
		
        //遍历月
		for (int i = 0; i < labelList.size(); i++) {
			String titleLike=labelList.get(i)+"份-月度考核";
			//遍历部门
			for(int j=0;j<names.size();j++){
				//提交率
				double submittedRate=0;
				//查询部门需要交表的人数
				String subDeptLevel="first";
				switch (deptLevel) {
				case "third":
					subDeptLevel="second";
					break;

				default:
					break;
				}
				
				Integer a=processService.getNumberHaveToSubmittedTheMonthAssess(names.get(j), subDeptLevel);
				
				if(a==0){
					datasetList.get(j).add((double) 0);
					continue;
				}
				Integer submittedNumber=0;
				
				submittedNumber=processService.getSubMittedNumber(names.get(j), subDeptLevel, titleLike);
				
				//提交数为0的话 ，后面三个就不用查了
				if(submittedNumber==0){
					datasetList.get(j).add((double) 0);
					continue;
				}
				submittedRate=submittedNumber*100/a;
				//审批率
				double approvalRate=0;
				List<String> currentCandidateGroupNotIn=new ArrayList<String>();
				currentCandidateGroupNotIn.add("31");
				currentCandidateGroupNotIn.add("");
				
				Integer approvalNumber=processService.selectCountProcess(names.get(j), subDeptLevel,titleLike,"",currentCandidateGroupNotIn);
			
				approvalRate=approvalNumber*100/submittedNumber;
				//任务量占比
				double taskRate=0;
				Integer totalProcessNumber=processService.selectCountProcess("", titleLike, "");//总流程数
				
				taskRate=submittedNumber*100/totalProcessNumber;
				//用时占比
				double timeRate=100;
				List<String> deptNames=userService.getDeptNames(names.get(j), subDeptLevel);
				if(!deptNames.contains(names.get(j))) deptNames.add(names.get(j));
				
				timeRate=processService.getApprovalTimeRate(titleLike, deptNames, "领导审批", "部门主任组");
		
				//结果
				double result=submittedRate*0.10+approvalRate*0.10+taskRate*0.4+(100-timeRate)*0.4;
//				System.out.println(names.get(j)+":a:"+a+",submittedNumber:"+submittedNumber+",approvalNumber:"+approvalNumber+",totalProcessNumber:"+totalProcessNumber+",timeRate:"+timeRate);
//				System.out.println(names.get(j)+":a:"+a+",submittedRate:"+submittedRate+",approvalRate:"+approvalRate+"taskRate:"+taskRate+",timeRate:"+timeRate+",result:"+result);
				datasetList.get(j).add(result);
			}
		}
        
		StringBuffer buffer=new StringBuffer("{}");
		List<Double[]> dataset=new ArrayList<Double[]>();
		for(List<Double> data:datasetList){
			dataset.add(data.toArray(new Double[data.size()]));
		}
		buffer.insert(1,"\"labels\":"+JsonUtils.getGson().toJson(labelList.toArray(new String[labelList.size()]))+",\"dataset\":"+JsonUtils.getGson().toJson(dataset)+",\"names\":"+JsonUtils.getGson().toJson(names.toArray(new String[names.size()])));
		
		return buffer.toString();
		
	}
	/**
	 * 从提交率、审批率、任务量占比、审批用时占比四个维度考核审批效率
	 * @param deptName
	 * @param deptLevel
	 * @return
	 */
	@RequestMapping("/monthApprovalEfficiency/{deptLevel}/{deptName}")
	@ResponseBody
	public String getMonthApprovalEfficiency(
			@PathVariable String deptName,
			@PathVariable String deptLevel){
		
		List<String> deptNames=userService.getDeptNames(deptName, deptLevel);
		if(!deptNames.contains(deptName)) deptNames.add(deptName);

		//查询部门需要交表的人数
		Integer a=processService.getNumberHaveToSubmittedTheMonthAssess(deptName, deptLevel);

		if(a==0) throw new RuntimeException("该部门["+deptName+"]实际应交人数为["+a+"]");

		//提交率
		String titleLike=DateUtil.formatDateByFormat(DateUtil.addMonths(new Date(),-2), "yyyy年M月份")+"-月度考核";
		Integer submittedNumber=processService.getSubMittedNumber(deptName, deptLevel, titleLike);
		Integer submittedRate=submittedNumber*100/a;
		//审批率
		List<String> currentCandidateGroupNotIn=new ArrayList<String>();
		currentCandidateGroupNotIn.add("31");
		currentCandidateGroupNotIn.add("");
		
		Integer approvalNumber=processService.selectCountProcess(deptName, deptLevel,titleLike,"",currentCandidateGroupNotIn);
		Integer approvalRate=approvalNumber*100/submittedNumber;
		//任务量占比
		Integer totalProcessNumber=processService.selectCountProcess("", titleLike, "");//总流程数
		Integer taskRate=submittedNumber*100/totalProcessNumber;
		//审批用时占比
		Integer timeRate=processService.getApprovalTimeRate(titleLike, deptNames, "领导审批", "部门主任组");
		
		StringBuffer buffer=new StringBuffer("{}");
		Integer[] data={submittedRate,approvalRate,taskRate,timeRate};
		String[] labels={"提交率","审批率","任务量占比(占报社)","审批用时占比(占报社)"};
		
		buffer.insert(1, "\"data\":"+JsonUtils.getGson().toJson(data)+",\"labels\":"+JsonUtils.getGson().toJson(labels));
		
		//System.out.println("a:"+a+",submittedNumber:"+submittedNumber+",approvalNumber:"+approvalNumber+",totalProcessNumber"+totalProcessNumber);
		return buffer.toString();

	}
	
	/**
	 * 查询当前考核组下所有子部门的未完成率
	 * @param groupName
	 * @return
	 */
	@RequestMapping("/unCompletionRateForEachDept/{groupName}/{deptLevel}/{deptName}")
	@ResponseBody
	public String getUnCompletionRateForEachDept(@PathVariable String groupName,@PathVariable String deptLevel,@PathVariable String deptName){
		String titleLike=DateUtil.formatDateByFormat(DateUtil.addMonths(new Date(),-1), "yyyy年M月份")+"-月度考核";
		
		//查询不重复的部门

		String[] deptNames=processService.selectDeptNames(deptLevel, deptName);

		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("titleLike", titleLike);
		switch (groupName) {
		case ProcessType.PROCESS_UNSUBMITTED:
			params.put("committed", "0");
			break;
		case ProcessType.PROCESS_FINISHED:
			//params.put("completed", "1");
			return null;
			//break;
		default:
			String currentCandidateGroup=sysPowerService.selectGroupIdWithGroupName(groupName);
			if(currentCandidateGroup==null) return null;
			params.put("currentCandidateGroup", currentCandidateGroup);
			break;
		}
		//首先查询当前考核组下的流程总数
		params.put("deptNames", deptNames);
		Integer total=processService.selectCountProcess(params);
		params.remove("deptNames");
		if(total==0)return null;

		//查询各个部门的占比
		
		List<Integer> rateList=new ArrayList<Integer>();
		for (String dname : deptNames) {
			
			params.put("deptName", dname);
			Integer b=processService.selectCountProcess(params);
           
			rateList.add(b*100/total);
		}
		
		//生成json数组
		StringBuffer buffer=new StringBuffer("{}");
		buffer.insert(1,"\"deptNames\":"+JsonUtils.getGson().toJson(deptNames)+",\"rates\":"+JsonUtils.getGson().toJson(rateList.toArray(new Integer[rateList.size()])));

		return buffer.toString();
		
	}
	/**
	 * 查询各个考核组未审批任务占总任务数的比率
	 * @param deptLevel
	 * @param deptName
	 * @return
	 */
	@RequestMapping("/unCompletionRateForEachGroup/{deptLevel}/{deptName}")
	@ResponseBody
	public String getUnCompletionRateForEachGroup(@PathVariable String deptLevel,@PathVariable String deptName){
		
		List<String> dnames=null;
		Dept dept=userService.selectSingleDept(deptName);
		if(dept==null) throw new RuntimeException("该部门【"+deptName+"】不存在，或者已经被删除了");
		String postIds=dictionaryService.selectSingleValueOfDic("考核对象id", "月度考核");
		String titleLike=DateUtil.formatDateByFormat(DateUtil.addMonths(new Date(),-1), "yyyy年M月份")+"-月度考核";
		Integer a=0;
		if ("second".equals(deptLevel)) {
			//统计该部门实际应交数
			a=userService.selectCountUserNumberWithSecondDeptId(dept.getDid(), postIds);
			dnames=userService.selectDistinctFirstDeptNames(dept.getDid());
		}
		if ("first".equals(deptLevel)) {
			//统计该部门实际应交数
			a=userService.selectCountUserNumberWithFirsDeptId(dept.getDid(), postIds);
			dnames=new ArrayList<String>();
			dnames.add(deptName);
		}
		if (dnames==null||dnames.size()==0) return null;
		String[] deptNames=dnames.toArray(new String[dnames.size()]);
		if(a==0) return null;
	    
		//查询流程所在用户组
		List<String> groupIds=processService.selectDistinctCurrentCandidateGroup(deptNames, titleLike, "0", "1");
        
		//获取用户组名字
		List<String> groupNames=new ArrayList<String>();
		//查询各个考核组未完成比率
		List<Integer> rateOfGroup=new ArrayList<Integer>();
		for (String currentCandidateGroup : groupIds) {
			if(currentCandidateGroup==null||"".equals(currentCandidateGroup)) continue;
			GroupTable group=sysPowerService.selectGroupWithGroupId(currentCandidateGroup);
			groupNames.add(group.getGroupname());
			
			Integer b=processService.selectCountProcess(deptNames, titleLike, "0",currentCandidateGroup);
			
			rateOfGroup.add(b*100/a);
			
			
		}
        Integer c=processService.selectCountProcess(deptNames, titleLike, "1");
        groupNames.add(ProcessType.PROCESS_FINISHED);
        rateOfGroup.add(c*100/a);
        //		添加未提交的比率
		groupNames.add(ProcessType.PROCESS_UNSUBMITTED);
		Integer d=100;
		for (Integer integer : rateOfGroup) {
			d-=integer;
		}
		rateOfGroup.add(d);
		
		String[] groups=groupNames.toArray(new String[groupNames.size()]);
		Integer[] rates=rateOfGroup.toArray(new Integer[rateOfGroup.size()]);
		
		StringBuffer buffer=new StringBuffer("{}");
		
		buffer.insert(1,"\"groups\":"+JsonUtils.getGson().toJson(groups)+",\"rates\":"+JsonUtils.getGson().toJson(rates));
		
		return buffer.toString();
	}
	@RequestMapping("/completionRate")
	@ResponseBody
	public String getCompletionRate1(){
        String postIds=dictionaryService.selectSingleValueOfDic("考核对象id", "月度考核");
		//表格横坐标
		List<String> deptNameList=new ArrayList<String>();
		List<Integer> userNumberList=new ArrayList<Integer>();
		List<Integer> userRateListSubmitted=new ArrayList<Integer>();
		List<Integer> processRateListCompletted=new ArrayList<Integer>();
		//先查询所有二级部门
		
        List<Dept> depts=userService.selectDistinctSecondLevelDept();
        
        if(depts==null || depts.size()==0) return null;
		for (Dept dept : depts) {
			//最小子部门名称
			if(dept.getName().equals("0")) continue;
			//查询所有子部门的人数
			Integer a=userService.selectCountUserNumberWithSecondDeptId(dept.getDid(), postIds);
			
			if(a==0) continue;
			
			deptNameList.add(dept.getName());
			userNumberList.add(a);
			
			//交表人数比例
			String titleLike=DateUtil.formatDateByFormat(DateUtil.addMonths(new Date(),-1), "yyyy年M月份")+"-月度考核";
			List<String> dnames=userService.selectDistinctFirstDeptNames(dept.getDid());
			   
			//dnames.add(dept.getName()); //有些部门领导是以大部门提交的
			
			Integer b=processService.selectCountUserNumberWhichSubmittedProcess(dnames.toArray(new String[dnames.size()]), titleLike);
			
			userRateListSubmitted.add(b*100/a);
			//流程完结数
			Integer c=processService.selectCountProcess(dnames.toArray(new String[dnames.size()]), titleLike,"1");
			processRateListCompletted.add(c*100/a);
			
			
		}

		
		String[] deptNames=deptNameList.toArray(new String[deptNameList.size()]);
		Integer[] userRateSubmitted=userRateListSubmitted.toArray(new Integer[userRateListSubmitted.size()]);
		Integer[] processRateCompletted=processRateListCompletted.toArray(new Integer[processRateListCompletted.size()]);
        
		StringBuffer buffer=new StringBuffer("{}");
				
		buffer.insert(1,"\"deptNames\":"+JsonUtils.getGson().toJson(deptNames)+",\"userRateSubmitted\":"+JsonUtils.getGson().toJson(userRateSubmitted)+",\"processRateCompletted\":"+JsonUtils.getGson().toJson(processRateCompletted)+"");

		return JsonUtils.getGson().toJson(buffer);
		
	}
	@RequestMapping("/completionRate/{taskType}")
	@ResponseBody
	public String getCompletionRate(@PathVariable String taskType){
		String postIds=dictionaryService.selectSingleValueOfDic("考核对象id", "月度考核");
		
		List<String> deptNameList=new ArrayList<String>();
		List<Integer> userNumberList=new ArrayList<Integer>();
		List<Integer> userRateListSubmitted=new ArrayList<Integer>();
		List<Integer> processRateListCompletted=new ArrayList<Integer>();
		//先查询所有最小子部门
		List<Dept> depts=userService.selectDistinctFirstLevelDept(new HashMap<String, Object>());
        
        if(depts==null || depts.size()==0) return null;
		for (Dept dept : depts) {
			//最小子部门名称
			if(dept.getName().equals("0")) continue;
			//查询所有子部门的人数
			Integer a=userService.selectCountUserNumberWithFirsDeptId(dept.getDid(), postIds);
			
			if(a==0) continue;
			
			deptNameList.add(dept.getName());
			userNumberList.add(a);
			
			//交表人数比例
			String titleLike=DateUtil.formatDateByFormat(DateUtil.addMonths(new Date(),-1), "yyyy年M月份")+"-月度考核";
			Integer b=processService.selectCountUserNumberWhichSubmittedProcess(dept.getName(), titleLike);
			userRateListSubmitted.add(b*100/a);
			//流程完结数
			Integer c=processService.selectCountProcess(dept.getName(), titleLike,"1");
			processRateListCompletted.add(c*100/a);
		}

		
		String[] deptNames=deptNameList.toArray(new String[deptNameList.size()]);
		Integer[] userRateSubmitted=userRateListSubmitted.toArray(new Integer[userRateListSubmitted.size()]);
		Integer[] processRateCompletted=processRateListCompletted.toArray(new Integer[processRateListCompletted.size()]);
        
		StringBuffer buffer=new StringBuffer("{}");
				
		buffer.insert(1,"\"deptNames\":"+JsonUtils.getGson().toJson(deptNames)+",\"userRateSubmitted\":"+JsonUtils.getGson().toJson(userRateSubmitted)+",\"processRateCompletted\":"+JsonUtils.getGson().toJson(processRateCompletted)+"");
		
		return JsonUtils.getGson().toJson(buffer);

	}
	
}
