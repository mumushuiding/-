package com.crm.springboot.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crm.springboot.mapper.GroupMapper;
import com.crm.springboot.pojos.Group;
import com.crm.springboot.pojos.User;
import com.crm.springboot.service.GroupService;


@Controller
@RequestMapping("/group")
public class GroupController {
	@Autowired
	private GroupService groupService;
	@Autowired
	private IdentityService identityService;
	
	@RequestMapping(value="/{location}")
	public String system(@PathVariable String location,Model model){
		if("addgroupForm".equals(location)){
			model.addAttribute("group", new Group());
		}
		return "system/group/"+location;
	}
	
	@RequestMapping("/addgroup")
	public String addgroup(@ModelAttribute("group") Group group,Model model,HttpSession session){
		User user=(User) session.getAttribute("sysuser");
		if(user==null) return "redirect:/user/loginForm";
		group.setCreatorid(user.getId());
		group.setCreatorname(user.getPhone());
		group.setCreatedDate(new Date());
		
		group=groupService.save(group);

		if(group.getGroupid()!=0){
			org.activiti.engine.identity.Group g2=identityService.newGroup(Integer.toString(group.getGroupid()));
			g2.setName(group.getGroupname());
			g2.setType(group.getGroupType());
			identityService.saveGroup(g2);
		}
		
		model.addAttribute("msg", "添加成功");
		return null;
	}
}
