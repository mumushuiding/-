package com.crm.springboot.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.tools.DocumentationTool.Location;

import org.activiti.engine.IdentityService;
import org.apache.logging.log4j.core.appender.rolling.action.DeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crm.springboot.pojos.Action;
import com.crm.springboot.pojos.ActionColumn;
import com.crm.springboot.pojos.ActionGroup;
import com.crm.springboot.pojos.GroupTable;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;




@Controller
@RequestMapping("/system/power/syspower")
public class SysPowerController {
	@Autowired
	private ActivitiService activitiService;
	@Autowired
	private SysPowerService sysPowerService;
	@RequestMapping("/{location}")
	public String Locate(@PathVariable String location,Model model,HttpServletRequest request){
		
		//添加用户组
		if("addGroupForm".equals(location)){
			model.addAttribute("group",new GroupTable());
		}
		//显示权限列表
		if("showAllActions".equals(location)){
			List<ActionColumn> actionColumns=sysPowerService.selectAllColumns();
			List<GroupTable> groups=sysPowerService.selectAllGroups();
			model.addAttribute("actionColumns", actionColumns);
			model.addAttribute("groups", groups);
		}
        //显示组织架构
		if("structure".equals(location)){
			
		}
		
		return "/system/power/"+location;
	}

	@RequestMapping("/{location}/{purpose}")
	public String Location(@PathVariable String location,@PathVariable String purpose,Model model,HttpSession session){
		
		//更新Action
		if("updateActionForm".equals(location)){
			//purpose为actionid
			Action action=sysPowerService.selectActionWithActionId(purpose);
			
			model.addAttribute("action", sysPowerService.selectActionWithActionId(purpose));
		}
		
		if("saveAction".equals(purpose)) model.addAttribute("action",new Action());
		if("saveActionColumn".equals(purpose)) model.addAttribute("actionColumn",new ActionColumn());

		//显示某个用户所在的用户组，及所拥有的权限
		if("userPower".equals(location)){
			model.addAttribute("userid", purpose);
			model.addAttribute("groupManagers", sysPowerService.selectGroupManagerByUserId(purpose));
		}
		if("saveGroup".equals(purpose)){
			model.addAttribute("group", new GroupTable());
		}
        //显示某个用户组下的所有成员
		if("userInGroup".equals(location)){
			session.setAttribute("powercontroller_groupid", purpose);
			return "/system/activiti/"+location;
		}
		
        if("userList".equals(location)){
    		model.addAttribute("taskType", purpose);
    	}
    	

		return "/system/power/"+location;
	}
	@RequestMapping("/{location}/{purpose}/{parameter}")
	public String Location1(@PathVariable String location,@PathVariable String purpose,@PathVariable String parameter,Model model){

		if("saveActionGroup".equals(purpose)) {
			ActionGroup actionGroup=new ActionGroup();
			actionGroup.setGroupid(Integer.valueOf(parameter));
			model.addAttribute("actionGroup", actionGroup);
			model.addAttribute("actionColumns", sysPowerService.selectAllColumns());
		}
		
		if("saveGroupManager".equals(purpose)){
			GroupManager groupManager=new GroupManager();
			groupManager.setUserid(Integer.valueOf(parameter));
			model.addAttribute("groupManager",groupManager);
			model.addAttribute("groups", sysPowerService.selectAllGroups());
		}
		if("updateGroup".equals(purpose)){
			GroupTable groupTable=sysPowerService.selectGroupWithGroupId(parameter);
			model.addAttribute("group", groupTable);
		}
		return "/system/power/"+location;
	}

	@RequestMapping("/saveActionColumn")
	public String saveActionColumn(ActionColumn actionColumn,Model model){
		sysPowerService.saveActionColumn(actionColumn);
		if(actionColumn.getActioncolumnid()!=null){
			return "redirect:/system/power/syspower/showAllActions";
		};
		model.addAttribute("msg", "添加分栏失败!!!!!!!!!");
		return "redirect:/info";
	}
	//====================Action==============
	@RequestMapping("/saveAction")
	public String saveAction(Action action,Model model){
		if(action.getActionid()!=null){
			sysPowerService.updateAction(action);
		}else {
			sysPowerService.save(action);
		}
		if(action.getActionid()!=null){
			
			return "redirect:/system/power/syspower/showAllActions";
		}
		model.addAttribute("msg", "添加action失败");
		return "redirect:/info";
	}
	@RequestMapping("/deleteAction/{id}")
	public String deleteAction(@PathVariable String id){
		sysPowerService.deleteAction(id);
		return "redirect:/system/power/syspower/showAllActions";
	}
	@RequestMapping("/selectAction/{id}")
	@ResponseBody
	public Action selectAction(@PathVariable Serializable id){
		return sysPowerService.selectActionWithActionId(id);
	}
	//==============================ActionGroup==================================
	@RequestMapping("/saveActionGroup")
	public String saveActionGroup(ActionGroup actionGroup,HttpSession session,Model model){
		User user=(User) session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		String[] ids=actionGroup.getIds().split(",");
		actionGroup.setCreatedate(new Date());
		actionGroup.setCreatorid(user.getId());
		actionGroup.setCreatorname(user.getUsername());
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		hashMap.put("actionGroup",actionGroup);
		hashMap.put("ids", ids);
		sysPowerService.saveActionGroupWithActionIds(hashMap);
		return "redirect:/system/power/syspower/showAllActions";
	}
	
