package com.crm.springboot.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.crm.springboot.pojos.user.User;

public class AuthorizationInterceptor implements HandlerInterceptor{

	//不拦截请求
		private List<String> getIgnoreUri(){
			List<String> result=new ArrayList<String>();
			result.add("/user/loginForm");
			result.add("/user/login");
			result.add("/index");
			result.add("/user/findPassword");
			result.add("/user/forgotPasswordForm");
			return result;
		}
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
		
			User user=(User) request.getSession().getAttribute("sysuser");
			
			
			boolean flag=false;
			//获取请求的路径进行判断
			String servletPath=request.getServletPath();
			for(String s:this.getIgnoreUri()){
				if(servletPath.contains(s)){
					flag=true;
					break;
				}
			}
			//拦截请求
			if(!flag){
				
				if(user==null){
					request.getRequestDispatcher("/user/loginForm").forward(request, response);
				}else{
					//用户已经登录，放行
					flag=true;
				}
			}
            System.out.println("flag="+flag);
			
			return flag;
		}

		@Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				ModelAndView modelAndView) throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
				throws Exception {
			
			
		}

	}
