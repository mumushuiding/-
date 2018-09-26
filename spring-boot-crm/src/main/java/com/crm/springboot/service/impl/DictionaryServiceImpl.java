package com.crm.springboot.service.impl;


import java.util.HashMap;
import java.util.List;

import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.springboot.mapper.DictionaryMapper;
import com.crm.springboot.pojos.Dictionary;
import com.crm.springboot.service.DictionaryService;
@Service
public class DictionaryServiceImpl implements DictionaryService{
   
	@Autowired
	private DictionaryMapper dictionaryMapper;

	@Override
	public List<String> selectAllDictionaryWithName(String name) {
		
		return dictionaryMapper.selectAllDictionaryWithName(name);
	}
	@Override
	public String selectSingleDictionaryWithName(String name) {
		List<String> dList=this.selectAllDictionaryWithName(name);
		if (dList==null||dList.size()==0) return null;
		return dList.get(0);
	}
	@Override
	public List<Dictionary> selectAllDics(HashMap<String, Object> params) {
		
		return dictionaryMapper.selectAllDics(params);
	}
	@Override
	public Dictionary selectSingleDic(HashMap<String, Object> params) {
		List<Dictionary> dList=this.selectAllDics(params);
		if (dList==null||dList.size()==0) return null;
		return dList.get(0);
	}
	@Override
	public List<String> selectDistinctNameWithType(String type) {
		
		return dictionaryMapper.selectDistinctNameWithType(type);
	}
	@Override
	public Dictionary selectSingleDic(String name, String type) {
        HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("type", type);
		params.put("name", name);
		return this.selectSingleDic(params);
	}
	@Override
	public String selectSingleValueOfDic(String name, String type) {
		Dictionary dictionary=selectSingleDic(name, type);
		if(dictionary==null) return null;
		return dictionary.getValue();
	}
	@Override
	public void updateDic(Dictionary dictionary) {
		dictionaryMapper.updateDic(dictionary);
		
	}
	@Override
	public List<Dictionary> selectAllDics(String name, String type) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("type", type);
		params.put("name", name);
		return this.selectAllDics(params);
	}
	@Override
	public List<Dictionary> selectAllDics(String type) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("type", type);
		return this.selectAllDics(params);
	}
	@Override
	public List<Dictionary> selectAllDics2(String value, String type) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("type", type);
		params.put("value", value);
		return this.selectAllDics(params);
	}
	@Override
	public Dictionary selectSingleDict(String value, String type) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("type", type);
		params.put("value", value);
		return selectSingleDic(params);
	}


}