	//=================================Group==================================
	@RequestMapping("/saveGroup")
	public String addgroup(@ModelAttribute("group") GroupTable group,Model model,HttpSession session){
		User user=(User) session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		group.setCreatorid(user.getId());
		group.setCreatorname(user.getPhone());
		group.setCreatedDate(new Date());
		
		group=sysPowerService.saveGroup(group);

		if(group.getGroupid()!=0){
			activitiService.saveGroup(group);
		}
		return "redirect:/system/power/syspower/showAllActions";
	}
	@RequestMapping("/updateGroup")
	public String updateGroup(GroupTable groupTable,Model model,HttpSession session,HttpServletRequest request){
		User user=(User) session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		
		groupTable.setCreatedDate(new Date());
		groupTable.setCreatorname(user.getUsername());
		groupTable.setCreatorid(user.getId());
		try {
			sysPowerService.updateGroup(groupTable);
			activitiService.updateGroup(groupTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "/system/activiti/groupList";
		
	}
	@RequestMapping("/deleteGroup/{id}")
	public String deleteGroup(@PathVariable String id){
		sysPowerService.deleteGroupById(id);
		activitiService.deleteGroup(id);
		return "/system/activiti/groupList";
	}
    @RequestMapping("/groupInfo")
    @ResponseBody
    public String groupInfo(Integer pageIndex,Integer pageSize){
    	pageIndex=pageIndex==null?1:pageIndex;
		pageSize=pageSize==null?10:pageSize;
		PageHelper.startPage(pageIndex,pageSize);
		List<GroupTable> groupTables=sysPowerService.selectOnlyAllGroups();
		PageInfo pageInfo=new PageInfo(groupTables);
		return JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(groupTables), pageInfo.getTotal(), pageIndex, pageSize);
    }
    
	
	//=================================GroupManager用户与用户组关系==================================
    /**
     * 批量绑定用户与用户组
     * @param groupManager
     * @param model
     * @param session
     * @return
     */
	@RequestMapping("/saveGroupManager")
	public String addGroupManager(GroupManager groupManager,Model model,HttpSession session){
		User user=(User) session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		String[] ids=groupManager.getIds().split(",");
		groupManager.setCreatedate(new Date());
		groupManager.setCreatorid(user.getId());
		groupManager.setCreatorname(user.getUsername());
		
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		hashMap.put("groupManager", groupManager);
		hashMap.put("ids",ids);
		
		sysPowerService.saveGroupManagerWithGroupIds(hashMap);
		//activiti用户绑定用户组
		if(activitiService.selectUser(groupManager.getUserid())==null){
			activitiService.saveUser(groupManager.getUserid());
		}
		activitiService.bindUserAndGroups(String.valueOf(groupManager.getUserid()), ids);
		return "redirect:/system/power/syspower/userPower/"+groupManager.getUserid();
	}
	
	@RequestMapping("/deleteGroupManager/{userId}/{groupId}/{groupManagerId}")
	public String deleteGroupManager(@PathVariable String userId,@PathVariable String groupId,@PathVariable String groupManagerId,HttpServletRequest request){
		String referer=request.getHeader("referer");
	
		
		sysPowerService.deleteGroupManagerById(groupManagerId);
		activitiService.unBindUserAndGroup(userId, groupId);
		
		return "redirect:"+referer;
	}
	@RequestMapping("/usersInGroup")
	@ResponseBody
	public String getUsersInGroup(Integer pageIndex,Integer pageSize,HttpSession session){
		pageIndex=pageIndex==null?1:pageIndex;
		pageSize=pageSize==null?10:pageSize;
		PageHelper.startPage(pageIndex,pageSize);
		String groupId=(String) session.getAttribute("powercontroller_groupid");
//		session.removeAttribute("powercontroller_groupid");
		List<GroupManager> groupManagers=sysPowerService.selectGroupManagerWithGroupId(groupId);
		PageInfo pageInfo=new PageInfo(groupManagers);
		
		return JsonUtils.formatDataForPagination(JsonUtils.getGson().toJson(groupManagers), pageInfo.getTotal(), pageIndex, pageSize);
	}
}
