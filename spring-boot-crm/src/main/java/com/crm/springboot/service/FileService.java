package com.crm.springboot.service;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	//上传文件到指定文件夹
	void uploadFile(HttpServletRequest request,MultipartFile file,String path) throws Exception;
	File uploadSingleAndReturnFile(HttpServletRequest request,MultipartFile file) throws Exception;
}
