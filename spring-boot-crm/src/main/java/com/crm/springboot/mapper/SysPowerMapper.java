package com.crm.springboot.mapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.Action;
import com.crm.springboot.pojos.ActionColumn;
import com.crm.springboot.pojos.ActionGroup;
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.GroupTable;

public interface SysPowerMapper extends BaseMapper{
	//===============Action================================
    void updateAction(Action action);
	public Action selectActionWithActionId(Serializable id);
	public ActionColumn selectActionWithActionColumnId(Serializable id);
	public void saveActionColumn(ActionColumn actionColumn);
	public List<ActionColumn> selectAllColumns();
	void deleteAction(Serializable id);
	
	//================Group========================
	public void saveGroup(GroupTable groupTable);
	public List<GroupTable> selectAllGroups();
	public List<GroupTable> selectOnlyAllGroups();
	public void updateGroup(GroupTable groupTable);
	public GroupTable selectGroupWithGroupId(Serializable id);
	public void deleteGroupById(Serializable id);
	public String selectGroupIdWithGroupName(String groupname);
	//=================actionGroup==================
	public void saveActionGroup(ActionGroup actionGroup);
	/**
	 * hashmap参数包含两个参数，第一个为actionGroup,别一个为actionid集合,因为只有actionid不同;
	 * @param hashMap
	 */
	public void saveActionGroupWithActionIds(HashMap<String, Object> hashMap);
	
	public List<ActionGroup> selectActionGroupWithGroupId(Serializable id);
	
	//group
	
	List<String> selectAllGroupIds(HashMap<String, Object> params);
	//================groupManager==========================
	public void saveGroupManager(GroupManager groupManager);
	/**
	 * hashmap参数包含两个参数，第一个为groupManager,别一个为groupid数组集合;
	 * @param hashMap
	 */
	public void saveGroupManagerWithGroupIds(HashMap<String, Object> hashMap);
	
	public List<GroupManager> selectGroupManagerByUserId(Serializable userid);
	public void deleteGroupManagerById(Serializable id);
	public void deleteGroupManager(HashMap<String, Object> params);
	public List<GroupManager> selectGroupManagerWithGroupId(Serializable groupId);
	public List<GroupManager> selectAllGroupManagers();
	List<GroupManager> selectAllGroupManagersWithHashMap(HashMap<String, Object> params);
	List<GroupManager> selectGroupManager(HashMap<String, Object> params);
	List<String> selectAllGroupIdsFromGroupManager(HashMap<String, Object> params);
}
