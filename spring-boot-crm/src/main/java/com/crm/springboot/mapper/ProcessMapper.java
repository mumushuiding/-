package com.crm.springboot.mapper;

import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.user.User;

public interface ProcessMapper extends BaseMapper<ProcessBean>{
	List<ProcessBean> selectAllProcess(HashMap<String , Object> params);
	List<String> selectAllProcessInstanceIds(HashMap<String , Object> params);
	void deleteProcessByProcessInstanceIds(String[] ids);
	void updateProcess(ProcessBean processBean);
	/**
	 * 查询所有未按时提交指定表格的员工信息
	 * @param params
	 * @return
	 */
	List<User> selectAllUsersWhoUnsubmittedProcess(HashMap<String, Object> params);
}
