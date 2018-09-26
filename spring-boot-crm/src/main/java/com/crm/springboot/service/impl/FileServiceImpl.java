package com.crm.springboot.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import com.crm.springboot.config.AvailableSettings;
import com.crm.springboot.service.FileService;
import com.crm.springboot.utils.DateUtil;
@Controller
public class FileServiceImpl implements FileService{
    
	
	@Override
	public File uploadSingleAndReturnFile(HttpServletRequest request, MultipartFile file) throws Exception{
		// 如果文件不为空，写入上传路径
				if(!file.isEmpty()){
					// 上传文件路径
					String path = request.getServletContext().getRealPath(
			                "/upload/");
					// 上传文件名
					String filename = file.getOriginalFilename();
				    File filepath = new File(path,filename);
					// 判断路径是否存在，如果不存在就创建一个
			        if (!filepath.getParentFile().exists()) { 
			        	filepath.getParentFile().mkdirs();
			        }
			        // 将上传文件保存到一个目标文件当中
			        File result=new File(path+File.separator+DateUtil.formatFileName(new Date())+filename);
			        
					file.transferTo(result);
					
					return result;
				}else{
					return null;
				}
	}

	@Override
	public void uploadFile(HttpServletRequest request, MultipartFile file,String path) throws Exception {
		if(!file.isEmpty()){
			
			// 上传文件路径
			String path1 =System.getProperty("user.dir")+File.separator+"upload"+File.separator+path;
			
			// 上传文件名
			String filename = file.getOriginalFilename();
		    File filepath = new File(path1,filename);
		    
			// 判断路径是否存在，如果不存在就创建一个
	        if (!filepath.getParentFile().exists()) { 
	        	filepath.getParentFile().mkdirs();
	        }
	        // 将上传文件保存到一个目标文件当中
	        File result=new File(path1+File.separator+filename);
	        
			file.transferTo(result);
		}
		
	}



}
