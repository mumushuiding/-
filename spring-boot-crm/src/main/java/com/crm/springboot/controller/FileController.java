package com.crm.springboot.controller;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import com.crm.springboot.config.AvailableSettings;
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessBean;
import com.crm.springboot.pojos.ProcessEntity;
import com.crm.springboot.pojos.assess.Assessment;
import com.crm.springboot.pojos.assess.Evaluation;
import com.crm.springboot.pojos.assess.EvaluationForExport;
import com.crm.springboot.pojos.assess.Mark;
import com.crm.springboot.pojos.assess.MarkForExport;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.DeptIdentityLink;
import com.crm.springboot.pojos.user.DeptType;
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
import com.crm.springboot.utils.RegexUtils;

@Controller
@RequestMapping("/file")
public class FileController {
	private static final Log log=LogFactory.getLog(FileController.class);
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
	 * *******************下载总分
	 * @throws UnsupportedEncodingException *************
	 */
	@RequestMapping("/export/totalMark")
	public ResponseEntity<byte[]> exportTotalMark(
			@RequestParam(required=false) String startDate,
			@RequestParam(required=false)  String endDate,
			@RequestParam(required=false)  String posts,
			@RequestParam(required=false)  String groups) throws UnsupportedEncodingException{
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		//下载显示的文件名，解决中文名称乱码问题
		String filename="导出.xls";
		String downloadFileName=new String(filename.getBytes("UTF-8"),"ISO-8859-1");
		//通知浏览器以attachment(下载方式）打开
		headers.setContentDispositionFormData("attachment", downloadFileName);

		  //时间段
		  Date date=new Date();
		  startDate=startDate==null?DateUtil.formatDefaultDate(DateUtil.getFirstDayOfYear(date)):startDate;
		  endDate=endDate==null?DateUtil.formatDefaultDate(DateUtil.getLastDayOfYear(date)):endDate;
	      
		  //获取考核组下的所有二级部门和一级部门id,并将它们以字符串数组形式存入哈希表
		  HashMap<String, Object> params=null;
		 
		  if (groups!=null&&!"".equals(groups)) {
			  params=sysPowerService.getDepartmentsWithGroupname(groups, "考核组");
		  }else {
			  params=new HashMap<String, Object>();
		 }
	      
	      params.put("startDate", startDate);
		  params.put("endDate",endDate);
		  if("项目舞台负责人".equals(posts)){
			 params.put("xmwt", groups.split(","));
		  }else {
			 if(posts!=null && posts!="")params.put("postNames", posts.split(","));
		  }
		  params.put("checked", "1");
		  List<Mark> result=responsibilityService.selectTotalMarkWithAllUser(params);
		  List<Object> list=new ArrayList<>();
		  for (Mark mark : result) {
			if(mark.getUser().getRetire()==1) {
				System.out.println("已经离退");
				continue;
			}else {
				System.out.println("未离退");
			}
			MarkForExport export=new MarkForExport();
			export.setDeptname(userService.getDeptName(mark.getUser()));
			export.setTotalMark(mark.getTotalMark());
			export.setUsername(mark.getUser().getUsername());
			list.add(export);
		  }
		  return new ResponseEntity<byte[]>(ExcelUtils.exportDataToExcelAsByteArray(list, MarkForExport.getColnames()),headers,HttpStatus.CREATED);
		
	}
	/**
	 * *********************************半年考核和年度考核下载
	 * @throws UnsupportedEncodingException **************************
	 */
	@RequestMapping("/export/assessment/{taskType}")
	public ResponseEntity<byte[]> exportAssessment(HttpRequest request,
			@PathVariable String taskType,
			@RequestParam(required=false) String secondDeptId,
			@RequestParam(required=false) String times,
			@RequestParam(required=false) String assessGroup) throws UnsupportedEncodingException{
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		//下载显示的文件名，解决中文名称乱码问题
		String filename="导出.xls";
		String downloadFileName=new String(filename.getBytes("UTF-8"),"ISO-8859-1");
		//通知浏览器以attachment(下载方式）打开
		headers.setContentDispositionFormData("attachment", downloadFileName);
		//查询数据
		String businessType=responsibilityService.getBusinessType(taskType);
		String titleLike=responsibilityService.getTitleLikeForYear(taskType, times);
		HashMap<String, Object> params=sysPowerService.getDepartmentsWithGroupname(assessGroup, "考核组");
		if(params==null) params=new HashMap<String, Object>();
		params.put("businessType", businessType);
		
		params.put("titleLike", titleLike);
		//根据二级部门查询
		
		params.put("secondDeptId", secondDeptId);
		List<ProcessEntity> processEntities=responsibilityService.selectEvaluationWithUser(params);
	   
		List<Object> result=new ArrayList<Object>();
		
		for (ProcessEntity processEntity : processEntities) {
			EvaluationForExport export=new EvaluationForExport();
			export.setUsername(processEntity.getUser().getUsername());
			export.setPhone(processEntity.getUser().getPhone());
			export.setDeptName(processEntity.getUser().getUserLinkDepts().get(0).getFirstLevel().getName());
			
			export.setLeadershipEvaluation(processEntity.getEvaluation().getLeadershipEvaluation());
			
			export.setMarks(processEntity.getEvaluation().getMarks());
			
			export.setOverseerEvaluation(processEntity.getEvaluation().getOverseerEvaluation());
			
			export.setPublicEvaluation(processEntity.getEvaluation().getPublicEvaluation());
			
			export.setTotalMark(processEntity.getEvaluation().getTotalMark());
			
			export.setResult(processEntity.getEvaluation().getResult());
			
			result.add(export);
		}
		String colnames=EvaluationForExport.getColnames();
		
		return new ResponseEntity<byte[]>(ExcelUtils.exportDataToExcelAsByteArray(result, colnames),headers,HttpStatus.CREATED);
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
		//files.getFiles(AvailableSettings.TEMP_UPLOAD_LOCATION+pathname);
		files.getFiles(System.getProperty("user.dir")+File.separator+"upload"+File.separator+pathname);
		

	
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
	/**
	 * **********批量修改半年和全年申请表内容************************
	 */
	@RequestMapping("/upload/assessment/updateYear")
	@ResponseBody
	public String updateYear(HttpSession session,
			HttpServletRequest request,
			@RequestParam("file") MultipartFile file){
		int sucSize=0;
		StringBuffer buffer=new StringBuffer();
		try {
			File result=fileService.uploadSingleAndReturnFile(request, file);
			List<List<String>> list=ExcelUtils.readExcel(result);
			
			for (int i = 1; i < list.size(); i++) {
				List<String> list2=list.get(i);
				int size2=list2.size();
				System.out.println("*************size:"+size2);
				System.out.println("**************"+list.get(0).get(6));
				//检查电话和流程类型是否为空
				if (list2.get(1).trim().equals("")||list2.get(2).trim().equals("")) {
					buffer.append("第"+(i+1)+"行:"+list2.get(0)+"【电话】和【流程类型】不能为空,\n");
					continue;
				}
				//先查询电话是否存在
				
				String userId=userService.selectUserIdByPhone(list2.get(1).trim());
				if(userId==null) {
					buffer.append("第"+(i+1)+"行:，电话号码不存在,\n");
					continue;
				}
                //查询流程是否存在 
				Evaluation evaluation=responsibilityService.selectSingleEvaluationWithProcess(list2.get(1),list2.get(2));
				if(evaluation==null) {
					buffer.append("第"+(i+1)+"行:"+list2.get(0)+"【未填写】考核申请表，抑或是【流程类型】填写错误\n");
					continue;
				}
				//修改申请表内容
					//赋值，赋值前要判断输入类型是否有误
				if(!("").trim().equals(list2.get(3))){
					if(!Assessment.contains(list2.get(3))){
						buffer.append("第"+(i+1)+"行:修改失败，【"+list.get(0).get(3)+"】只能为：优秀，合格，基本合格，不合格\n");
						continue;
					}
					evaluation.setLeadershipEvaluation(list2.get(3));//领导点评
				}
				if(size2>4&&!("").trim().equals(list2.get(4))){
					if(!RegexUtils.isDouble(list2.get(4))){
						buffer.append("第"+(i+1)+"行:修改失败，【"+list.get(0).get(4)+"】只能为：数字\n");
						continue;
					}
					evaluation.setPublicEvaluation(list2.get(4));///群众评议
				}
				if(size2>5&&!("").trim().equals(list2.get(5))){
					if(!Assessment.contains(list2.get(5))){
						buffer.append("第"+(i+1)+"行:修改失败，【"+list.get(0).get(5)+"】只能为：优秀，合格，基本合格，不合格\n");
						continue;
					}
					evaluation.setOverseerEvaluation(list2.get(5));//组织考评
				}
				
				if(size2>6&&!("").trim().equals(list2.get(6))){
					if(!Assessment.contains(list2.get(6))){
						buffer.append("第"+(i+1)+"行:修改失败，【"+list.get(0).get(6)+"】只能为：优秀，合格，基本合格，不合格\n");
						continue;
					}
					evaluation.setResult(list2.get(6));//考核结果
				}
				
				//重新计算总分1.先重新计算考效总分;2然后再重新计算总分
				evaluation.setMarks(String.valueOf(responsibilityService.getTotalMarkOfAllMonthAssessmentWithBasemark(evaluation)));//先重新计算考效总分
				evaluation.setTotalMark(String.valueOf(responsibilityService.getTotalMarkOfEvaluation(evaluation)));//然后再重新计算总分
				//保存到数据库
				sucSize++;
				try {
					System.out.println(evaluation.toString());
					responsibilityService.updateEvaluation(evaluation);
				} catch (Exception e) {
					sucSize--;
					buffer.append("第"+(i+1)+"行:"+list2.get(0)+"更新失败,\n");
					e.printStackTrace();
				}
				
			}
			buffer.append("总条数："+(list.size()-1)+"条\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		buffer.append("成功导入："+sucSize+"条\n");
		return buffer.toString();
	}
	
	/**
	 * **********上传半年和全年考核群众评议结果************************
	 */
	@RequestMapping("/upload/assessment/data/{taskType}")
	@ResponseBody
	public String uploadAssessmentData(@PathVariable String taskType,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam("file") MultipartFile file){
		StringBuffer buffer=new StringBuffer();
		StringBuffer failBuffer=new StringBuffer("导入失败行数：\n");
        int sucSize=0;
		try {
			File result=fileService.uploadSingleAndReturnFile(request, file);
			List<List<String>> list=ExcelUtils.readExcel(result);
            buffer.append("1、每次导入都会重新计算考核量化分！！\n2、可以重复导入，请保留原始数据\n导入条数："+String.valueOf(list.size()-1)+"\n");
			for (int i = 1; i < list.size(); i++) {
				List<String> list2=list.get(i);
				String userId=userService.selectUserIdByPhone(list2.get(0).trim());
				if(userId==null) {
					failBuffer.append("第"+(i+1)+"行，电话号码不存在（第一列必须为电话号码）,\n");
					continue;
				}
				//先查询流程是否存在
				if (list2.get(0).trim().equals("")||list2.get(1).trim().equals("")) {
					failBuffer.append("第"+(i+1)+"行"+list2.get(0)+"第一列和第二列未填全,\n");
					continue;
				}
				if (list2.get(3).trim().equals("")) {
					failBuffer.append("第"+(i+1)+"行"+list2.get(0)+"部门未填,\n");
					continue;
				}
				Evaluation evaluation=responsibilityService.selectSingleEvaluationWithProcess(list2.get(0),list2.get(1));
				if(evaluation==null) {
					failBuffer.append("第"+(i+1)+"行"+list2.get(0)+"【未填写】考核申请表，抑或是【流程类型】填写错误\n");
					continue;
				}
				
				//赋值
				if(list2.get(3).contains("社直")){
					evaluation.setPublicRemark(""+list2.get(2)+"，分别召集社直员工、社直服务对象开展社直工作人员群众测评。应参加人数("+list2.get(4)+")人，实参加人数("+list2.get(5)+")人。（1）社直评议结果：发出推荐票("+list2.get(6)+")张，收回("+list2.get(7)+")张，有效票("+list2.get(8)+")张。该同志得票为：优秀("+list2.get(9)+")票，合格("+list2.get(10)+")票，基本合格("+list2.get(11)+")票，不合格("+list2.get(12)+")票。（2）服务对象评议结果：发出推荐票("+list2.get(14)+")张，收回("+list2.get(15)+")张，有效票("+list2.get(16)+")张。该同志得票为：优秀("+list2.get(17)+")票，合格("+list2.get(18)+")票，基本合格("+list2.get(19)+")票，不合格("+list2.get(20)+")票。");	
				}else {
					evaluation.setPublicRemark(""+list2.get(2)+"，报社一线考核群众评议会，对"+list2.get(3)+"一般工作人员进行群众评议。应参加人数("+list2.get(4)+")人，实参加人数("+list2.get(5)+")人，发出推荐票("+list2.get(6)+")张，收回("+list2.get(7)+")张，有效票("+list2.get(8)+")张。该同志群众评议得票为：优秀("+list2.get(9)+")票，合格("+list2.get(10)+")票，基本合格("+list2.get(11)+")票，不合格("+list2.get(12)+")票");
					
				}
				double publicE=0.0;
				if(list2.get(13)!=null) publicE=(double)Math.round(Double.valueOf(list2.get(13))*100)/100;
				evaluation.setPublicEvaluation(String.valueOf(publicE));
				
				sucSize++;
				try {
					
					evaluation.setMarks(String.valueOf(responsibilityService.getTotalMarkOfAllMonthAssessmentWithBasemark(evaluation)));
					evaluation.setTotalMark(String.valueOf(responsibilityService.getTotalMarkOfEvaluation(evaluation)));
					responsibilityService.updateEvaluation(evaluation);
				} catch (Exception e) {
					sucSize--;
					failBuffer.append("第"+(i+1)+"行"+list2.get(0)+"更新失败,\n");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		buffer.append("成功导入："+sucSize+"\n");
        buffer.append(failBuffer);
		return buffer.toString();
		
	}
	/**
	 * 批量导入加减分
	 * @param session
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping("/uploadProjectAndMark")
	@ResponseBody
	public String uploadProjectAndMark(HttpSession session,
			HttpServletRequest request,
			@RequestParam("file") MultipartFile file,
			@RequestParam(required=false) Integer completed){
		
		completed=completed==null?0:completed;
		
		
		StringBuffer failBuffer=new StringBuffer("导入失败行数：\n");
		List<List<String>> list=null;
		try {
			File result=fileService.uploadSingleAndReturnFile(request, file);
			list=ExcelUtils.readExcel(result);
			if(list.size()<=1) return "导入行数小于2，仔细检查导入表";
			for (int i = 1; i < list.size(); i++) {
				List<String> list2=list.get(i);
				String userId=userService.selectUserIdByPhone(list2.get(0).trim());
				if(userId==null) {
					failBuffer.append("第"+(i+1)+"行，电话号码不存在（第一列必须为电话号码）,导入失败\n");
					continue;
				}


				responsibilityService.saveProjectAndMark(userId, 
						DateUtil.parseDefaultDate(list2.get(1)),
						DateUtil.parseDefaultDate(list2.get(2)),
						list2.get(3), list2.get(4), list2.get(5),completed);
			}
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		failBuffer.insert(0, "1、重复条目自动略过\n导入条数："+String.valueOf(list.size()-1)+"\n");

		return failBuffer.toString();
		
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
/**
 * **********************************************************部门*********************************************
 */
	@RequestMapping("/uploadTypeOfDept")
	@ResponseBody
	public String uploadTypeOfDepts(HttpSession session,HttpServletRequest request,
			@RequestParam("file") MultipartFile file) throws Exception{
		File result=fileService.uploadSingleAndReturnFile(request, file);
		List<List<String>> list=ExcelUtils.readExcel(result);
		StringBuffer buffer=new StringBuffer();
		for(int i=1;i<list.size();i++){
			List<String> list2=list.get(i);
			
			DeptType deptType=new DeptType();
			deptType.setDeptId(Integer.valueOf(list2.get(0)));
			deptType.setDicId(Integer.valueOf(list2.get(1)));
			if(userService.selectSingleDept(deptType.getDeptId())==null){
				buffer.append("第"+(i+1)+"行,对应的部门id不存在\r\n");
				continue;
			}
			if(userService.selectAllDeptType(deptType.getDeptId(), deptType.getDicId())!=null) {
				buffer.append("第"+(i+1)+"行,在数据中已经存在\r\n");
				continue;
			}
			
			userService.saveDeptType(deptType);
			
		}
		
		return buffer.toString();
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
