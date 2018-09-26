package com.crm.springboot.mapper;

import java.util.HashMap;

import com.crm.springboot.pojos.activiti.TaskEntity;

public interface ActivitiMapper {
	void updateTaskInst(TaskEntity taskEntity);
	
	String selectAVGDration(HashMap<String, Object> params);

	String selectSumDuration(HashMap<String, Object> params);
}
