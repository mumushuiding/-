package com.crm.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController{
	@RequestMapping(value="/hello")
	public String hello(Model model){
		model.addAttribute("message", "Hello World!");
		return "welcome";
	}
	@RequestMapping(value={"ajax1","ajax2"})
	public String hello3(Model model){
		model.addAttribute("message", "Hello World!");
		return "welcome";
	}
	@RequestMapping(value="/hello2")
	public ModelAndView hello2(){
		ModelAndView mView=new ModelAndView();
		//���ģ�����ݣ������������POJO����
		mView.addObject("message", "Hello world2!");
		//�����߼���ͼ������ͼ����������ݸ����ֽ������������ͼҳ��
		mView.setViewName("/WEB-INF/content/welcome.jsp");
		return mView;
	}
	
}
