package com.crm.springboot.utils;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;

public class JsonUtils {
	private static Gson gson;
	public static Gson getGson(){
		if(gson==null) return new Gson();
		return gson;
	}
	public static String formatDataForPagination(String jsonString,long total,int pageIndex,int pageSize){
		StringBuffer stringBuffer=new StringBuffer("{}");
		stringBuffer.insert(1,"\"total\":"+total+",\"page\":"+pageIndex+",\"pageSize\":"+pageSize+",\"rows\":"+jsonString);
		return stringBuffer.toString();
	}
	public static String formatListForPagination(List<?> list,Integer pageIndex,Integer pageSize){
		
		PageInfo pageInfo=new PageInfo(list);
		return formatDataForPagination(getGson().toJson(list), pageInfo.getTotal(), pageIndex, pageSize);
	}
	public static void startPageHelper(Integer pageIndex,Integer pageSize){

		pageIndex=pageIndex==null?1:pageIndex;
		pageSize=pageSize==null?10:pageSize;
		PageHelper.startPage(pageIndex, pageSize);
	}
}

