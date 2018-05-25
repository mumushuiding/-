package com.crm.springboot.service;

import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.Dictionary;

public interface DictionaryService {
	String selectSingleDictionaryWithName(String name);
	List<String> selectAllDictionaryWithName(String name);
	List<Dictionary> selectAllDics(HashMap<String, Object> params);
	Dictionary selectSingleDic(HashMap<String, Object> params);
	List<String> selectDistinctNameWithType(String type);
}
