package com.crm.springboot.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	public static void getOutputStream(InputStream in,OutputStream out){
		byte[] b=new byte[1024];
		try {
			for(int len=-1;(len=in.read(b))!=-1;){
				out.write(b,0,len);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	public static String getFileExtensionName(File file){
		
		return file.getName().lastIndexOf(".")==-1?"":file.getName()
				.substring(file.getName().lastIndexOf(".")+1);
	}
	
}
