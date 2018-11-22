package com.crm.springboot.service.impl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.crm.springboot.mapper.SysPowerMapper;
import com.crm.springboot.pojos.Action;
import com.crm.springboot.pojos.ActionColumn;
import com.crm.springboot.pojos.ActionGroup;
import com.crm.springboot.pojos.Dictionary;
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.GroupTable;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.DictionaryService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;


@Service
public class SysPowerServiceImpl implements SysPowerService{
	private static final Log log=LogFactory.getLog(SysPowerServiceImpl.class);
	@Autowired
	private SysPowerMapper sysPowerMapper;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivitiService activitiService;
	@Override
	public <T> T save(T t) {
		sysPowerMapper.save(t);
		return t;
	}

	@Override
	public <T> void deleteById(Serializable id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T update(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionColumn selectActionWithActionColumnId(Serializable id) {
		
		return sysPowerMapper.selectActionWithActionColumnId(id);
	}

	@Override
	public ActionColumn saveActionColumn(ActionColumn actionColumn) {
		sysPowerMapper.saveActionColumn(actionColumn);
		return actionColumn;
	}

	@Override
	public List<ActionColumn> selectAllColumns() {
		
		return sysPowerMapper.selectAllColumns();
	}

	@Override
	public GroupTable saveGroup(GroupTable group) {
		sysPowerMapper.saveGroup(group);
		return group;
	}

	@Override
	public List<GroupTable> selectAllGroups() {
		
		return sysPowerMapper.selectAllGroups();
	}

	@Override
	public ActionGroup saveActionGroup(ActionGroup actionGroup) {
		sysPowerMapper.saveActionGroup(actionGroup);
		return actionGroup;
	}

	@Override
	public void saveActionGroupWithActionIds(HashMap<String, Object> hashMap) {
		sysPowerMapper.saveActionGroupWithActionIds(hashMap);
	}

	@Override
	public List<ActionGroup> selectActionGroupWithGroupId(Serializable id) {
		
		return sysPowerMapper.selectActionGroupWithGroupId(id);
	}

	@Override
	public Action selectActionWithActionId(Serializable id) {
		
		return sysPowerMapper.selectActionWithActionId(id);
	}

	@Override
	public GroupManager saveGroupManager(GroupManager groupManager) {
		sysPowerMapper.saveGroupManager(groupManager);
		return groupManager;
	}

	@Override
	public List<GroupManager> selectGroupManagerByUserId(Serializable userid) {
		
	
		return sysPowerMapper.selectGroupManagerByUserId(userid);
	}

	@Override
	public void saveGroupManagerWithGroupIds(HashMap<String, Object> hashMap) {
		sysPowerMapper.saveGroupManagerWithGroupIds(hashMap);
		
	}

	@Override
	public List<GroupTable> selectOnlyAllGroups() {
		
		return sysPowerMapper.selectOnlyAllGroups();
	}

	@Override
	public GroupTable updateGroup(GroupTable groupTable) {
		sysPowerMapper.updateGroup(groupTable);
		return groupTable;
	}

	@Override
	public GroupTable selectGroupWithGroupId(Serializable id) {
		
		return sysPowerMapper.selectGroupWithGroupId(id);
	}

	@Override
	public void deleteGroupById(Serializable id) {
		sysPowerMapper.deleteGroupById(id);
		
	}

	@Override
	public void deleteGroupManagerById(Serializable id) {
		sysPowerMapper.deleteGroupManagerById(id);
		
	}

	@Override
	public List<GroupManager> selectGroupManagerWithGroupId(Serializable groupId) {
		
		return sysPowerMapper.selectGroupManagerWithGroupId(groupId);
	}

	@Override
	public List<GroupManager> selectAllGroupManagers() {

		return sysPowerMapper.selectAllGroupManagers();
	}

	@Override
	public void saveGroupToUser(User user, User createtor, String[] groupIds) {
		GroupManager groupManager=new GroupManager();
		groupManager.setCreatedate(new Date());
		
		if (createtor==null) {
			groupManager.setCreatorname("系统设置");
		}else {
			groupManager.setCreatorid(createtor.getId());
			groupManager.setCreatorname(createtor.getUsername());
		}
		
		
		
		groupManager.setUserid(user.getId());
		groupManager.setUsername(user.getUsername());
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		hashMap.put("groupManager",groupManager);
		hashMap.put("ids",groupIds);
		this.saveGroupManagerWithGroupIds(hashMap);
	}

	@Override
	public void updateAction(Action action) {
		sysPowerMapper.updateAction(action);
		
	}

	@Override
	public void deleteAction(Serializable id) {
		sysPowerMapper.deleteAction(id);
		
	}

	@Override
	public List<String> selectAllActionsWithUserId(Serializable userId) {
		
		List<String> result=new ArrayList<String>();

		return result;
	}

	@Override
	public List<GroupManager> selectAllGroupManagersWithHashMap(HashMap<String, Object> params) {
		
		return sysPowerMapper.selectAllGroupManagersWithHashMap(params);
	}

	@Override
	public List<String> selectAllGroupIdsWithHashMap(HashMap<String, Object> params) {
		List<String> result=new ArrayList<String>();
		for (GroupManager groupManager: this.selectAllGroupManagersWithHashMap(params)) {
			result.add(groupManager.getGroupid());
		}
		return result;
	}

	@Override
	public List<String> selectAllGroupIds(String groupname) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("groupname", groupname);
		return this.selectAllGroupIds(params);
	}
    @Override
    public List<String> selectAllGroupIds(HashMap<String, Object> params){
    	return sysPowerMapper.selectAllGroupIds(params);
    }
	@Override
	public String selectGroupIdWithGroupName(String groupname) {
		
		return sysPowerMapper.selectGroupIdWithGroupName(groupname);
	}


	@Override
	public String getGroupNameWithDepartmentNameAndGroupType(String[] departmentNames, String groupType) {
		if(departmentNames==null)return null;
        HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("values",departmentNames);
		params.put("type", groupType);
		Dictionary dictionary=dictionaryService.selectSingleDic(params);
		if(dictionary==null) return null;
		return dictionary.getName();
	}

	@Override
	public HashMap<String, Object> getDepartmentsWithGroupname(String groupname, String groupType) {
		  if(groupname==null||"".equals(groupname))return null;
		  List<String> firstLevelIds=new ArrayList<String>();
		  List<String> secondLevelIds=new ArrayList<String>();
		  HashMap<String, Object> params1=new HashMap<String, Object>();
		  //查询考核组对应的部门
		  params1.put("names",groupname.split(","));
		  params1.put("type",groupType);
	      List<Dictionary> dictionaries=dictionaryService.selectAllDics(params1);
	    
	      //根据部门不同的组别进行分流
	      HashMap<String, Object> params=new HashMap<String, Object>();
	      for (Dictionary dictionary : dictionaries) {
	    	  params.put("name", dictionary.getValue());
	    	  HashMap<String, Object> dept=userService.selectSingleDeptAsHashMap(params);
	    	  if(dept==null)continue;
	    	 
	    	  switch ((String)dept.get("level")) {
			    case "first":
			    	firstLevelIds.add(String.valueOf(dept.get("did")));
				break;
			    case "second":
					secondLevelIds.add(String.valueOf(dept.get("did")));
					break;
			    default:
				  break;
			}
		  }
	      params.remove("name");
	      if(firstLevelIds.size()>0) params.put("firstLevelIds", firstLevelIds.toArray(new String[firstLevelIds.size()]));
	      if(secondLevelIds.size()>0) params.put("secondLevelIds",secondLevelIds.toArray(new String[secondLevelIds.size()]));
		return params;
	}

	@Override
	public List<String> getGroupsOfOvereerWithUserId(String userId) {
		//查询用户包含哪些审批组
    	List<String> candidateGroups=this.selectAllGroupIdsFromGroupManager(userId);
		//查询考核组和考核办的用户组
		List<String> list=this.selectAllGroupIds("考");
		List<String> assessGroups=new ArrayList<String>();
		//添加候选组
	    for (String string : list) {
			if(candidateGroups.contains(string)){
				assessGroups.add(string);
			}
		}
        if(assessGroups==null||assessGroups.size()==0)return null;
		return assessGroups;
	}

	@Override
	public List<String> getGroupsOfLeaderWithUserId(String userId) {
		//查询用户包含哪些审批组
    	List<String> candidateGroups=activitiService.candidateGroups(userId);
    	List<String> deptManager=this.selectAllGroupIds("部门主任组");
    	List<String> leader=this.selectAllGroupIds("分管领导组");
    	List<String> assessGroups=new ArrayList<String>();
    	
    	//添加候选组
	    for (String string : deptManager) {
			if(candidateGroups.contains(string)){
				assessGroups.add(string);
			}
		}
	    for (String string : leader) {
			if(candidateGroups.contains(string)){
				assessGroups.add(string);
			}
		}
        if(assessGroups==null||assessGroups.size()==0)return null;
		return assessGroups;
	}

	@Override
	public List<GroupManager> selectGroupManager(HashMap<String, Object> params) {
		
		return sysPowerMapper.selectGroupManager(params);
	}

	@Override
	public GroupManager selectSingleGroupManager(HashMap<String, Object> params) {
		List<GroupManager> groupManagers=this.selectGroupManager(params);
		if(groupManagers==null||groupManagers.size()==0) return null;
		return groupManagers.get(0);
	}

	@Override
	public GroupManager selectSingleGroupManager(Integer userid, String groupid) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("userid", userid);
		params.put("groupid", groupid);
		return this.selectSingleGroupManager(params);
	}

