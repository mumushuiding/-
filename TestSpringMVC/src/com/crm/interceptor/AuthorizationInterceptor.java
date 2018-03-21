package com.crm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.crm.pojos.User;

public class AuthorizationInterceptor implements HandlerInterceptor{
	private static final Log logger=LogFactory.getLog(AuthorizationInterceptor.class);
   //以下为不拦截
	private static final String[] IGNORE_URI={"user/loginForm","user/login","user/registerForm","user/register"};
    /**
     * 该方法将在整个请求完成之后执行，主要作用是用于清理资源
     * 该方法也只能在当前Interceptor的preHandle方法的返回值为true时都会执行。
     */
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		logger.info("AuthorizationInterceptor afterCompletion -->");
		
	}
    /**
     * 该方法将在Controller的方法调用之后执行，方法中可以对ModelAndView进行操作，
     * 该方法也只能 在当前Interceptor的preHandle方法的返回值为true时才会执行。
     */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		logger.info("AuthorizationInterceptor postHandle-->");
		
	}
    /**
     * preHandle方法是进行处理器拦截用的，该方法将在Controller处理之前进行调用，
     * 该方法的返回值为true拦截器才会继续往下执行，该方法的返回值为false的时候整个请求就结束了
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println("AuthorizationInterceptor preHandle-->");
		boolean flag=false;
		//获取请求路径进行判断
		String servletPath=request.getServletPath();
		logger.info("请求路径为："+servletPath);
		//判断请求是否需要拦截
		for(String s:IGNORE_URI){
			if(servletPath.contains(s)){
				flag=true;
				break;
			}
		}
		//拦截请求
		if(!flag){
			User user=(User)request.getSession().getAttribute("user");
			if(user==null){
				logger.info("未登录，拦截");
				request.setAttribute("message", "请先登录再访问网站");
				request.getRequestDispatcher("user/loginForm").forward(request, response);
			}else{
				logger.info("已经登录，放行");
				flag=true;
			}
		}
		return false;
	}

}
