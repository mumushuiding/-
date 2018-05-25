package com.crm.springboot.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.NonWritableChannelException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.tools.DocumentationTool.Location;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.crm.springboot.config.AvailableSettings;
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.DeptIdentityLink;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.service.DictionaryService;
import com.crm.springboot.service.FileService;
import com.crm.springboot.service.ResponsibilityService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
import com.crm.springboot.utils.DateUtil;
import com.crm.springboot.utils.ExcelUtils;
import com.crm.springboot.utils.FileUtils;
import com.crm.springboot.utils.Files;
import com.crm.springboot.utils.JsonUtils;

@Controller
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	private FileService fileService;
	@Autowired
	private SysPowerService sysPowerService;
	@Autowired
	private UserService userService;
	@Autowired
	private ResponsibilityService responsibilityService;
	@Autowired
	private DictionaryService dictionaryService;
	
	@RequestMapping("/{var1}")
	public String Location(@PathVariable String var1){
		
		
		return "/file/"+var1;
	}
/**
 * 	**********************************************download******************************
 */
	/**
	 * 
	 * @param request
	 * @param filename
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/download")
	public ResponseEntity<byte[]> download(HttpServletRequest request,
			@RequestParam String filename,Model model) throws IOException{
		//下载文件路径 
		System.out.println("download filename="+filename);
		File file=new File(filename);
		HttpHeaders headers=new HttpHeaders();
		//下载显示的文件名，解决中文名称乱码问题
		String downloadFileName=new String(filename.getBytes("UTF-8"),"ISO-8859-1");
		//通知浏览器以attachment(下载方式）打开
		headers.setContentDispositionFormData("attachment", downloadFileName);
		//application/octet-stream:二进制流数据（最常见的文件下载）
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		
		return new ResponseEntity<byte[]>(FileUtil.readAsByteArray(file),headers,HttpStatus.CREATED);
	}
	
	
	//获取系统指定目录下的文件链接
	@RequestMapping("/getFileLinks/{pathname}")
	@ResponseBody
	public String getFileLinks(@PathVariable String pathname,HttpServletRequest request){
		Files files=new Files();
		files.getFiles(AvailableSettings.TEMP_UPLOAD_LOCATION+File.separator+pathname);
		System.out.println("pathname="+AvailableSettings.TEMP_UPLOAD_LOCATION+File.separator+pathname);
		System.out.println(JsonUtils.getGson().toJson(files.getFileList()));
		return JsonUtils.getGson().toJson(files.getFileList());
	}
	
/**
 * 	**********************************************upload*****************************************************
 */
	
	@RequestMapping("/uploadFile/{path}")
	@ResponseBody
	public String uploadFile(HttpSession session,
			HttpServletRequest request,
			@RequestParam("file") MultipartFile file,@PathVariable String path){
		String result="";
		try {
			fileService.uploadFile(request, file, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping("/uploadProjectAndMark")
	@ResponseBody
	public String uploadProjectAndMark(HttpSession session,
			HttpServletRequest request,
			@RequestParam("file") MultipartFile file){
		StringBuffer buffer=new StringBuffer();		
		try {
			File result=fileService.uploadSingleAndReturnFile(request, file);
			List<List<String>> list=ExcelUtils.readExcel(result);

			for (int i = 1; i < list.size(); i++) {
				List<String> list2=list.get(i);
				String userId=userService.selectUserIdByPhone(list2.get(0));
				if(userId==null) {
					buffer.append(list2.get(0)+",");
					continue;
				}


				responsibilityService.saveProjectAndMark(userId, 
						DateUtil.parseDefaultDate(list2.get(1)),
						DateUtil.parseDefaultDate(list2.get(2)),
						list2.get(3), list2.get(4), list2.get(5));
			}
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		System.out.println("号码不存在："+buffer.toString());
		
		return null;
		
	}
	@RequestMapping("/uploadDeptIdentityLinks")
	@ResponseBody
	public String uploadDeptIdentityLinks(HttpSession session,HttpServletRequest request,@RequestParam("file") MultipartFile file) throws Exception{
		User createtor=(User) session.getAttribute("sysuser");
		if(createtor==null) return "redirect:/user/loginForm";
		File result=fileService.uploadSingleAndReturnFile(request, file);
		List<List<String>> list=ExcelUtils.readExcel(result);
		for (int i = 1; i < list.size(); i++) {
			List<String> list2=list.get(i);
			
			DeptIdentityLink deptIdentityLink=new DeptIdentityLink();
			//第一列为firstLevel，最底层部门
			if(!"".equals(list2.get(0))){
				Dept firstLevel=new Dept();
				firstLevel.setDid(Integer.valueOf(list2.get(0)));
				
				deptIdentityLink.setFirstLevel(firstLevel);
			}
			//第二列为secondLevel，次级部门
			if(!"".equals(list2.get(1))){
				Dept secondLevel=new Dept();
				secondLevel.setDid(Integer.valueOf(list2.get(1)));
				deptIdentityLink.setSecondLevel(secondLevel);
			}
			
			//第三列为thirdLevel，第三级部门
			if(!"".equals(list2.get(2))){
				Dept thirdLevel=new Dept();
	            thirdLevel.setDid(Integer.valueOf(list2.get(2)));
				deptIdentityLink.setThirdLevel(thirdLevel);
			}
			
			
			userService.saveDeptIdentityLink(deptIdentityLink);
		}
		return null;
		
	}
	@RequestMapping("/uploadDepts")
	@ResponseBody
	public String uploadDepts(HttpSession session,HttpServletRequest request,@RequestParam("file") MultipartFile file) throws Exception{
		User createtor=(User) session.getAttribute("sysuser");
		if(createtor==null) return "redirect:/user/loginForm";
		
		File result=fileService.uploadSingleAndReturnFile(request, file);
		List<List<String>> list=ExcelUtils.readExcel(result);
		
		for (int i = 1; i < list.size(); i++) {
			List<String> list2=list.get(i);
			Dept dept=new Dept();
			dept.setName(list2.get(0));
			userService.saveDept(dept);
			
		}
		return null;
		
	}
	@RequestMapping("/upload")
	@ResponseBody
	public String uploadUsers(HttpSession session,HttpServletRequest request,@RequestParam("file") MultipartFile file) throws Exception{
		User createtor=(User) session.getAttribute("sysuser");
		if(createtor==null) return "redirect:/user/loginForm";
		File result=fileService.uploadSingleAndReturnFile(request, file);
		List<List<String>> list=ExcelUtils.readExcel(result);
		
			
		
		for (int i = 1; i < list.size(); i++) {
			//保存用户基础数据
			List<String> list2=list.get(i);
			User user=new User();
			user.setPassword(list2.get(0));
			user.setUsername(list2.get(1));
			user.setPhone(list2.get(2));
			user.setEmail(list2.get(3));

			//设置职级
			Post post=new Post();
			post.setpId(Integer.valueOf(list2.get(4)));
			user.setPost(post);
			
			//设置用户登陆名
			user.setLoginname(list2.get(5));
			
			//设置头像
			user.setAvatar(list2.get(6));
			
			userService.save(user);
			
			//设置用户组
			sysPowerService.saveGroupToUser(user, createtor, (list2.get(7)).split(","));
			
			//设置部门
			userService.saveUserLinkDept(user.getId(),list2.get(8), list2.get(9), list2.get(10));
			
			
			
			
			
		}
		return null;
		
	}
	

}
