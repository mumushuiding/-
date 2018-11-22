package com.crm.springboot.config.activitiServiceTask;



import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.process.AbstractProcessPojo;
import com.crm.springboot.pojos.process.AddDeptForProcess;
import com.crm.springboot.pojos.process.AddMarksForProcess;
import com.crm.springboot.pojos.process.DeleteMarkForProcess;
import com.crm.springboot.pojos.process.DeleteMarksByOverTimeForProcess;
import com.crm.springboot.pojos.process.DeleteUserForProcess;
import com.crm.springboot.pojos.process.DisableUserForProcess;
import com.crm.springboot.pojos.process.GroupManagerForProcess;
import com.crm.springboot.pojos.process.UpdateMarkForProcess;
import com.crm.springboot.pojos.user.DepartmentLevel;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.DeptIdentityLink;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.RegexUtils;
@Service
public class ServiceTaskForOthers implements JavaDelegate{
	@Autowired
    private ActivitiService activitiService;
	@Autowired
	private SysPowerService sysPowerService;
	@Autowired
	private UserService userService;
	@Autowired
	private ResponsibilityService responsibilityService;
	@Override
	public void execute(DelegateExecution execution) {
		String processInstanceId=execution.getProcessInstanceId();
		AbstractProcessPojo aPojo=(AbstractProcessPojo)activitiService.getVariableFromProcessInstance(processInstanceId, "pojo");
		
		
		switch (aPojo.getBusinessType()) {
		case ProcessType.BUSINESSTYPE_GROUPMANAGER:
			groupManager(aPojo);
			break;
		case ProcessType.BUSINESSTYPE_DELETEMARKSBYOVERTIME:
			deleteMarksByOverTime(aPojo);
			break;
		case ProcessType.BUSINESSTYPE_ADDMARKS:
			addMarks(aPojo);
			break;
		case ProcessType.BUSINESSTYPE_UPDATEMARK:
			updateMarks(aPojo);
			break;
		case ProcessType.BUSINESSTYPE_DELETEUSER:
			deleteUser(aPojo);
			break;
		case ProcessType.BUSINESSTYPE_DISABLEUSER:
			disableUser(aPojo);
			break;
		case ProcessType.BUSINESSTYPE_DELETEMARK:
			deleteMark(aPojo);
			break;
		case ProcessType.BUSINESSTYPE_ADDDEPARTMENT:
			addDepartment(aPojo);
			break;
		
		default:
			break;
		}
	}
   
  

private void addDepartment(AbstractProcessPojo aPojo) {
	    AddDeptForProcess pojo=(AddDeptForProcess)aPojo;
	    
		//跟添加部门相关的表有info_dept(部门信息),info_dept_identitylink(部门层级关系),info_dept_type(部门类型)
	    
	   
	    //先确定部门是否已经存在 ，不存在就添加(info_dept)
	    
	    
	    if(!userService.isDeptExists(pojo.getDepartment())){
	    	
	    	try {
	    		userService.saveDepartment(pojo.getDepartment());
	    		System.out.println("新部门的ID:"+pojo.getDepartment().getDid());
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
	    	
	    }else {
			System.out.println("部门["+pojo.getDepartment().getName()+"]已经存在");
			return;
		}
	    //确定部门层级关系
	    switch (pojo.getDepartment().getLevel()) {
		    case "first":
		      
		      userService.saveDeptIdentityLink(pojo.getDepartment().getDid(), pojo.getDepartment().getSecondId(), pojo.getDepartment().getThirdId());
			  break;
            case "second":
              userService.saveDeptIdentityLink(null, pojo.getDepartment().getDid(), pojo.getDepartment().getThirdId());
			   break;
            case "third":
            	userService.saveDeptIdentityLink(null,null,pojo.getDepartment().getDid());
            	break;
		    default:
			  break;
		}    
	   
        
		
		
	}

private void deleteMark(AbstractProcessPojo aPojo) {
		DeleteMarkForProcess pojo=(DeleteMarkForProcess) aPojo;
		for (Mark mark : pojo.getMarks()) {
			if(mark.getMarkId()==null) continue;
			System.out.println("*********markid:"+mark.getMarkId());
			responsibilityService.deleteMark(String.valueOf(mark.getMarkId()));
		}

	}
private void disableUser(AbstractProcessPojo aPojo) {
	DisableUserForProcess pojo=(DisableUserForProcess)aPojo;
	if(pojo.getUsers().size()==0) return ;
	String[] ids=new String[pojo.getUsers().size()];
	for (int i=0;i<pojo.getUsers().size();i++) {
		
		ids[i]=String.valueOf(pojo.getUsers().get(i).getId());
		System.out.println("********************删除："+pojo.getUsers().get(i).getId());
	}
	userService.disableUsersByUserIds(ids);
	
}
private void deleteUser(AbstractProcessPojo aPojo) {
		DeleteUserForProcess pojo=(DeleteUserForProcess)aPojo;
		if(pojo.getUsers().size()==0) return ;
		String[] ids=new String[pojo.getUsers().size()];
		for (int i=0;i<pojo.getUsers().size();i++) {
			
			ids[i]=String.valueOf(pojo.getUsers().get(i).getId());
			System.out.println("********************删除："+pojo.getUsers().get(i).getId());
		}
		userService.deleteUsersByUserIds(ids);
		
	}
private void updateMarks(AbstractProcessPojo aPojo) {
		UpdateMarkForProcess pojo=(UpdateMarkForProcess) aPojo;
		responsibilityService.updateMark(pojo.getMark());
		
	}
   private void addMarks(AbstractProcessPojo aPojo){
	   AddMarksForProcess pojo=(AddMarksForProcess)aPojo;
	   List<Mark> marks=pojo.getMarks();
	   Date startDate = null;
	   Date endDate = null;
	   if (pojo.getTimes().matches("(.*?)年(.*?)月")) {
		    int year=Integer.valueOf(RegexUtils.getIndexGroup(1, pojo.getTimes(), "(.*?)年(.*?)月"));
			int month=Integer.valueOf(RegexUtils.getIndexGroup(2, pojo.getTimes(), "(.*?)年(.*?)月"));
			startDate=DateUtil.getFirstDayOfMonth(year, month);
			endDate=DateUtil.getLastDayOfMonth(year, month);
	   }else {
		  return ;
	   }
	   for (Mark mark : marks) {
		   if(mark.getMarkReason()==null||"".equals(mark.getMarkReason())) continue;
		   responsibilityService.saveProjectAndMark(pojo.getId(), startDate, endDate, mark.getMarkReason(), mark.getAccordingly(), mark.getMarkNumber(),1);
	   }
   }
   private void deleteMarksByOverTime(AbstractProcessPojo aPojo){
	   DeleteMarksByOverTimeForProcess pojo=(DeleteMarksByOverTimeForProcess)aPojo;
	   if(pojo.getTimes()==null||"".equals(pojo.getTimes())) return;
	   String[] times=pojo.getTimes().split(",");
	  
	  
	   for (String string : times){
		   responsibilityService.deleteAllMonthWhichDelaySubmitMonthAssessApply(string, aPojo.getProcessBean().getUser().getId());
	   }
   }
   private void groupManager(AbstractProcessPojo aPojo){
	   GroupManagerForProcess pojo=(GroupManagerForProcess) aPojo;
		Integer userid=pojo.getProcessBean().getUser().getId();
		
		try {
			HashMap<String, Object> params=new HashMap<String, Object>();
			params.put("userId", userid);
			List<GroupManager> gList=sysPowerService.selectAllGroupManagersWithHashMap(params);
			StringBuffer buffer=new StringBuffer();
			if(gList.size()>0){
				for (GroupManager groupManager : gList) {
					buffer.append(groupManager.getGroupid()+",");
				}
				buffer.deleteCharAt(buffer.length()-1);
			}
			
			
			
			
			sysPowerService.deleteGroupManager(userid, buffer.toString());
			
	    	if(pojo.getGroupManager().getIds()==null||"".equals(pojo.getGroupManager().getIds()))return;
	    	pojo.getGroupManager().setUserid(userid);
	    	
	    	
			sysPowerService.saveGroupManagerWithGroupIds(pojo.getGroupManager(), pojo.getGroupManager().getIds().split(","));
			
	    	
		} catch (Exception e) {
			throw e;
		}
   }
}
