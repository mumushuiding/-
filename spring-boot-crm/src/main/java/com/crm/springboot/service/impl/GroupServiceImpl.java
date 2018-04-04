package com.crm.springboot.service.impl;

import java.io.Serializable;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.crm.springboot.mapper.GroupMapper;
import com.crm.springboot.service.GroupService;
@Service
public class GroupServiceImpl implements GroupService{
	
	@Autowired
	private GroupMapper groupMapper;
	@Override
	public <T> T save(T t) {
		groupMapper.save(t);
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
	public <T> T getById(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

}