	@Override
	public List<String> getGroupIds(User user) {
		List<String> result=new ArrayList<String>();
		for (GroupManager groupManager : user.getUserLinkGroup()) {
			result.add(groupManager.getGroupid());
		}
		return result;
	}

	@Override
	public void deleteGroupManager(HashMap<String, Object> params) {
		sysPowerMapper.deleteGroupManager(params);
		
	}

	@Override
	public void deleteGroupManager(Integer userid, String groupids) {
		if(StringUtils.isEmpty(groupids)) return;
		HashMap<String, Object> params=new HashMap<String, Object>();
		System.out.println(" userid="+userid+" and groupid in ("+groupids+")");
		params.put("condition", " userid="+userid+" and groupid in ("+groupids+")");
		sysPowerMapper.deleteGroupManager(params);
		
	}

	@Override
	public void deleteGroupManager(Integer userid, List<String> groupids) {
		if(groupids.size()==0) return;
		StringBuffer buffer=new StringBuffer();
		for (String string : groupids) {
			buffer.append(string+",");
		}
		this.deleteGroupManager(userid, buffer.toString());
	}

	@Override
	public void saveGroupManagerWithGroupIds(GroupManager groupManager, String[] ids) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("groupManager", groupManager);
		params.put("ids", ids);
		this.saveGroupManagerWithGroupIds(params);
		
	}

	@Override
	public List<String> selectAllGroupIdsFromGroupManager(HashMap<String, Object> params) {
		
		return sysPowerMapper.selectAllGroupIdsFromGroupManager(params);
	}

	@Override
	public List<String> selectAllGroupIdsFromGroupManager(String userId) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("userid", userId);
		return this.selectAllGroupIdsFromGroupManager(params);
	}




	

}
