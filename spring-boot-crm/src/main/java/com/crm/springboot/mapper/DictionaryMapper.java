package com.crm.springboot.mapper;

import java.util.HashMap;
import java.util.List;

import com.crm.springboot.pojos.Dictionary;

public interface DictionaryMapper {
	List<String> selectAllDictionaryWithName(String name);
	List<Dictionary> selectAllDics(HashMap<String, Object> params);
	List<String> selectDistinctNameWithType(String type);
}
