package com.crm.springboot.service;

import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.Dictionary;

public interface DictionaryService {
	String selectSingleDictionaryWithName(String name);
	List<String> selectAllDictionaryWithName(String name);
	
	List<Dictionary> selectAllDics(HashMap<String, Object> params);
	List<Dictionary> selectAllDics(String name,String type);
	List<Dictionary> selectAllDics2(String value,String type);
	List<Dictionary> selectAllDics(String type);
	Dictionary selectSingleDic(HashMap<String, Object> params);
	List<String> selectDistinctNameWithType(String type);
	Dictionary selectSingleDic(String name,String type);
	Dictionary selectSingleDict(String value,String type);
	String selectSingleValueOfDic(String name,String type);
	void updateDic(Dictionary dictionary);

}
