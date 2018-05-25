package com.crm.springboot.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.Action;
import com.crm.springboot.pojos.ActionColumn;
import com.crm.springboot.pojos.ActionGroup;
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.GroupTable;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;

public interface SysPowerService extends BaseService<Action>{
	//===============Action================================
    void updateAction(Action action);
	public Action selectActionWithActionId(Serializable id);
	public ActionColumn selectActionWithActionColumnId(Serializable id);
	public ActionColumn saveActionColumn(ActionColumn actionColumn);
	public List<ActionColumn> selectAllColumns();
	void deleteAction(Serializable id);
	//================group=========================
	public GroupTable saveGroup(GroupTable group);
	List<String> selectAllGroupIds(String groupname);
	/**
	 * 级联查询
	 * @return
	 */
	public List<GroupTable> selectAllGroups();
	/**
	 * 只查询groups
	 * @return
	 */
	public List<GroupTable> selectOnlyAllGroups();
	
	public GroupTable updateGroup(GroupTable groupTable);
	
	public GroupTable selectGroupWithGroupId(Serializable id);
	
	public void deleteGroupById(Serializable id);
	
	public String selectGroupIdWithGroupName(String groupname);
	
	public String getGroupNameWithDepartmentNameAndGroupType(String departmentName,String groupType);
	public String getGroupNameWithDepartmentNameAndGroupType(String[] departmentNames,String groupType);
	
	public String getGroupIdWithDepartmentNameAndGroupType(String departmentName,String groupType);
	
	public HashMap<String, Object> getDepartmentsWithGroupname(String groupname,String groupType);
	//查询用户包含哪些考核组
	public List<String> getGroupsOfOvereerWithUserId(String userId);
	//=================actionGroup==================
	public ActionGroup saveActionGroup(ActionGroup actionGroup);
	public void saveActionGroupWithActionIds(HashMap<String, Object> hashMap);
	public List<ActionGroup> selectActionGroupWithGroupId(Serializable id);
	
	//================groupManager==========================
	public void saveGroupToUser(User user,User createtor,String[] groupIds);
	public GroupManager saveGroupManager(GroupManager groupManager);
	public void saveGroupManagerWithGroupIds(HashMap<String, Object> hashMap);
	public List<GroupManager> selectGroupManagerByUserId(Serializable userid);
	public void deleteGroupManagerById(Serializable id);
	public List<GroupManager> selectGroupManagerWithGroupId(Serializable groupId);
	public List<GroupManager> selectAllGroupManagers();
	List<GroupManager> selectAllGroupManagersWithHashMap(HashMap<String, Object> params);
	List<String> selectAllGroupIdsWithHashMap(HashMap<String, Object> params);
	List<String> selectAllActionsWithUserId(Serializable userId);
}
