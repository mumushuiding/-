package com.crm.springboot.utils;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;



public class Files {
	private  List<String> fileList=new ArrayList<String>();
	
	public  void getFiles(String pathname){
		File root=new File(pathname);
		String rootPath=root.getAbsolutePath();
		if(!root.exists()) return;
		File[] files=root.listFiles();
		for(File file:files){
			if(file.isDirectory()){
				getFiles(file.getAbsolutePath());
			}else {
				String result=file.getAbsolutePath();
				fileList.add(result);
			}
		}
	}
  
	public  List<String> getFileList() {
	
		return fileList;
	}
}
